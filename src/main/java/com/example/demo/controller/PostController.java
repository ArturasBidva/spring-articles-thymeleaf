package com.example.demo.controller;

import com.example.demo.entities.PostEntity;
import com.example.demo.models.Comment;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.services.PostService;
import com.example.demo.validator.PostValidator;
import com.example.demo.validator.UrlValidator;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class PostController {
    private final PostService postService;
    private final UrlValidator urlValidator;
    private final PostValidator postValidator;

    public PostController(PostService postService, UrlValidator urlValidator, PostValidator postValidator) {
        this.postService = postService;
        this.urlValidator = urlValidator;
        this.postValidator = postValidator;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_AUTHOR')")
    @PostMapping("/private/post/createpostform")
    public String createPost(
            @ModelAttribute @Valid Post post,
            BindingResult bindingResult,
            Model model,
            @AuthenticationPrincipal User user
    ) {
        urlValidator.checkIfImageUrlValidPost(post, bindingResult);
        postValidator.contentValidator(post, bindingResult);
        model.addAttribute("post", post);
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "postcreate";
        }
        post.setUserId(user.getId());
        Long newPostId = postService.createPost(post);
        return "redirect:/post/details/" + newPostId;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_AUTHOR')")
    @GetMapping("/private/post/createpostform")
    public String createPost(
            Post post,
            Model model,
            @AuthenticationPrincipal User user
    ) {
        model.addAttribute("user", user);
        model.addAttribute("post", post);
        return "postcreate";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_AUTHOR')")
    @DeleteMapping("/private/post/remove/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_AUTHOR')")
    @PostMapping("/private/post/update")
    public String updatePost(
            @ModelAttribute Post post,
            @AuthenticationPrincipal User user,
            BindingResult bindingResult,
            Model model
    ) {
        urlValidator.checkIfImageUrlValidPost(post, bindingResult);
        postValidator.contentValidator(post, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "postedit";
        }
        post.setId(post.getId());
        postService.updatePost(post);
        return "redirect:/";
    }

    @GetMapping("/")
    public String getAllPosts(
            @RequestParam(defaultValue = "0") int pageNumber,
            Model model,
            @AuthenticationPrincipal User user
    ) {
        if (user == null) {
            user = new User();
        }
        Page<Post> posts = postService.getAllArticlesPaginated(pageNumber);
        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("hasNextPage", posts.hasNext());
        return "index";
    }

    @GetMapping("/post/details/{id}")
    public String getPostById(
            @PathVariable Long id,
            Model model,
            @AuthenticationPrincipal User user
    ) {
        Comment comment = new Comment();
        comment.setPostId(id);
        if (user == null) {
            user = new User();
            user.setId(-1L);
        }
        Post post = new Post(postService.getPostById(id));
        model.addAttribute("user", user);
        model.addAttribute("post", post);
        model.addAttribute("comment", comment);
        return "postdetails";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_AUTHOR')")
    @GetMapping("/private/post/edit/{id}")
    public String editArticle(
            @PathVariable Long id,
            Model model,
            @AuthenticationPrincipal User user
    ) {
        PostEntity postById = postService.getPostById(id);
        model.addAttribute("user", user);
        model.addAttribute("post", postById);
        return "postedit";
    }
}