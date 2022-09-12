package lt.arturas.spring.articles.requests;

import lombok.Data;

@Data
public class CreateUserRequest {
    String name;
    String username;
    String password;
    String repeatPassword;
}