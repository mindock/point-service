package com.triple.pointservice.controller;

import com.triple.pointservice.dto.EventDTO;
import com.triple.pointservice.dto.PointDTO;
import com.triple.pointservice.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/points")
public class ApiPointController {
    @Autowired
    private PointService pointService;

    @GetMapping("")
    public ResponseEntity<List<PointDTO>> getPersonalPointHistory(HttpSession session) {
        //현재 세션에 저장된 아이디를 이용해 회원 포인트 이력 조회
        return new ResponseEntity<>(pointService.getPersonalPointHistory(session.getAttribute("LOGIN_USER").getId()), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Void> storePointHistory(@RequestBody @Valid EventDTO eventDTO) {
        pointService.storePointHistory(eventDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
