package com.halcyon.udemy.controller;

import com.halcyon.udemy.dto.NewPageDto;
import com.halcyon.udemy.model.Page;
import com.halcyon.udemy.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pages")
@RequiredArgsConstructor
public class PageController {
    private final PageService pageService;

    @PostMapping
    public ResponseEntity<Page> create(@RequestBody NewPageDto dto) {
        Page page = pageService.create(dto);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{pageId}")
    public ResponseEntity<Page> getPage(@PathVariable Long pageId) {
        Page page = pageService.getPage(pageId);
        return ResponseEntity.ok(page);
    }
}
