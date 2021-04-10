package com.softwaretesting.demo.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {

    private static final long serialVersionUID = -8509478570203058031L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Client owner;

    @OneToMany(cascade = CascadeType.MERGE, fetch=FetchType.LAZY)
    private Set<DebtRecord> debtRecords = new HashSet<>();

    public Account() {
    }

    public Account(Client owner){
        this.owner=owner;
    }

    public Client getOwner() {
        return owner;
    }

    public void setOwner(Client owner) {
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<DebtRecord> getDebtRecords() {
        return debtRecords;
    }

    public void setDebtRecords(Set<DebtRecord> debtRecords) {
        this.debtRecords = debtRecords;
    }
}
