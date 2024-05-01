package com.javatechie.service.impl;

import com.javatechie.config.UserInfoUserDetails;
import com.javatechie.dto.CommentDto;
import com.javatechie.dto.ReviewDto;
import com.javatechie.dto.UserDto;
import com.javatechie.entity.CommentEntity;
import com.javatechie.entity.ItemEntity;
import com.javatechie.entity.ReviewEntity;
import com.javatechie.entity.User;
import com.javatechie.repository.CommentRepository;
import com.javatechie.repository.ItemRepository;
import com.javatechie.repository.ReviewRepository;
import com.javatechie.repository.UserInfoRepository;
import com.javatechie.service.IReviewService;
import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.xml.stream.events.Comment;
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
    private ItemRepository itemRepository;

    // có nên gộp đánh giá vào không tạo thành bảng mới
    @Override
    public JSONObject saveReview(ReviewDto reviewDto, Integer itemId) {
        JSONObject response = new JSONObject();
        try {
            // lấy ra người dùng đang đăng nhập
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserInfoUserDetails userInfo = (UserInfoUserDetails) auth.getPrincipal();
            User user = userInfoRepository.findByUsernameAndDeleted(userInfo.getUsername(), 0).orElse(new User());
            ItemEntity item = itemRepository.findByIdAndDeleted(itemId, 0).orElse(new ItemEntity());
            // kiểm tra xem người dùng này đã thực hiện đánh giá chưa
            if(reviewRepository.existsByItem_IdAndUser_Id(itemId, user.getId())) {
                response.put("code", 0);
                response.put("message", "User has been reviewed");
                return response;
            }
            ReviewEntity review = new ReviewEntity(System.currentTimeMillis(), reviewDto.getRanking(), new Date(System.currentTimeMillis()), item, user);
            reviewRepository.save(review);
            response.put("code", 1);
            response.put("message", "Review success");
        }
        catch(Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Review fail");
        }
        return response;
    }

    @Override
    public JSONObject saveComment(CommentDto commentDto, Integer itemId) {
        JSONObject response = new JSONObject();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserInfoUserDetails userDetails = (UserInfoUserDetails) auth.getPrincipal();
            User user = userInfoRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(new User());
            ItemEntity item = itemRepository.findByIdAndDeleted(itemId, 0).orElse(new ItemEntity());
            CommentEntity comment = new CommentEntity(System.currentTimeMillis(), new Date(System.currentTimeMillis()), null, commentDto.getContent(), user, item);
            commentRepository.save(comment);
            response.put("code", 1);
            response.put("message", "Add new comment success");
//            response.put("comment", commentDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Add new comment fail");
        }
        return response;
    }

    @Override
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
                response.put("message", "Access denied");
                return response;
            }
            comment.setModifiledDate(new Date(System.currentTimeMillis()));
            comment.setContent(commentDto.getContent());
            commentRepository.save(comment);
            response.put("code", 1);
            response.put("message", "Update comment success");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Update comment fail");
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

    @Override
    public List<CommentDto> findAllComment(Integer itemId) {
        try {
            List<CommentEntity> listComment = commentRepository.findAllByItem_Id(itemId);
            List<CommentDto> responses = new ArrayList<>();
            for(CommentEntity comment : listComment) {
                CommentDto commentDto = new CommentDto();
                UserDto userDto = new UserDto();
                BeanUtils.copyProperties(comment.getUser(), userDto);
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

    @Override
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
            response.put("message", "Delete comment success");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 1);
            response.put("message", "Delete comment fail");
        }
        return response;
    }
}
