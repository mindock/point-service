package com.triple.pointservice.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(
indexes = {@Index(name = "REVIEW_INDEX_USER", columnList = "user_id"),
    @Index(name = "REVIEW_INDEX_PLACE", columnList = "place_id")})
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @Length(max = 36)
    private String id;
    @ManyToOne
    @JoinColumn
    private User user;
    @ManyToOne
    @JoinColumn
    private Place place;
    @NotNull
    @Length(min = 1, max = 255)
    private String content;
    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReviewPhoto> reviewPhotos;
    private boolean deleted = false;
}
