package org.jruchel.carworkshop.repositories;
import org.jruchel.carworkshop.entities.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Integer> {

    @Query(nativeQuery = true, value = "select * from orders where status = 'pending'")
    List<Order> getUnrespondedOrders(Pageable pageable);

}