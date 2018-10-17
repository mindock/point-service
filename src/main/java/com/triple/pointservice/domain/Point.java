package com.triple.pointservice.domain;

import com.triple.pointservice.dto.PointDTO;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(indexes = {
        @Index(name = "POINT_INDEX_USER", columnList = "user_id"),
        @Index(name = "POINT_INDEX_TYPE_AND_RELATED", columnList = "type, relatedId")
})
@NoArgsConstructor
public class Point {
    @Id
    private String id;
    @ManyToOne
    @JoinColumn
    private User user;
    private String content;
    private int point;
    private String type;
    private String relatedId;
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
