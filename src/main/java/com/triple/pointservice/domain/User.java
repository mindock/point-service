package com.triple.pointservice.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    private String id;

    private String name;
    private String email;
    private String password;

    private int point;

    public User addPoint(int point) {
        this.point += point;
        return this;
    }
}
