package edu.fudan.sqat.domain;

import javax.persistence.*;

/**
 * @author zyl
 */
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String IDCode;
    private Double total;


    public Account() {
    }

    public Account(String IDCode) {
        this.IDCode = IDCode;
        this.total = 0.0;
    }

    public Account(String IDCode, Double total) {
        this.IDCode = IDCode;
        this.total = total;
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

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
