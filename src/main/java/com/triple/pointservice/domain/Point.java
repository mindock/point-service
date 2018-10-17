package com.triple.pointservice.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
@NoArgsConstructor
public class Point {
    @Id
    private String id;
    @ManyToOne
    @JoinColumn
    private User user;
    private String content;
    private int point;
    private String pointType;
    private String relatedId;
    private String action;

    public Point(User user, String content, int point, String pointType, String relatedId, String action) {
        this.id = UUID.randomUUID().toString();
        this.user = user;
        this.content = content;
        this.point = point;
        this.pointType = pointType;
        this.relatedId = relatedId;
        this.action = action;
    }
}
