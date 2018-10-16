package com.triple.pointservice.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ReviewPhoto {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn
    private Review review;
}
