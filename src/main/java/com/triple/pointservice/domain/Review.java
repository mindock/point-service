package com.triple.pointservice.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(
indexes = {@Index(name = "REVIEW_INDEX_USER", columnList = "user_id"),
    @Index(name = "REVIEW_INDEX_PLACE", columnList = "place_id")})
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    private String id;
    @ManyToOne
    @JoinColumn
    private User user;
    @ManyToOne
    @JoinColumn
    private Place place;
    private String content;
    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReviewPhoto> reviewPhotos;
    private boolean deleted;
}
