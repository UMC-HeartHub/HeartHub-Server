package com.umc_spring.Heart_Hub.board.model.mission;

import com.umc_spring.Heart_Hub.constant.entity.BaseEntity;
import com.umc_spring.Heart_Hub.user.model.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class UserMissionStatus extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userMissionStatusId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Mission mission;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // 0(Fail), 1(Success) , 미션의 성공 상태정보
    private String checkStatus;

    public UserMissionStatus(Mission mission, User user) {
        this.mission = mission;
        this.user = user;
        this.checkStatus = "0";
    }

}
