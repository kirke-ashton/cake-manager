package com.waracle.cakemanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="UIB_ROLE")
public class Role implements Serializable {

    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, updatable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "role_value")
    private String roleValue;

    @Column(name = "active_ind")
    private boolean activeInd;

    @ManyToMany(mappedBy = "roles")
    @OrderBy("username ASC")
    @JsonIgnore
    private List<User> associatedUsers;

    public Role()
    {
    }

    public Role(String name, String description, String roleValue) {
        this.activeInd = true;
        this.name = name;
        this.description = description;
        this.roleValue = roleValue;
        this.activeInd = activeInd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoleValue() {
        return roleValue;
    }

    public void setRoleValue(String roleValue) {
        this.roleValue = roleValue;
    }

    public boolean isActiveInd() {
        return activeInd;
    }

    public void setActiveInd(boolean activeInd) {
        this.activeInd = activeInd;
    }

    public List<User> getAssociatedUsers() {
        return associatedUsers;
    }

    public void setAssociatedUsers(List<User> associatedUsers) {
        this.associatedUsers = associatedUsers;
    }

    public void addAssociatedUser(User user) {
        if (this.associatedUsers == null)
            this.associatedUsers = new ArrayList<>();

        if (null != user)
            this.associatedUsers.add(user);
    }
}