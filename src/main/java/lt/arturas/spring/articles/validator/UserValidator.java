package lt.arturas.spring.articles.validator;

import lt.arturas.spring.articles.requests.CreateUserRequest;
import lt.arturas.spring.articles.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class UserValidator {
    UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    public void validateUser(CreateUserRequest request, BindingResult bindingResult) {
        if (request.getName().length() < 5 || request.getName().length() > 30) {
            bindingResult.rejectValue("name", "", " invalid name size: Size must be between 5 and 30");
            return;
        }
        if (request.getUsername().length() < 5 || request.getUsername().length() > 30) {
            bindingResult.rejectValue("username", "", " invalid username size: Size must be between 5 and 30");
            return;
        }
        if (request.getPassword().length() < 5 || request.getPassword().length() > 30) {
            bindingResult.rejectValue("password", "", "invalid password size: Size must be between 5 and 30");
            return;
        }
        if (!request.getPassword().equals(request.getRepeatPassword())) {
            bindingResult.rejectValue("password", "", "Passwords must match");
            return;
        }
        if (userService.checkIfUserExist(request.getUsername())) {
            bindingResult.rejectValue("username", "", "User with this username already exist");
        }
    }
}