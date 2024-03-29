package org.jruchel.carworkshop.repositories;

import org.jruchel.carworkshop.models.entities.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

    @Query(nativeQuery = true, value = "select * from roles where title = ?1 limit 1")
    Role getRoleByTitle(String title);

}