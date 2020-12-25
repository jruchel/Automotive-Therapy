package org.jruchel.carworkshop.repositories;

import org.jruchel.carworkshop.entities.Client;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClientRepository extends PagingAndSortingRepository<Client, Integer> {

    @Query(nativeQuery = true, value = "select * from client where phone_number = ?1 limit 1")
    Client findByPhone(String phone);

    @Query(nativeQuery = true, value = "select * from client where email = ?1 limit 1")
    Client findByEmail(String email);

    @Query(nativeQuery = true, value = "SELECT * from carworkshop.client where carworkshop.client.id in (select distinct carworkshop.orders.client_id from carworkshop.orders where carworkshop.orders.responded = 0)")
    List<Client> getUnrespondedClients(Pageable pageable);
}