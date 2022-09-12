package com.example.demo.models;

import com.example.demo.entities.PostEntity;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Post {
    private Long id;
    private String title;
    private java.sql.Timestamp createdOn;
    private java.sql.Timestamp updatedOn;
    private String content;
    private Long userId;
    private String authorName;
    private String imageUrl;
    private List<Comment> comments;

    public Post(PostEntity postEntity) {
        this.id = postEntity.getId();
        this.title = postEntity.getTitle();
        this.createdOn = postEntity.getCreatedOn();
        this.updatedOn = postEntity.getUpdatedOn();
        this.content = postEntity.getContent();
        this.userId = postEntity.getUserEntity().getId();
        this.comments = postEntity.getCommentEntities().stream().map(Comment::new).collect(Collectors.toList());
        this.imageUrl = postEntity.getImageUrl();
        this.authorName = postEntity.getUserEntity().getName();
    }

    public Post() {
    }

    public String getTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return formatter.format(createdOn.toLocalDateTime());
    }

    public String getUpdatedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return formatter.format(updatedOn.toLocalDateTime());
    }
}