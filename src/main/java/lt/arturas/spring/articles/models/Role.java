package lt.arturas.spring.articles.models;

import lt.arturas.spring.articles.entities.RoleEntity;
import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority {
    private Long id;
    private final String roleName;

    public Role(RoleEntity roleEntity) {
        this.id = roleEntity.getId();
        this.roleName = roleEntity.getRoleName();
    }

    public Long getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

    public Role(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String getAuthority() {
        return roleName;
    }

    @Override
    public String toString() {
        return id + roleName;
    }
}