package com.triple.pointservice.service;

import com.triple.pointservice.domain.Point;
import com.triple.pointservice.domain.User;
import com.triple.pointservice.dto.EventDTO;
import com.triple.pointservice.dto.PointDTO;
import com.triple.pointservice.repository.PlaceRepository;
import com.triple.pointservice.repository.PointRepository;
import com.triple.pointservice.repository.ReviewRepository;
import com.triple.pointservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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
        User user = userRepository.findById(eventDTO.getUserId()).orElseThrow(UserNotFoundException.class);

        String type = eventDTO.getType();
        if(type.equals("REVIEW"))
           storeReviewPoint(eventDTO, user);
    }

    private void storeReviewPoint(EventDTO eventDTO, User user) {
        //장소가 없는 경우, PlaceNotFoundException 발생
        if(!placeRepository.existsById(eventDTO.getPlaceId()))
            throw new PlaceNotFoundException();

        //리뷰가 없는 경우, ReviewNotFoundException 발생
        if(!reviewRepository.existsById(eventDTO.getReviewId()))
            throw new ReviewNotFoundException();

        String action = eventDTO.getAction();
        if(action.equals("ADD"))
            addReviewPoint(eventDTO, user);
        else if(action.equals("MOD"))
            modifyReviewPoint(eventDTO, user);
        else
            deleteReviewPoint(eventDTO, user);
    }

    @Transactional
    public void addReviewPoint(EventDTO eventDTO, User user) {
        int bonusPoint = addBonusPoint(eventDTO, user);
        //사진 첨부하지 않은 경우,
        if(eventDTO.getAttachedPhotoIds().size() == 0) {
            // 포인트 1점
            pointRepository.save(new Point(user, "리뷰 작성 - 글 작성", 1,
                    eventDTO.getType(), eventDTO.getReviewId(), eventDTO.getAction()));
            //User의 포인트에 (bonusPoint + 1)점을 추가한다.
            userRepository.save(user.addPoint(bonusPoint + 1));
            return;
        }
        //사진 첨부한 경우, 포인트 2점
        pointRepository.save(new Point(user, "리뷰 작성 - 글, 사진 작성", 2,
                eventDTO.getType(), eventDTO.getReviewId(), eventDTO.getAction()));
        //User의 포인트에 (bonusPoint + 2)점을 추가한다.
        userRepository.save(user.addPoint(bonusPoint + 2));
    }

    private int addBonusPoint(EventDTO eventDTO, User user) {
        //사용자 입장에서 본 첫 리뷰가 아닌 경우, return 0
        if(reviewRepository.existsByPlaceIdEqualsAndDeletedFalse(eventDTO.getPlaceId())){
            return 0;
        }
        //사용자 입장에서 본 첫 리뷰인 경우, 보너스 점수 1점
        pointRepository.save(new Point(user,"첫 리뷰", 1,
                eventDTO.getType(), eventDTO.getReviewId(), "BONUS"));
        return 1;
    }

    @Transactional
    public void modifyReviewPoint(EventDTO eventDTO, User user) {
        //보너스 점수를 제외한 해당 리뷰 포인트
        int reviewPoint = getPointSum(pointRepository.findByTypeEqualsAndRelatedIdEqualsAndActionNot(eventDTO.getType(), eventDTO.getReviewId(), "BONUS"));

        //포인트 변동이 없는 경우,
        if((reviewPoint == 1 && eventDTO.getAttachedPhotoIds().size() == 0) || (reviewPoint == 2 && eventDTO.getAttachedPhotoIds().size() != 0))
            return;

        //사진을 새롭게 올린 경우,
        if(reviewPoint == 1) {
            //포인트 1점
            pointRepository.save(new Point(user, "리뷰 수정 - 사진 추가", 1,
                    eventDTO.getType(), eventDTO.getReviewId(), eventDTO.getAction()));
            //User의 포인트에 1점을 추가한다.
            userRepository.save(user.addPoint(1));
            return;
        }
        //사진을 모두 지운 경우, 포인트 -1점
        pointRepository.save(new Point(user, "리뷰 수정 - 사진 삭제", -1,
                eventDTO.getType(), eventDTO.getReviewId(), eventDTO.getAction()));
        //User의 포인트에 -1점을 추가한다.
        userRepository.save(user.addPoint(-1));
    }

    @Transactional
    public void deleteReviewPoint(EventDTO eventDTO, User user) {
        //Type이 review이며 reviewId가 같은 point 리스트를 받아 해당 리뷰 포인트를 구한다
        int reviewPoint = getPointSum(pointRepository.findByTypeEqualsAndRelatedIdEquals(eventDTO.getType(), eventDTO.getReviewId()));
        //(현재까지 해당 리뷰를 통해 얻은 포인트) * -1 저장 = 해당 리뷰 포인트 = 0
        pointRepository.save(new Point(user, "리뷰 삭제", reviewPoint * -1,
                eventDTO.getType(), eventDTO.getReviewId(), eventDTO.getAction()));
        //User의 포인트에 (-1 * reviewPoint)점을 추가한다.
        userRepository.save(user.addPoint(reviewPoint * -1));
    }

    private int getPointSum(List<Point> pointList) {
        int totalPoint = 0;
        for(Point point : pointList)
            totalPoint += point.toDTO().getPoint();
        return totalPoint;
    }

    public List<PointDTO> getPersonalPointHistory(String userId) {
        //유저가 없는 경우, UserNotFoundException 발생
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException.class);

        //해당 유저의 point 기록을 DTO로 변환해 return
        return pointRepository.findByUserId(userId).stream().map(point -> point.toDTO()).collect(Collectors.toList());
    }
}
