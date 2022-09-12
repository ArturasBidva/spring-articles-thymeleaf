package lt.arturas.spring.articles.validator;

import lt.arturas.spring.articles.models.Post;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class PostValidator {

    public void contentValidator(Post post, BindingResult bindingResult) {
        if (post.getContent().length() < 10) {
            bindingResult.rejectValue("content", "", "Content size must be between 10 and 300");
        }
        if (post.getTitle().length() < 10) {
            bindingResult.rejectValue("title", "", "Title size must be between 10 and 30");
        }
    }
}