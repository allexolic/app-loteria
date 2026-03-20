package com.allexolic.app_loteria.repositories;

import com.allexolic.app_loteria.entities.LotteryFileDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LotteryFileDetailsRepository extends JpaRepository<LotteryFileDetail, Long> {
}
