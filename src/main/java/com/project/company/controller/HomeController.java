package com.project.company.controller;

import com.project.company.entities.Person;
import com.project.company.entities.Position;
import com.project.company.model.Employee;
import com.project.company.model.PersonInPosition;
import com.project.company.repository.PersonRepository;
import com.project.company.repository.PositionRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;


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

    private static final org.apache.log4j.Logger logger = Logger.getLogger(HomeController.class);

    @RequestMapping(value = {"/persons"}, method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity showPersons() {
      /*  List<Employee> employee = new ArrayList<>();
        for (Position ps : positionRepository.findAll()) {
            for (Person p : personRepository.findAll()) {
                if (p.getId() == ps.getId())
                    employee.add(new Employee(p.getName(), p.getSurname(), p.getEmail(), ps.getPosition()));
            }
        }*/
        return new ResponseEntity(personRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity addPerson(@RequestBody Employee employee) {

        try {
            Position newPosition;
            Person newPerson = new Person();

            List<Position> ps = positionRepository.findByPosition(employee.getPositionName());
            Position dbPosition = ps.get(0);
            if (dbPosition != null && employee.getPositionName().equals(dbPosition.getPosition())) {
                newPerson.setPosition(ps.get(0));
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
            logger.error("Error message", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{name}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity removePerson(@PathVariable("name") String name) {
        return new ResponseEntity(personRepository.deleteByName(name), HttpStatus.OK);
    }

    @RequestMapping(value = {"/search/name/{name}"}, method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity findByName(@PathVariable("name") String name) {
        return new ResponseEntity(personRepository.findByName(name), HttpStatus.OK);
    }

    @RequestMapping(value = {"/search/surname/{surname}"}, method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity findBySurname(@PathVariable("surname") String surname) {
        return new ResponseEntity(personRepository.findBySurname(surname), HttpStatus.OK);
    }

    @RequestMapping(value = "/search/email/{email}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity findByEmail(@PathVariable("email") String email) {
        return new ResponseEntity(personRepository.findByEmail(email), HttpStatus.OK);
    }

    @RequestMapping(value = "/search/position", method = RequestMethod.GET, produces = "application/json")
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
            logger.error("Error message", ex);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(personInPositions, HttpStatus.OK);
    }
}
