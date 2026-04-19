package com.rakesh.task_manager.service.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rakesh.task_manager.entity.Users;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@Data
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID=1L;

    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private List<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id,String username, String password, String email,
                           List< ? extends GrantedAuthority> authorities){
        this.id=id;
        this.username=username;
        this.password=password;
        this.email=email;
        this.authorities=authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public UserDetailsImpl build(Users user){
        authorities = List.of(new SimpleGrantedAuthority(user.getRole()));
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                authorities
        );

    }
}
