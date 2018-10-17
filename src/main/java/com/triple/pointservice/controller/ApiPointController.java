package com.triple.pointservice.controller;

import com.triple.pointservice.dto.EventDTO;
import com.triple.pointservice.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/points")
public class ApiPointController {
    @Autowired
    private PointService pointService;

    @GetMapping("")
    public ResponseEntity<Integer> getPersonalPoint(HttpSession session) {
        //현재 세션에 저장된 아이디를 이용해 회원 포인트 조회
        return new ResponseEntity<>(pointService.getPersonalPoint(session.getAttribute("LOGIN_USER").getId()), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Void> storePointHistory(@RequestBody @Valid EventDTO eventDTO) {
        pointService.storePointHistory(eventDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
