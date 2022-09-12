package lt.arturas.spring.articles.models;

import lombok.Data;

@Data
public class EditUserRoleForm {
    private Long userId;
    private Long roleId;

    public EditUserRoleForm(Long id) {
        this.roleId = id;
    }

    public EditUserRoleForm() {

    }
}