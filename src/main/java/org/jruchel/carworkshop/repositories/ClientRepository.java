package org.jruchel.carworkshop.repositories;

import org.jruchel.carworkshop.entities.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends CrudRepository<Client, Integer> {

    @Query(nativeQuery = true, value = "select * from client where phone_number = ?1 limit 1")
    Client findByPhone(String phone);

    @Query(nativeQuery = true, value = "select * from client where email = ?1 limit 1")
    Client findByEmail(String email);

}