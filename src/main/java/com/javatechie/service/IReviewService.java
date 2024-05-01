package com.javatechie.service;

import com.javatechie.dto.CommentDto;
import com.javatechie.dto.ReviewDto;
import org.json.simple.JSONObject;

import java.util.List;

public interface IReviewService {

    JSONObject saveReview(ReviewDto reviewDto, Integer itemId);
    JSONObject saveComment(CommentDto commentDto, Integer itemId);
    JSONObject updateComment(CommentDto commentDto);
    ReviewDto findOne(Long id); // đang phân vân có dùng đến không
    CommentDto findOneComment(Long id); // đang phân vân có dùng đến không
    List<CommentDto> findAllComment(Integer itemId);
    JSONObject deleteComment(Long id);
}
