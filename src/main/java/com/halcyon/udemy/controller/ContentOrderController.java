package com.halcyon.udemy.controller;

import com.halcyon.udemy.model.ContentOrder;
import com.halcyon.udemy.service.ContentOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/content-orders")
@RequiredArgsConstructor
public class ContentOrderController {
    private final ContentOrderService contentOrderService;

    @GetMapping("/{contentOrderId}")
    public ResponseEntity<ContentOrder> getById(@PathVariable Long contentOrderId) {
        return ResponseEntity.ok(contentOrderService.findById(contentOrderId));
    }
}
