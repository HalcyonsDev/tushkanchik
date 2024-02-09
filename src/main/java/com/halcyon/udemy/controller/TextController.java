package com.halcyon.udemy.controller;

import com.halcyon.udemy.dto.NewTextDto;
import com.halcyon.udemy.model.Text;
import com.halcyon.udemy.service.TextService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/texts")
@RequiredArgsConstructor
public class TextController {
    private final TextService textService;

    @PostMapping
    public ResponseEntity<Text> create(@RequestBody NewTextDto dto) {
        Text text = textService.create(dto);
        return ResponseEntity.ok(text);
    }

    @PostMapping("/order")
    public ResponseEntity<Text> createByOrder(@RequestBody NewTextDto dto, @RequestParam Integer order) {
        Text text = textService.create(dto, order);
        return ResponseEntity.ok(text);
    }

    @GetMapping("{textId}")
    public ResponseEntity<Text> getById(@PathVariable Long textId) {
        Text text = textService.findById(textId);
        return ResponseEntity.ok(text);
    }

    @DeleteMapping("/{textId}/delete")
    public ResponseEntity<String> delete(@PathVariable Long textId) {
        String response = textService.deleteById(textId);
        return ResponseEntity.ok(response);
    }
}
