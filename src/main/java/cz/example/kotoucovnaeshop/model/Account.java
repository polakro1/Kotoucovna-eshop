package cz.example.kotoucovnaeshop.model;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;

import java.util.HashSet;
import java.util.Set;

@GroupSequence({Account.WithoutPassword.class, Account.class})
public abstract class Account {
    public interface WithoutPassword extends Default {};
    private Long id;
    @NotBlank(groups = WithoutPassword.class)
    @Email(regexp = "^(\\S+)\\@(\\S+)\\.(\\S+)$", groups = WithoutPassword.class)
    private String email;
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "Heslo musí obsahovat min. 8 znaků a alespoň jedno číslo a speciální znak")
    private String password;
    @Size(min = 6, max = 30, groups = WithoutPassword.class)
    private String username;
    @NotBlank(groups = WithoutPassword.class)
    private String name;
    @NotBlank(groups = WithoutPassword.class)
    private String surname;

    private Set<String> roles = new HashSet<>();

    public Account() {
    }

    public Account(long id, String email, String password, String username, String name, String surname) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.name = name;
        this.surname = surname;
    }
/*
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        roles.forEach(role -> authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return role;
            }
        }));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

 */

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public void addRole(String role) {
        roles.add(role);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
