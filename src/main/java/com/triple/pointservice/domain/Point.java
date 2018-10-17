package com.triple.pointservice.domain;

import com.triple.pointservice.dto.PointDTO;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(indexes = {
        @Index(name = "POINT_INDEX_USER", columnList = "user_id"),
        @Index(name = "POINT_INDEX_TYPE_AND_RELATED", columnList = "type, relatedId")
})
@NoArgsConstructor
public class Point {
    @Id
    @Length(max = 36)
    private String id;
    @ManyToOne
    @JoinColumn
    private User user;
    @NotNull
    @Length(min = 1, max = 25)
    private String content;
    private int point;
    @Length(max = 10)
    private String type;
    @Length(max = 36)
    private String relatedId;
    @Length(max = 10)
    private String action;

    public Point(User user, String content, int point, String type, String relatedId, String action) {
        this.id = UUID.randomUUID().toString();
        this.user = user;
        this.content = content;
        this.point = point;
        this.type = type;
        this.relatedId = relatedId;
        this.action = action;
    }

    public PointDTO toDTO() {
        return new PointDTO(id, content, point, type, relatedId, action);
    }
}
