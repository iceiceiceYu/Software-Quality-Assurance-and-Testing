package com.softwaretesting.demo.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zyl
 */
@Entity
public class Client implements UserDetails {

    private static final long serialVersionUID = 4942181896974654934L;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique =true)
    private String name;

    private String gender;
    private String pwd;
    private Integer age;
    private String idCard;


    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Set<Authority> authorities = new HashSet<>();

    public Client() {}

    public Client(String name, String pwd, Integer age, String gender, String idCard, Set<Authority> authorities) {
        this.name=name;
        this.pwd=pwd;
        this.age=age;
        this.gender=gender;
        this.idCard = idCard;
        this.authorities = authorities;
    }



    //重写equals方法, 最佳实践就是如下这种判断顺序:
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Client))
            return false;
        if (obj == this)
            return true;
        return this.getId().equals(((Client) obj).getId());
    }

    public int hashCode(){
        return id.intValue();//简单原则
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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


    @Override
    public String getPassword() {
        return pwd;
    }

    @Override
    public String getUsername() {
        return name;
    }


    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
    public String getIdCard() {return idCard;}
}

