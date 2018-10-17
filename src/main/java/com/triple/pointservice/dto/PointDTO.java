package com.triple.pointservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PointDTO {
    private String id;
    private String content;
    private int point;
    private String type;
    private String relatedId;
    private String action;
}
