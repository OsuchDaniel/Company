package com.project.company.repository;

import com.project.company.entities.Position;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PositionRepository extends CrudRepository<Position, Long> {

    Position findByPosition(String position);


}
