package lt.arturas.spring.articles.models;

import lt.arturas.spring.articles.entities.CommentEntity;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.format.DateTimeFormatter;

@Data
public class Comment {
    private Long id;
    private String commentText;
    private Long userId;

    private Long postId;
    private String username;
    private java.sql.Timestamp commentPostDate;
    private String authorAvatarImageUrl;

    static Logger logger = LogManager.getLogger(Comment.class);

    public Comment() {

    }

    public Comment(CommentEntity commentEntity) {
        this.id = commentEntity.getId();
        this.commentText = commentEntity.getCommentText();
        this.userId = commentEntity.getUserEntity().getId();
        this.username = commentEntity.getUserEntity().getUsername();
        this.commentPostDate = commentEntity.getCommentPostDate();
        this.authorAvatarImageUrl = commentEntity.getUserEntity().getAvatarImgUrl();
    }

    public String getTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String format = formatter.format(commentPostDate.toLocalDateTime());
        logger.info(format);
        return formatter.format(commentPostDate.toLocalDateTime());
    }
}