package com.project.company.controller;

import com.project.company.entities.Person;
import com.project.company.entities.Position;
import com.project.company.model.Employee;
import com.project.company.model.PersonInPosition;
import com.project.company.repository.PersonRepository;
import com.project.company.repository.PositionRepository;
//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import sun.plugin2.message.Message;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/*dodawanie nowego pracownika (wymagane atrybuty: imię, nazwisko, stanowisko, adres email)
zwracanie listy wszystkich pracowników z możliwością filtrowania po adresie email, imieniu, nazwisku
zwracanie listy wszystkich stanowisk razem z ilością pracowników przypisanych do nich
usuwanie danego pracownika z listy*/

@Controller
public class HomeController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PositionRepository positionRepository;

    //private static final org.apache.log4j.Logger logger = Logger.getLogger(HomeController.class);

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = {"/persons"}, method = RequestMethod.GET)
    public String Persons() {
        return "persons";
    }

    @RequestMapping(value = {"/search"}, method = RequestMethod.GET)
    public String Search(){
        return "search";
    }

    @RequestMapping(value = {"/api/persons"}, method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity showPersons() {
        return new ResponseEntity(GetByParam("", EnumParams.All), HttpStatus.OK);
    }

    @RequestMapping(value = {"/add"}, method = RequestMethod.GET)
    public String addPerson() {
        return "add";
    }

    @RequestMapping(value = "/api/add", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity addPerson(@RequestBody Employee employee) {

        try {
            Position newPosition;
            Person newPerson = new Person();

            List<Position> ps = positionRepository.findByPosition(employee.getPositionName());
            if (ps.size() > 0) {
                Position dbPosition = ps.get(0);
                if (dbPosition != null && employee.getPositionName().equals(dbPosition.getPosition())) {
                    newPerson.setPosition(ps.get(0));
                }
            } else {
                Position position = new Position();
                position.setPosition(employee.getPositionName());
                newPosition = positionRepository.save(position);
                newPerson.setPosition(newPosition);
            }


            newPerson.setName(employee.getName());
            newPerson.setSurname(employee.getSurname());
            newPerson.setEmail(employee.getEmail());
            personRepository.save(newPerson);

        } catch (Exception e) {
            //logger.error("Error message", e);
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

    @RequestMapping(value = {"/byName"}, method = RequestMethod.GET)
    public String findByName() {
        return "byName";
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

    @RequestMapping(value = {"/positions"}, method = RequestMethod.GET)
    public String positions() {
        return "positions";
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
            //logger.error("Error message", ex);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(personInPositions, HttpStatus.OK);
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
}