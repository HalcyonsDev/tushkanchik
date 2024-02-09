package com.halcyon.udemy.repository;

import com.halcyon.udemy.model.ContentOrder;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentOrderRepository extends JpaRepository<ContentOrder, Long> {
    @Transactional
    @Query("SELECT COALESCE(MAX(co.order), 0) FROM ContentOrder co WHERE co.pageId = ?1")
    Integer findLastOrderForPage(Long pageId);

    @Transactional
    @Query("SELECT co FROM ContentOrder co WHERE co.pageId = ?1 AND co.order > ?2")
    List<ContentOrder> findAllByPageIdAndOrder(Long pageId, Integer order);

    @Transactional
    @Query("SELECT co FROM ContentOrder co WHERE co.pageId = ?1 AND ?2 <= co.order AND co.order <= ?3")
    List<ContentOrder> findAllByPageIdInInterval(Long pageId, Integer minOrder, Integer maxOrder);
}
