package lt.arturas.spring.articles.controller;

import lt.arturas.spring.articles.entities.UserEntity;
import lt.arturas.spring.articles.models.EditUserRoleForm;
import lt.arturas.spring.articles.models.User;
import lt.arturas.spring.articles.requests.CreateUserRequest;
import lt.arturas.spring.articles.services.UserService;
import lt.arturas.spring.articles.validator.UrlValidator;
import lt.arturas.spring.articles.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    private final UserService userService;

    private final UserValidator userValidator;

    private final UrlValidator urlValidator;

    @Autowired
    public UserController(
            UserService userService,
            UserValidator userValidator,
            UrlValidator urlValidator
    ) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.urlValidator = urlValidator;
    }

    @PostMapping("/register")
    String createUser(
            @ModelAttribute CreateUserRequest createUserRequest,
            BindingResult bindingResult
    ) {
        userValidator.validateUser(createUserRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            return "register";
        }
        userService.createUser(createUserRequest);
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("createUserRequest", new CreateUserRequest());
        model.addAttribute("user", new User());
        return "register";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/user/delete/{id}")
    String deleteUserById(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/private/admin-panel";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/user/{id}")
    String getUserById(
            @PathVariable Long id,
            Model model,
            @AuthenticationPrincipal User authUser
    ) {
        User user = new User(userService.getUserById(id));
        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("user", user);
        model.addAttribute("authUser", authUser);
        return "profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    String getUserByUsername(Model model, @AuthenticationPrincipal User authUser) {
        User user = new User(userService.getUserById(authUser.getId()));
        model.addAttribute("user", user);
        return "profile";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/private/admin-panel")
    String adminPanel(Model model, @AuthenticationPrincipal User authUser) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("authUser", authUser);
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("roles", userService.getAllRoles());
        model.addAttribute("editUserForm", new EditUserRoleForm(user.getId()));
        return "adminpanel";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/private/admin-panel")
    String adminPanelUserDetails(
            User user,
            Model model,
            @AuthenticationPrincipal User authUser
    ) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("authUser", authUser);
        User userDetailss = new User(userService.getUserById(user.getId()));
        model.addAttribute("user", userDetailss);
        model.addAttribute("roles", userService.getAllRoles());
        EditUserRoleForm editUserRoleForm = new EditUserRoleForm();
        editUserRoleForm.setUserId(user.getId());
        model.addAttribute("editUserForm", editUserRoleForm);
        return "adminpanel";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile/edit")
    String getUserEditForm(@AuthenticationPrincipal User authUser, Model model) {
        model.addAttribute("user", authUser);
        return "profileedit";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profile/edit")
    String editUser(
            @ModelAttribute User user,
            @AuthenticationPrincipal User authUser,
            BindingResult result
    ) {
        urlValidator.checkIfImageUrlValidUser(user, result);
        if (result.hasErrors()) {
            return "profileedit";
        }
        if (!user.getId().equals(authUser.getId())) {
            return "redirect:/profile";
        }
        userService.editUser(user);
        return "redirect:/profile";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/private/addrole")
    String changeUserRole(
            @ModelAttribute("editUserForm") EditUserRoleForm editUserRoleForm,
            @AuthenticationPrincipal User authUser
    ) {
        UserEntity user = userService.getUserById(editUserRoleForm.getUserId());
        if (user.getRoles().stream().anyMatch(it -> it.getId().equals(editUserRoleForm.getRoleId()))) {
            return "redirect:/private/admin-panel";
        }
        userService.addUserRole(editUserRoleForm.getUserId(), editUserRoleForm.getRoleId());
        return "redirect:/private/admin-panel";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/private/removerole")
    String deleteUserRole(
            @ModelAttribute("editUserForm") EditUserRoleForm editUserRoleForm,
            @AuthenticationPrincipal User authUser
    ) {
        UserEntity user = userService.getUserById(editUserRoleForm.getUserId());
        if (user.getRoles().stream().anyMatch(it -> it.getId().equals(editUserRoleForm.getRoleId()))) {
            userService.removeRole(editUserRoleForm.getUserId(), editUserRoleForm.getRoleId());
            return "redirect:/private/admin-panel";
        }
        return "redirect:/private/admin-panel";
    }
}