package com.umc_spring.Heart_Hub.board.repository.mission;

import com.umc_spring.Heart_Hub.config.mission.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Long> {
}
