package com.triple.pointservice.domain;

import javax.persistence.*;

@Entity
@Table(indexes = {@Index(name = "REVIEW_PHOTO_INDEX_REVIEW", columnList = "review_id")})
public class ReviewPhoto {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn
    private Review review;
}
