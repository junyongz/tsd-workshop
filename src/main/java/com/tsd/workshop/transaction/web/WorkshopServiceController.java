package com.tsd.workshop.transaction.web;

import com.tsd.workshop.transaction.State;
import com.tsd.workshop.transaction.TransactionService;
import com.tsd.workshop.transaction.TransactionType;
import com.tsd.workshop.transaction.VehicleOngoingServiceException;
import com.tsd.workshop.transaction.data.WorkshopService;
import com.tsd.workshop.transaction.media.WorkshopServiceMedia;
import com.tsd.workshop.transaction.media.WorkshopServiceMediaService;
import com.tsd.workshop.transaction.media.WrongMediaOwnerException;
import com.tsd.workshop.transaction.utilization.SparePartUsageService;
import com.tsd.workshop.transaction.utilization.data.SparePartUsage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/workshop-services")
public class WorkshopServiceController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private SparePartUsageService sparePartUsageService;

    @Autowired
    private WorkshopServiceMediaService workshopServiceMediaService;

    @GetMapping("/{id}")
    public Mono<WorkshopService> getSingle(@PathVariable Long id) {
        return transactionService.findById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Long> deleteSingle(@PathVariable Long id) {
        return transactionService.deleteById(id);
    }

    @GetMapping
    public Flux<WorkshopService> getWorkshopServices(
            @RequestParam(name = "vehicleId", required = false) Long vehicleId,
            @RequestParam(name="type", required = false) TransactionType[] transactionTypes,
            @RequestParam(name="pageNumber", required = false, defaultValue = "-1") int pageNum,
            @RequestParam(name="pageSize", required = false, defaultValue = "-1") int pageSize,
            @RequestParam(name="year", required = false, defaultValue = "-1") int year,
            @RequestParam(name="month", required = false, defaultValue = "-1") int month,
            @RequestParam(name="keyword", required = false) List<String> keywords,
            @RequestParam(name="state", required = false) State state
    ) {
        if (vehicleId != null) {
            return transactionService.findByVehicleId(vehicleId);
        }
        if (transactionTypes != null) {
            return transactionService.findLatestByTransactionTypes(transactionTypes);
        }
        if (pageNum >= 0 && pageSize >= 0) {
            return transactionService.findWithPages(pageNum, pageSize);
        }
        if (year > 0 && month > 0) {
            return transactionService.findByYearAndMonth(year, month);
        }
        if (keywords != null && !keywords.isEmpty()) {
            return transactionService.searchByKeywords(keywords);
        }
        if (state != null) {
            return transactionService.findByState(state);
        }

        return transactionService.findAll();
    }

    @PostMapping
    public Mono<WorkshopService> saveWorkshopService(@RequestBody WorkshopService workshopService,
                                                     @RequestParam(name = "op", required = false) Operation op) {
        if (op == Operation.COMPLETE) {
            if (workshopService.getCompletionDate() == null) {
                workshopService.setCompletionDate(LocalDate.now());
            }
            return transactionService.completeService(workshopService);
        }

        if (op == Operation.NOTE) {
            return transactionService.updateNote(workshopService);
        }

        Mono<WorkshopService> updateRoutine = transactionService.findByVehicleId(workshopService.getVehicleId())
                .collectList()
                .flatMap(wss -> {
                    if (!wss.isEmpty() && !Objects.equals(wss.getFirst().getId(), workshopService.getId())) {
                        throw new VehicleOngoingServiceException(wss.getFirst().getVehicleNo(), wss.getFirst().getStartDate());
                    }
                    return Mono.empty();
                })
                .then(transactionService.save(workshopService));

        List<SparePartUsage> sparePartUsages = workshopService.getSparePartUsages();
        if (sparePartUsages != null && !sparePartUsages.isEmpty()) {
            return sparePartUsageService.validateSparePartUsageByQuantity(sparePartUsages)
                    .all(Boolean.TRUE::equals)
                    .flatMap(truly -> updateRoutine);
        }

        return updateRoutine;
    }

    @PostMapping(value = "/{serviceId}/medias")
    public Mono<Long> uploadMedia(@PathVariable Long serviceId, @RequestPart("file") Mono<FilePart> filePartMono) {
        return filePartMono.flatMap(filePart -> {

            return filePart.content().reduce(DataBuffer::write)
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return bytes;
                })
                .map(bytes -> {
                    WorkshopServiceMedia media = new WorkshopServiceMedia();
                    media.setServiceId(serviceId);
                    media.setFileName(filePart.filename());
                    media.setFileSize(bytes.length);
                    media.setMedia(bytes);
                    media.setMediaType(filePart.headers().getContentType().toString());
                    media.setAddedTimestamp(LocalDateTime.now());
                    return media;
                })
                .flatMap(this.workshopServiceMediaService::saveMedia)
                .map(WorkshopServiceMedia::getId);
        }) ;
    }

    @DeleteMapping(value = "/{serviceId}/medias/{mediaId}")
    public Mono<Long> deleteMedia(@PathVariable Long serviceId, @PathVariable("mediaId") Long mediaId) {
        return workshopServiceMediaService.deleteMedia(serviceId, mediaId)
                .then(Mono.empty());
    }

    @GetMapping(value = "/{serviceId}/medias")
    public Flux<WorkshopServiceMedia> fetchMedias(@PathVariable Long serviceId) {
        return workshopServiceMediaService.getMediaByServiceId(serviceId);
    }

    @GetMapping(value = "/{serviceId}/medias/{mediaId}/data")
    public Mono<ResponseEntity<DataBuffer>> fetchMediaBinary(@PathVariable Long serviceId, @PathVariable Long mediaId) {
        DataBufferFactory bufferFactory = new DefaultDataBufferFactory();
        return workshopServiceMediaService.getMediaBinary(serviceId, mediaId)
                .map(media -> {
                    if (!media.getServiceId().equals(serviceId)) {
                        throw new WrongMediaOwnerException(mediaId, serviceId);
                    }

                    DataBuffer buffer = bufferFactory.wrap(media.getMedia());
                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(media.getMediaType()))
                            .body(buffer);
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}
