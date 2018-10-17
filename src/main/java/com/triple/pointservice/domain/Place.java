package com.triple.pointservice.domain;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Place {
    @Id
    @Length(max = 36)
    private String id;
    @NotNull
    @Length(min = 1, max = 255)
    String name;
}
