package com.triple.pointservice.repository;

import com.triple.pointservice.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, String> {
    boolean existsByPointTypeEqualsAndRelatedIdEquals(String pointType, String relatedId);
    List<Point> findByPointTypeEqualsAndRelatedIdEquals(String pointType, String relatedId);
}
