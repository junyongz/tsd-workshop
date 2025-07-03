package com.tsd.workshop.sparepart.media;

import com.tsd.workshop.sparepart.media.data.SparePartMedia;
import com.tsd.workshop.sparepart.media.data.SparePartMediaRepository;
import com.tsd.workshop.sparepart.media.data.SparePartMediaSqlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class SparePartMediaService {

    @Autowired
    private SparePartMediaRepository sparePartMediaRepository;

    @Autowired
    private SparePartMediaSqlRepository sparePartMediaSqlRepository;

    public Mono<SparePartMedia> saveMedia(SparePartMedia media) {
        return this.sparePartMediaRepository.save(media);
    }

    @Transactional(readOnly = true)
    public Flux<SparePartMedia> getMediaBySparePartId(Long sparePartId) {
        return this.sparePartMediaRepository.findAllBySparePartId(sparePartId);
    }

    @Transactional(readOnly = true)
    public Mono<SparePartMedia> getMediaBinary(Long mediaId) {
        return sparePartMediaRepository.findById(mediaId);
    }

    public Mono<Void> deleteMedia(Long sparePartId, Long mediaId) {
        return sparePartMediaRepository.findSparePartIdById(mediaId)
                .map(actualSparePartId -> {
                    if (!actualSparePartId.equals(sparePartId)) {
                        throw new WrongMediaOwnerException(mediaId, sparePartId);
                    }
                    return actualSparePartId;
                }).flatMap(actualSparePartId -> sparePartMediaRepository.deleteById(mediaId));
    }

    @Transactional(readOnly = true)
    public Mono<Map<Long, Integer>> groupedSparePartIdCounts() {
        return sparePartMediaSqlRepository.groupedSparePartIdCounts();
    }

    @Transactional(readOnly = true)
    public Mono<Integer> countBySparePartId(Long sparePartId) {
        return sparePartMediaRepository.countBySparePartId(sparePartId);
    }
}
