package com.halcyon.udemy.controller;

import com.halcyon.udemy.model.ContentOrder;
import com.halcyon.udemy.model.Image;
import com.halcyon.udemy.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/upload/{pageId}")
    public ResponseEntity<Image> create(@PathVariable Long pageId, @RequestParam("file") MultipartFile file) {
        Image image = imageService.create(file, pageId);
        return ResponseEntity.ok(image);
    }

    @PostMapping("/upload/{pageId}/order")
    public ResponseEntity<Image> createByOrder(
            @PathVariable Long pageId,
            @RequestParam Integer value,
            @RequestParam("file") MultipartFile file
    ) {
        Image image = imageService.create(file, pageId, value);
        return ResponseEntity.ok(image);
    }

    @PatchMapping("/{imageId}/change/order")
    public ResponseEntity<ContentOrder> changeOrder(@PathVariable Long imageId, @RequestParam Integer order) {
        ContentOrder contentOrder = imageService.changeOrderById(imageId, order);
        return ResponseEntity.ok(contentOrder);
    }

    @DeleteMapping("/{imageId}/delete")
    public ResponseEntity<String> delete(@PathVariable Long imageId) {
        String response = imageService.deleteById(imageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<Image> getById(@PathVariable Long imageId) {
        return ResponseEntity.ok(imageService.findById(imageId));
    }

    @GetMapping("/{pageId}")
    public ResponseEntity<List<Image>> getAllByPage(@PathVariable Long pageId) {
        List<Image> images = imageService.findAllByPageId(pageId);
        return ResponseEntity.ok(images);
    }

}
