package org.example.lab5.security;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.lab5.Entity.Task;
import org.example.lab5.Entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String name;
    private String login;
    private String password;
    private final LocalDate date = LocalDate.now();
    private String email;
    private List<Task> tasks;
    private String image_url;
    private  Collection<? extends GrantedAuthority> Authorities;

    public UserDetailsImpl(Collection<? extends GrantedAuthority> authorities,
                           List<Task> tasks, String password,
                           String login, String name, Long id, String email,
                           String image_url) {
        this.Authorities = authorities;
        this.tasks = tasks;
        this.password = password;
        this.login = login;
        this.name = name;
        this.id = id;
        this.image_url = image_url;
        this.email = email;
    }
    public static UserDetailsImpl build(User user){
        List<GrantedAuthority> authorityList = List.of(new SimpleGrantedAuthority(user.getRole().name()));
        return new UserDetailsImpl(
                authorityList,
                user.getTasks(),
                user.getPassword(),
                user.getLogin(),
                user.getName(),
                user.getId(),
                user.getEmail(),
                user.getImage_url()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
