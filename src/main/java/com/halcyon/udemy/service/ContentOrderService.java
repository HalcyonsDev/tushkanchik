package com.halcyon.udemy.service;

import com.halcyon.udemy.model.ContentOrder;
import com.halcyon.udemy.repository.ContentOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentOrderService {
    private final ContentOrderRepository contentOrderRepository;

    public ContentOrder save(ContentOrder contentOrder) {
        return contentOrderRepository.save(contentOrder);
    }

    public List<ContentOrder> saveAll(List<ContentOrder> contentOrders) {
        return contentOrderRepository.saveAll(contentOrders);
    }

    public void deleteById(Long contentOrderId) {
        contentOrderRepository.deleteById(contentOrderId);
    }

    public ContentOrder findById(Long contentOrderId) {
        return contentOrderRepository.findById(contentOrderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Content order with this id not found."));
    }

    public Integer getLastOrderForPage(Long pageId) {
        return contentOrderRepository.findLastOrderForPage(pageId);
    }

    public List<ContentOrder> findAllByPageIdAndOrder(Long pageId, Integer order) {
        return contentOrderRepository.findAllByPageIdAndOrder(pageId, order);
    }

    public List<ContentOrder> findAllByPageIdInInterval(Long pageId, Integer minOrder, Integer maxOrder) {
        return contentOrderRepository.findAllByPageIdInInterval(pageId, minOrder, maxOrder);
    }
}
