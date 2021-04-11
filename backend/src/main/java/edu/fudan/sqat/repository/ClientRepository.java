package edu.fudan.sqat.repository;

import edu.fudan.sqat.domain.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {
    Client findClientByIDCode(String IDCode);
}
