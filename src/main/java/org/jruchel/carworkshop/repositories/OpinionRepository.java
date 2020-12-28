package org.jruchel.carworkshop.repositories;

import org.jruchel.carworkshop.entities.Opinion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpinionRepository extends CrudRepository<Opinion, Integer> {

    @Query(nativeQuery = true, value = "select * from opinions limit ?1 order by stars desc")
    List<Opinion> getOpinions(int amount);

}