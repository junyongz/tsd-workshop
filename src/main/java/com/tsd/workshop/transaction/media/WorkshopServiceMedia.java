package com.tsd.workshop.transaction.media;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.InsertOnlyProperty;

import java.time.LocalDateTime;

public class WorkshopServiceMedia {

    @Id
    private Long id;
    private Long serviceId;
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

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public LocalDateTime getAddedTimestamp() {
        return addedTimestamp;
    }

    public void setAddedTimestamp(LocalDateTime addedTimestamp) {
        this.addedTimestamp = addedTimestamp;
    }

    public byte[] getMedia() {
        return media;
    }

    public void setMedia(byte[] media) {
        this.media = media;
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

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
}
