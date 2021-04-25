package edu.fudan.sqat.repository;

import edu.fudan.sqat.domain.Purchase;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends CrudRepository<Purchase, Long> {
    Purchase findPurchaseByIDCodeAndName(String IDCode, String name);

    Iterable<Purchase> findAll();

    Iterable<Purchase> findPurchaseByIDCode(String IDCode);
}
