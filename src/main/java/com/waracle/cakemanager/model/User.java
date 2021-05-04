package com.waracle.cakemanager.model;

import javax.persistence.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name="UIB_USER")
public class User implements UserDetails {

    private static final Logger LOGGER = LoggerFactory.getLogger(User.class);

    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "USERNAME", nullable = false, updatable = false, unique = true)
    private String username;

    @Column(name = "PASSWORD")
    //don't return the password in the JSON response
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @ManyToMany
    @JoinTable(
            name = "UIB_USER_ROLE",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"))
    @OrderBy("name ASC")
    private Set<Role> roles;

    public User(String firstName, String lastName, String userName, String password, String email, Boolean enabled) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = userName;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "active_ind")
    private boolean enabled;

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
        return enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        if (null == roles)
            return authorities;

        roles.forEach(role -> {
            if (null == role.getRoleValue()){
                LOGGER.error(String.format("Role '%s' has no role value associated with it!", role.getName()));
                return;
            }
            authorities.add(new SimpleGrantedAuthority(String.format("ROLE_%s", role.getRoleValue())));
        });

        return authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return  enabled == user.enabled &&
                Objects.equals(id, user.id) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email) &&
                Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, firstName, lastName, username, password, email, roles);
    }

    public void addRole(Role role) {

        if (this.roles == null) {
            this.roles = new HashSet<Role>();
        }
        this.roles.add(role);
        role.addAssociatedUser(this);
    }

    public Set<Role> getRoles() {
        if (this.roles == null)
            return new HashSet<>();
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
