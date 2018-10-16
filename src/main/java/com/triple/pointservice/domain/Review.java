package com.triple.pointservice.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
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
