package com.triple.pointservice.domain;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(indexes = {@Index(name = "REVIEW_PHOTO_INDEX_REVIEW", columnList = "review_id")})
public class ReviewPhoto {
    @Id
    @Length(max = 36)
    private String id;

    @ManyToOne
    @JoinColumn
    private Review review;

    @NotNull
    @Length(min = 1, max = 100)
    private String url;
}
