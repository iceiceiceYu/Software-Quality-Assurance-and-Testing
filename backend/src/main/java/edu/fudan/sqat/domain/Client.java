package edu.fudan.sqat.domain;

import javax.persistence.*;

/**
 * @author zyl
 */
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String IDCode;

    private String name;
    private String gender;
    private Integer age;

    public Client() {
    }

    public Client(String IDCode, String name, String gender, Integer age) {
        this.IDCode = IDCode;
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIDCode() {
        return IDCode;
    }

    public void setIDCode(String IDCode) {
        this.IDCode = IDCode;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
