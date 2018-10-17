package com.triple.pointservice.service;

import com.triple.pointservice.domain.Place;
import com.triple.pointservice.domain.Point;
import com.triple.pointservice.domain.User;
import com.triple.pointservice.dto.EventDTO;
import com.triple.pointservice.repository.PlaceRepository;
import com.triple.pointservice.repository.PointRepository;
import com.triple.pointservice.repository.ReviewRepository;
import com.triple.pointservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PointService {
    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaceRepository placeRepository;

    public void storePointHistory(EventDTO eventDTO) {
        //유저가 없는 경우, UserNotFoundException 발생
        IF userRepository.existsById(eventDTO.getUserId()) is false
            throw new UserNotFoundException()
        ELSE
            user = userRepository.findById(eventDTO.getUserId())

        type = eventDTO.getType()
        IF type == "REVIEW"
           storeReviewPoint(eventDTO, user)
    }

    private void storeReviewPoint(EventDTO eventDTO, User user) {
        //장소가 없는 경우, PlaceNotFoundException 발생
        IF placeRepository.existsById(eventDTO.getPlaceId()) is false
            throw new PlaceNotFoundException()

        //리뷰가 없는 경우, ReviewNotFoundException 발생
        IF reviewRepository.existsById(eventDTO.getReviewId()) is false
            throw new ReviewNotFoundException()

        action = eventDTO.getAction()
        IF action == "ADD"
            addReviewPoint(eventDTO, user)
        ELSE IF action == "MOD"
            modifyReviewPoint(eventDTO, user)
        ELSE
            deleteReviewPoint(eventDTO, user)
    }

    @Transactional
    private void addReviewPoint(EventDTO eventDTO, User user) {
        addBonusPoint(eventDTO, user);
        //사진 첨부하지 않은 경우, 포인트 1점
        IF eventDTO.getAttachedPhotoIds().size() == 0 {
            pointRepository.save(new Point(user, "리뷰 작성", 1,
                    eventDTO.getType(), eventDTO.getReviewId(), eventDTO.getAction()))
            return;
        }
        //사진 첨부한 경우, 포인트 2점
        pointRepository.save(new Point(user, "리뷰 작성", 2,
                eventDTO.getType(), eventDTO.getReviewId(), eventDTO.getAction()))
    }

    private void addBonusPoint(EventDTO eventDTO, User user, Place place) {
        IF reviewRepository.existsByPlaceIdEqualsAndDeletedFalse(eventDTO.getPlaceId()) is true {
            return;
        }
        //사용자 입장에서 본 첫 리뷰인 경우, 보너스 점수 1점
        pointRepository.save(new Point(user,"첫 리뷰", 1,
                "BONUS", eventDTO.getReviewId(), eventDTO.getAction()))
    }

    private void modifyReviewPoint(EventDTO eventDTO, User user) {
        reviewPoint = getReviewPoint(eventDTO.getReviewId());
        //보너스 점수 받은 경우를 제외한 해당 리뷰 포인트 계산
        IF pointRepository.existsByPointTypeEqualsAndRelatedIdEquals("BONUS", eventDTO.getReviewId()) is true
            reviewPoint = reviewPoint - 1

        //포인트 변동이 없는 경우,
        IF (reviewPoint == 1 AND eventDTO.getAttachedPhotoIds().size() == 0) OR (reviewPoint == 2 AND eventDTO.getAttachedPhotoIds().size() != 0)
            return;
        //사진을 새롭게 올린 경우, 포인트 1점
        IF reviewPoint == 1 {
            pointRepository.save(new Point(user, "리뷰 수정", 1,
                    eventDTO.getType(), eventDTO.getReviewId(), eventDTO.getAction()))
            return;
        }
        //사진을 모두 지운 경우, 포인트 -1점
        pointRepository.save(new Point(user, "리뷰 수정", -1,
                eventDTO.getType(), eventDTO.getReviewId(), eventDTO.getAction()))
    }

    private void deleteReviewPoint(EventDTO eventDTO, User user) {
        //(현재까지 해당 리뷰를 통해 얻은 포인트) * -1 저장 = 해당 리뷰 포인트 = 0
        pointRepository.save(new Point(user, "리뷰 삭제", getReviewPoint(eventDTO.getReviewId()) * -1,
                eventDTO.getType(), eventDTO.getReviewId(), eventDTO.getAction()))
    }

    private int getReviewPoint(String reviewId) {
        //Type이 review이며 reviewId가 같은 pointHistory 리스트를 받아 해당 리뷰 포인트를 구한다
        pointHistoryList = pointRepository.findByPointTypeEqualsAndRelatedIdEquals("REVIEW", reviewId)
        return getPointSum(pointHistoryList);
    }

    public int getPersonalPoint(String userId) {
        //유저가 없는 경우, UserNotFoundException 발생
        IF userRepository.existsById(eventDTO.getUserId()) is false
            throw new UserNotFoundException()
        ELSE
            user = userRepository.findById(eventDTO.getUserId())

        //해당 유저의 point 기록을 이용해 포인트를 구함
        pointHistoryList = pointRepository.findByUserId(userId)
        return getPointSum(pointHistoryList)
    }

    private int getPointSum(List<Point> pointList) {
        totalPoint = 0
        FOR index = 0 to pointList.size() - 1
            totalPoint = totalPoint + pointList.get(index).getPoint()

        return totalPoint
    }
}
