package lt.arturas.spring.articles.validator;

import lt.arturas.spring.articles.models.Post;
import lt.arturas.spring.articles.models.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UrlValidator {
    public void checkIfImageUrlValidUser(User user, BindingResult bindingResult) {
        String pattern = "(https?:\\/\\/.*\\.(?:png|jpg))";
        Matcher m = Pattern.compile(pattern).matcher(user.getAvatarImgUrl());
        if (!m.matches()) {
            bindingResult.rejectValue("avatarImgUrl", "", "invalid picture url : picture url must end with [jpg,gif,png] ");
        }
    }

    public void checkIfImageUrlValidPost(Post post, BindingResult bindingResult) {
        String pattern = "(https?:\\/\\/.*\\.(?:png|jpg))";
        Matcher m = Pattern.compile(pattern).matcher(post.getImageUrl());
        if (!m.matches()) {
            bindingResult.rejectValue("imageUrl", "", "invalid picture url : picture url must end with [jpg,gif,png] ");
        }
    }
}