package lt.arturas.spring.articles.requests;

import lombok.Data;

@Data
public class CreateUserRequest {
   private String name;
   private String username;
   private String password;
   private String repeatPassword;
}