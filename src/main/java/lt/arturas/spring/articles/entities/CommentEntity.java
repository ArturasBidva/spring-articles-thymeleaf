package lt.arturas.spring.articles.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "COMMENTS")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String commentText;
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity userEntity;

    private java.sql.Timestamp commentPostDate;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    PostEntity postEntity;
    public CommentEntity (){

    }
}