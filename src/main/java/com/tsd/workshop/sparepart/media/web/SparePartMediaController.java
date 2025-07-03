package com.tsd.workshop.sparepart.media.web;

import com.tsd.workshop.sparepart.media.SparePartMediaService;
import com.tsd.workshop.sparepart.media.WrongMediaOwnerException;
import com.tsd.workshop.sparepart.media.data.SparePartMedia;
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

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/spare-parts")
public class SparePartMediaController {

    @Autowired
    private SparePartMediaService sparePartMediaService;

    @PostMapping(value = "/{sparePartId}/medias")
    public Flux<Long> uploadMedia(@PathVariable Long sparePartId, @RequestPart("file") Flux<FilePart> filePartMono) {
        return filePartMono.flatMap(filePart ->
                filePart.content().reduce(DataBuffer::write)
                        .map(dataBuffer -> {
                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bytes);
                            DataBufferUtils.release(dataBuffer);
                            return bytes;
                        })
                        .map(bytes -> {
                            SparePartMedia media = new SparePartMedia();
                            media.setSparePartId(sparePartId);
                            media.setFileName(filePart.filename());
                            media.setFileSize(bytes.length);
                            media.setMedia(bytes);
                            media.setMediaType(filePart.headers().getContentType().toString());
                            media.setAddedTimestamp(LocalDateTime.now());
                            return media;
                        })
                        .flatMap(this.sparePartMediaService::saveMedia)
                        .map(SparePartMedia::getId)
        ) ;
    }

    @DeleteMapping(value = "/{sparePartId}/medias/{mediaId}")
    public Mono<Long> deleteMedia(@PathVariable Long sparePartId, @PathVariable("mediaId") Long mediaId) {
        return sparePartMediaService.deleteMedia(sparePartId, mediaId)
                .then(Mono.empty());
    }

    @GetMapping(value = "/{sparePartId}/medias")
    public Flux<SparePartMedia> fetchMedias(@PathVariable Long sparePartId) {
        return sparePartMediaService.getMediaBySparePartId(sparePartId);
    }

    @GetMapping(value = "/{sparePartId}/medias/{mediaId}/data")
    public Mono<ResponseEntity<DataBuffer>> fetchMediaBinary(@PathVariable Long sparePartId, @PathVariable Long mediaId) {
        DataBufferFactory bufferFactory = new DefaultDataBufferFactory();
        return sparePartMediaService.getMediaBinary(mediaId)
                .map(media -> {
                    if (!media.getSparePartId().equals(sparePartId)) {
                        throw new WrongMediaOwnerException(mediaId, sparePartId);
                    }

                    DataBuffer buffer = bufferFactory.wrap(media.getMedia());
                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(media.getMediaType()))
                            .body(buffer);
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

}
