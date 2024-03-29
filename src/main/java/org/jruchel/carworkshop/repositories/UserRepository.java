package org.jruchel.carworkshop.repositories;

import org.jruchel.carworkshop.models.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    @Query(nativeQuery = true, value = "select * from users where username = ?1 limit 1")
    User findByUsername(String username);
}