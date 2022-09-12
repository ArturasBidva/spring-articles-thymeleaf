package com.example.demo.entities;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "POSTS")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private java.sql.Timestamp createdOn;

    private java.sql.Timestamp updatedOn;

    @Column(length = 500)
    private String content;

    private String imageUrl;
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity userEntity;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "postEntity")
    List<CommentEntity> commentEntities;

    public PostEntity() {
    }
}