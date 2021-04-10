package edu.fudan.sqat.domain;

import javax.persistence.*;

@Entity
public class FinancialManagement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;
    private Integer type; // 1：股票；2：基金；3：定期理财产品
}
