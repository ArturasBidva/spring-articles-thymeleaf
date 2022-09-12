package lt.arturas.spring.articles.services;

import lt.arturas.spring.articles.entities.CommentEntity;
import lt.arturas.spring.articles.entities.PostEntity;
import lt.arturas.spring.articles.entities.UserEntity;
import lt.arturas.spring.articles.models.Comment;
import lt.arturas.spring.articles.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class CommentService {
    CommentRepository commentRepository;
    PostService postService;

    public CommentService(CommentRepository commentRepository, PostService postService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
    }

    public void createComment(Comment comment) {
        long now = System.currentTimeMillis();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(comment.getUserId());
        PostEntity postEntity = new PostEntity();
        postEntity.setId(comment.getPostId());
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setCommentText(comment.getCommentText());
        commentEntity.setPostEntity(postEntity);
        commentEntity.setUserEntity(userEntity);
        commentEntity.setCommentPostDate(new Timestamp(now));
        commentRepository.save(commentEntity);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}