package com.mysite.sbb;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    @Transactional
    @Modifying // 아래 Query가 SELECT가 아니라면 붙여야 한다
    // nativeQuery = true여야 MySQL Query 문법 사용가능
    @Query(value = "ALTER TABLE answer AUTO_INCREMENT = 1", nativeQuery = true)
    void clearAutoIncrement();
}
