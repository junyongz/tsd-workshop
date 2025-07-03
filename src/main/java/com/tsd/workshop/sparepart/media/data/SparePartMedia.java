package com.tsd.workshop.sparepart.media.data;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.InsertOnlyProperty;

import java.time.LocalDateTime;

public class SparePartMedia {
    @Id
    private Long id;
    private Long sparePartId;
    @CreatedDate
    @InsertOnlyProperty
    private LocalDateTime addedTimestamp;
    private String fileName;
    private Integer fileSize;
    private byte[] media;
    private String mediaType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSparePartId() {
        return sparePartId;
    }

    public void setSparePartId(Long sparePartId) {
        this.sparePartId = sparePartId;
    }

    public LocalDateTime getAddedTimestamp() {
        return addedTimestamp;
    }

    public void setAddedTimestamp(LocalDateTime addedTimestamp) {
        this.addedTimestamp = addedTimestamp;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getMedia() {
        return media;
    }

    public void setMedia(byte[] media) {
        this.media = media;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
}
