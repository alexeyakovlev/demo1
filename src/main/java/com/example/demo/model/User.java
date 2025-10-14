package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Represents a user in the application")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique ID of the user", example = "1", required = true)
    private Long id;

    @Schema(description = "Username of the user for login",
            example = "John",
            required = true)
    @Column(nullable = false)
    private String username;

    @Schema(description = "Password of the user for login",
            example = "sGfkjs234",
            required = true)
    @Column(nullable = false)
    private String password;

    @Schema(description = "Email address of the user",
            example = "john@example.com",
            required = true)
    @Column(nullable = false, unique = true)
    private String email;

    @Schema(description = "Role of user (ADMIN or USER)")
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;

    @Schema(description = "List cards of user")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Card> cards = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority(getRole().name())
        );
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
