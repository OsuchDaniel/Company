package com.project.company.controller;

import com.project.company.entities.Person;
import com.project.company.entities.Position;
import com.project.company.model.Employee;
import com.project.company.model.PersonInPosition;
import com.project.company.repository.PersonRepository;
import com.project.company.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
public class RestController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PositionRepository positionRepository;

    @RequestMapping(value = {"/api/persons"}, method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity showPersons() {
        return new ResponseEntity(GetByParam("", EnumParams.All), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/add", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity addPerson(@RequestBody Employee employee) {

        try {
            addPersons(employee);

        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(path = "/api/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> removePerson(@PathVariable long id) {
        try {
            personRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/api/search/position", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity position() {
        List<PersonInPosition> personInPositions = new ArrayList<>();
        try {
            for (Position p : positionRepository.findAll()) {

                PersonInPosition pip = new PersonInPosition();
                pip.setName(p.getPosition());
                pip.setCount(p.getPersons().size());
                personInPositions.add(pip);
            }
        } catch (Exception ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(personInPositions, HttpStatus.OK);
    }

    @RequestMapping(value = {"/api/search/name/{name}"}, method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity findByName(@PathVariable String name) {
        return new ResponseEntity(GetByParam(name, EnumParams.ByName), HttpStatus.OK);
    }

    @RequestMapping(value = {"/api/search/surname/{surname}"}, method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity findBySurname(@PathVariable("surname") String surname) {
        return new ResponseEntity(GetByParam(surname, EnumParams.BySurname), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/search/email/{email}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity findByEmail(@PathVariable("email") String email) {
        return new ResponseEntity(GetByParam(email, EnumParams.ByEmail), HttpStatus.OK);
    }

    private List<Employee> GetByParam(String valueToGetBy, EnumParams parameter) {

        List<Employee> employees = new ArrayList<>();
        List<Person> personList = new ArrayList<>();
        switch (parameter) {
            case ByEmail:
                personList = personRepository.findByEmail(valueToGetBy);
                break;
            case ByName:
                personList = personRepository.findByName(valueToGetBy);
                break;
            case BySurname:
                personList = personRepository.findBySurname(valueToGetBy);
                break;
            default:
                personList = (List<Person>) personRepository.findAll();
                break;
        }

        for (Person p : personList) {
            employees.add(new Employee(p.getName(), p.getSurname(), p.getEmail(), p.getPosition().getPosition(), p.getId()));
        }
        return employees;
    }

    private enum EnumParams {
        ByName,
        BySurname,
        ByEmail,
        All
    }

    private void addPersons(Employee employee){

        Person newPerson = new Person();

        Position ps = positionRepository.findByPosition(employee.getPositionName());
        if (ps == null) {
            Position position = new Position();
            position.setPosition(employee.getPositionName());
            ps = positionRepository.save(position);

        }
        newPerson.setPosition(ps);
        newPerson.setName(employee.getName());
        newPerson.setSurname(employee.getSurname());
        newPerson.setEmail(employee.getEmail());
        personRepository.save(newPerson);
    }
}