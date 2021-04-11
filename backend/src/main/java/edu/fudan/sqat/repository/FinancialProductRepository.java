package edu.fudan.sqat.repository;

import edu.fudan.sqat.domain.FinancialProduct;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialProductRepository extends CrudRepository<FinancialProduct, Long> {
    Iterable<FinancialProduct> findAll();

    FinancialProduct findFinancialProductByName(String name);

    Iterable<FinancialProduct> findFinancialProductByType(String type);
}
