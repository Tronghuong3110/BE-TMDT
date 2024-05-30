package com.javatechie.controller;

import com.javatechie.dto.CommentDto;
import com.javatechie.dto.ReviewDto;
import com.javatechie.service.IReviewService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class ReviewController {

    @Autowired
    private IReviewService reviewService;

    @PostMapping("/api/review") // ok
    public ResponseEntity<?> saveReview(@RequestBody ReviewDto review, @RequestParam("itemId") Long itemId) {
        JSONObject response = reviewService.saveReview(review, itemId);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/comment") // ok
    public ResponseEntity<?> saveComment(@RequestBody CommentDto comment, @RequestParam("itemId") Long itemId) {
        JSONObject response = reviewService.saveComment(comment, itemId);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/comment") // ok
    public ResponseEntity<?> updateComment(@RequestBody CommentDto comment) {
        JSONObject response = reviewService.updateComment(comment);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/comments") /// ok
    public ResponseEntity<?> finAllComment(@RequestParam("itemId") Long itemId) {
        List<CommentDto> responses = reviewService.findAllComment(itemId);
        if(responses == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/api/comment") // ok
    public ResponseEntity<?> deleteComment(@RequestParam("id") Long id) {
        JSONObject response = reviewService.deleteComment(id);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
