package com.project.company.repository;

import com.project.company.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

    List<Person> findByName(String name);

    List<Person> findBySurname(String surname);

    List<Person> findByEmail(String email);

    List<Person> deleteByName(String name);

}
