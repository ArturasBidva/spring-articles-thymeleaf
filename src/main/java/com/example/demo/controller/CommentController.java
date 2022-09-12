package com.example.demo.controller;

import com.example.demo.models.Comment;
import com.example.demo.models.User;
import com.example.demo.services.CommentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/private/comment/addComment")
    String addComment(
            @ModelAttribute("comment") Comment comment,
            @AuthenticationPrincipal User user
    ) {
        comment.setUserId(user.getId());
        commentService.createComment(comment);
        return "redirect:/post/details/" + comment.getPostId();
    }

    @DeleteMapping("/private/comment/removeComment/{id}/{postId}")
    String removeCommentById(@PathVariable Long id, @PathVariable Long postId) {
        commentService.deleteComment(id);
        return "redirect:/post/details/" + postId;
    }
}