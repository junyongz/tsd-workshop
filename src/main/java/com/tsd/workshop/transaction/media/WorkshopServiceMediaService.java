package com.tsd.workshop.transaction.media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Transactional
@Service
public class WorkshopServiceMediaService {

    @Autowired
    private WorkshopServiceMediaRepository workshopServiceMediaRepository;

    @Autowired
    private WorkshopServiceMediaSqlRepository workshopServiceMediaSqlRepository;

    public Mono<WorkshopServiceMedia> saveMedia(WorkshopServiceMedia media) {
        return this.workshopServiceMediaRepository.save(media);
    }

    @Transactional(readOnly = true)
    public Flux<WorkshopServiceMedia> getMediaByServiceId(Long serviceId) {
        return this.workshopServiceMediaRepository.findAllByServiceId(serviceId);
    }

    @Transactional(readOnly = true)
    public Mono<WorkshopServiceMedia> getMediaBinary(Long serviceId, Long mediaId) {
        return workshopServiceMediaRepository.findById(mediaId);
    }

    public Mono<Void> deleteMedia(Long serviceId, Long mediaId) {
        return workshopServiceMediaRepository.findServiceIdById(mediaId)
                .map(actualServiceId -> {
                    if (!actualServiceId.equals(serviceId)) {
                        throw new WrongMediaOwnerException(mediaId, serviceId);
                    }
                    return actualServiceId;
                }).flatMap(actualServiceId -> workshopServiceMediaRepository.deleteById(mediaId));
    }

    @Transactional(readOnly = true)
    public Mono<Map<Long, Integer>> groupedServiceIdCounts() {
        return workshopServiceMediaSqlRepository.groupedServiceIdCounts();
    }

    @Transactional(readOnly = true)
    public Mono<Integer> countByServiceId(Long serviceId) {
        return workshopServiceMediaRepository.countByServiceId(serviceId);
    }

}
