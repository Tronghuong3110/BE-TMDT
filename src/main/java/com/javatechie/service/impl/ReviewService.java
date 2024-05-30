package com.javatechie.service.impl;

import com.javatechie.config.UserInfoUserDetails;
import com.javatechie.dto.CommentDto;
import com.javatechie.dto.ReviewDto;
import com.javatechie.dto.UserDto;
import com.javatechie.entity.CommentEntity;
import com.javatechie.entity.ProductEntity;
import com.javatechie.entity.ReviewEntity;
import com.javatechie.entity.User;
import com.javatechie.repository.CommentRepository;
import com.javatechie.repository.ProductRepository;
import com.javatechie.repository.ReviewRepository;
import com.javatechie.repository.UserInfoRepository;
import com.javatechie.service.IReviewService;
import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService implements IReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ProductRepository productRepository;

    // có nên gộp đánh giá vào không tạo thành bảng mới
    @Override // ok
    public JSONObject saveReview(ReviewDto reviewDto, Long productId) {
        JSONObject response = new JSONObject();
        try {
            // lấy ra người dùng đang đăng nhập
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserInfoUserDetails userInfo = (UserInfoUserDetails) auth.getPrincipal();
            User user = userInfoRepository.findByUsernameAndDeleted(userInfo.getUsername(), 0).orElse(new User());
//            ItemEntity item = itemRepository.findByIdAndDeleted(itemId, 0).orElse(new ItemEntity());
            ProductEntity product = productRepository.findByIdAndDeleted(productId, false).orElse(new ProductEntity());
            // kiểm tra xem người dùng này đã thực hiện đánh giá chưa
            if(reviewRepository.existsByProduct_IdAndUser_Id(productId, user.getId())) {
                response.put("code", 0);
                response.put("message", "Bạn đã hết lượt ánh giá cho sản phẩm này !!");
                return response;
            }
            ReviewEntity review = new ReviewEntity(System.currentTimeMillis(), reviewDto.getRanking(), new Date(System.currentTimeMillis()), product, user);
            review = reviewRepository.save(review);
            BeanUtils.copyProperties(review, reviewDto);
            response.put("code", 1);
            response.put("message", "Thêm mới đánh giá thành công !!");
            response.put("review", reviewDto);
        }
        catch(Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Thêm mới đánh giá thất bại !!");
        }
        return response;
    }
    @Override // ok
    public JSONObject saveComment(CommentDto commentDto, Long productId) {
        JSONObject response = new JSONObject();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserInfoUserDetails userDetails = (UserInfoUserDetails) auth.getPrincipal();
            User user = userInfoRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(new User());
//            ItemEntity item = itemRepository.findByIdAndDeleted(itemId, 0).orElse(new ItemEntity());
            ProductEntity product = productRepository.findByIdAndDeleted(productId, false).orElse(new ProductEntity());
            CommentEntity comment = new CommentEntity(System.currentTimeMillis(), new Date(System.currentTimeMillis()), null, commentDto.getContent(), user, product);
            comment = commentRepository.save(comment);
            BeanUtils.copyProperties(comment, commentDto);
            response.put("code", 1);
            response.put("message", "Thêm mới bình luận thành công !!");
            response.put("comment", commentDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Thêm mới bình luận thất bại !!");
        }
        return response;
    }
    @Override // ok
    public JSONObject updateComment(CommentDto commentDto) {
        JSONObject response = new JSONObject();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserInfoUserDetails userDetails = (UserInfoUserDetails) auth.getPrincipal();
            User user = userInfoRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(new User());
            CommentEntity comment = commentRepository.findById(commentDto.getId()).orElse(new CommentEntity());
            // TH chỉnh sửa comment không phải của mình
            if(!user.getId().equals(comment.getUser().getId())) {
                response.put("code", 0);
                response.put("message", "Không có quyền chỉnh sửa !!");
                return response;
            }
            comment.setModifiledDate(new Date(System.currentTimeMillis()));
            comment.setContent(commentDto.getContent());
            comment = commentRepository.save(comment);
            BeanUtils.copyProperties(comment, commentDto);
            response.put("code", 1);
            response.put("message", "Cập nhật bình luận thành công !!");
            response.put("comment", commentDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Cập nhật bình luận thất bại !!");
        }
        return response;
    }
    @Override
    public ReviewDto findOne(Long id) {
        try {

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public CommentDto findOneComment(Long id) {
        return null;
    }
    @Override // ok
    public List<CommentDto> findAllComment(Long productId) {
        try {
            List<CommentEntity> listComment = commentRepository.findAllByProduct_Id(productId);
            List<CommentDto> responses = new ArrayList<>();
            for(CommentEntity comment : listComment) {
                CommentDto commentDto = new CommentDto();
                UserDto userDto = new UserDto();
                userDto.setName(comment.getUser().getName());
                userDto.setAvatarPath(comment.getUser().getAvatarPath());
                BeanUtils.copyProperties(comment, commentDto);
                commentDto.setUser(userDto);
                responses.add(commentDto);
            }
            return responses;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override // ok
    public JSONObject deleteComment(Long id) {
        JSONObject response = new JSONObject();
        try {
            UserInfoUserDetails userDetails = (UserInfoUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userInfoRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(new User());
            CommentEntity comment = commentRepository.findById(id).orElse(new CommentEntity());
            if(!user.getId().equals(comment.getUser().getId())) {
                response.put("code", 0);
                response.put("message", "Access denied");
                return response;
            }
            commentRepository.deleteById(id);
            response.put("code", 1);
            response.put("message", "Xóa bình luận thành công !!");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Xóa bình luận thất bại !!");
        }
        return response;
    }
}
