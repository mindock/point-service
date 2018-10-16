package com.triple.pointservice.controller;

import com.triple.pointservice.dto.EventDTO;
import com.triple.pointservice.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/points")
public class ApiPointController {
    @Autowired
    private PointService pointService;

    @PostMapping("")
    public ResponseEntity<Void> storePointHistory(@RequestBody EventDTO eventDTO) {
        pointService.storePointHistory(eventDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
