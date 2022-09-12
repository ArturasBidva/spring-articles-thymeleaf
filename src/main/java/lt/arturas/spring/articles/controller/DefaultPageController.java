package lt.arturas.spring.articles.controller;

import lt.arturas.spring.articles.models.User;
import lt.arturas.spring.articles.services.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultPageController {
    private final PostService postService;

    public DefaultPageController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping()
    public String defaultHomePage(Model model, @AuthenticationPrincipal User user) {
        if (user == null) {
            user = new User();
        }
        model.addAttribute("user", user);
        model.addAttribute("posts", postService.getALlPosts());
        return "index";
    }
}