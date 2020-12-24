package org.jruchel.carworkshop.repositories;
import org.jruchel.carworkshop.entities.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {

    @Query(nativeQuery = true, value = "select * from orders where responded = 0")
    List<Order> getUnresponedOrders();

}