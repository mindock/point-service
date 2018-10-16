package com.triple.pointservice.repository;

import com.triple.pointservice.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, String> {
}
