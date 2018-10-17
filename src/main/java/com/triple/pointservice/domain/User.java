package com.triple.pointservice.domain;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class User {
    @Id
    @Length(max = 36)
    private String id;

    @NotNull
    @Length(min = 1, max = 50)
    private String name;

    @Length(max = 50)
    private String email;

    @NotNull
    @Length(min = 8, max = 50)
    private String password;

    private int point;

    public User addPoint(int point) {
        this.point += point;
        return this;
    }
}
