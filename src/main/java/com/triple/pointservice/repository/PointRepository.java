package com.triple.pointservice.repository;

import com.triple.pointservice.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, String> {
}
