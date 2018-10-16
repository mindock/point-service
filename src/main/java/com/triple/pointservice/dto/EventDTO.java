package com.triple.pointservice.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class EventDTO {
    private String type;
    private String action;
    private String reviewId;
    private String content;
    private List<String> attachedPhotoIds;
    private String userId;
    private String placeId;
}
