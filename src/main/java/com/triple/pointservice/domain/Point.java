package com.triple.pointservice.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
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

}
