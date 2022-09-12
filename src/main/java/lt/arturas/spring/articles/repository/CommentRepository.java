package lt.arturas.spring.articles.repository;

import lt.arturas.spring.articles.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
}