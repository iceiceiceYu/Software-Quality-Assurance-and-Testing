package com.softwaretesting.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;


/**
 * @author zyl
 */
@Entity
public class Authority implements GrantedAuthority {

    private static final long serialVersionUID = -7255708384645303338L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String authority;

    @ManyToMany(mappedBy = "authorities")
    @JsonIgnore
    private Set<Client> users;

    public Authority() {
    }

    public Authority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Set<Client> getUsers() {
        return users;
    }

    public void setUsers(Set<Client> users) {
        this.users = users;
    }
}
