package com.example.demo.services;

import com.example.demo.entities.PostEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.models.Post;
import com.example.demo.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Long createPost(Post post) {
        long now = System.currentTimeMillis();
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(post.getTitle());
        postEntity.setCreatedOn(new Timestamp(now));
        postEntity.setUpdatedOn(new Timestamp(now));
        postEntity.setImageUrl(post.getImageUrl());
        postEntity.setContent(post.getContent());
        UserEntity userEntity = new UserEntity();
        userEntity.setId(post.getUserId());
        postEntity.setId(post.getId());
        postEntity.setUserEntity(userEntity);
        PostEntity postEntity1 = postRepository.save(postEntity);
        return postEntity1.getId();
    }

    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    public void updatePost(Post post) {
        PostEntity referenceById = postRepository.getReferenceById(post.getId());
        referenceById.setTitle(post.getTitle());
        referenceById.setContent(post.getContent());
        referenceById.setImageUrl(post.getImageUrl());
        referenceById.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
        postRepository.save(referenceById);
    }

    public PostEntity getPostById(Long id) {
        return postRepository.getReferenceById(id);
    }

    public List<Post> getALlPosts() {
        return postRepository.findAll()
                .stream()
                .map(Post::new)
                .collect(Collectors.toList());
    }

    public Page<Post> getAllArticlesPaginated(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 2);
        return postRepository.findAll(pageable).map(Post::new);
    }
}