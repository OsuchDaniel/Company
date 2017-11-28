package com.project.company.controller;

import com.project.company.model.Person;
import com.project.company.model.Position;
import com.project.company.repository.PersonRepository;
import com.project.company.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/*dodawanie nowego pracownika (wymagane atrybuty: imię, nazwisko, stanowisko, adres email)
zwracanie listy wszystkich pracowników z możliwością filtrowania po adresie email, imieniu, nazwisku
zwracanie listy wszystkich stanowisk razem z ilością pracowników przypisanych do nich
usuwanie danego pracownika z listy*/

@Controller
//@EnableWebMvc
public class HomeController {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PositionRepository positionRepository;


    @RequestMapping(value = {"/persons"}, method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity showPersons() {
        return new ResponseEntity(personRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
    private ResponseEntity addPerson(@RequestBody Person person) {
        //Person p = new Person();
        //Position ps = new Position();

        // p.setName(person.getName());
        // p.setSurname(person.getSurname());
        // p.setEmail(person.getEmail());

        //positionRepository.existsByPosition(position)
        // if (ps.getPosition().toLowerCase().equals(position.toLowerCase()))
        //       p.setIdPosition(ps.getId());
        //   else
        //      p.setIdPosition(ps.getId() + 1);
        //if(!positionRepository.existsByPosition(position.getPosition()))
        //     person.setPosition(position);
        // else
        //   person.setPosition(positionRepository.);

        personRepository.save(person);

        // ps.setPosition(position.getPosition());
        //  positionRepository.save(ps);

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{name}", method = RequestMethod.DELETE, produces = "application/json")
    private ResponseEntity removePerson(@PathVariable("name") String name) {
        return new ResponseEntity(personRepository.deleteByName(name), HttpStatus.OK);
    }

    @RequestMapping(value = {"/search/name/{name}"}, method = RequestMethod.GET, produces = "application/json")
    private ResponseEntity findByName(@PathVariable("name") String name) {
        return new ResponseEntity(personRepository.findByName(name), HttpStatus.OK);
    }

    @RequestMapping(value = {"/search/surname/{surname}"}, method = RequestMethod.GET, produces = "application/json")
    private ResponseEntity findBySurname(@PathVariable("surname") String surname) {
        return new ResponseEntity(personRepository.findBySurname(surname), HttpStatus.OK);
    }

    @RequestMapping(value = "/search/email/{email}", method = RequestMethod.GET, produces = "application/json")
    private ResponseEntity findByEmail(@PathVariable("email") String email) {
        return new ResponseEntity(personRepository.findByEmail(email), HttpStatus.OK);
    }

    @RequestMapping(value = "/search/position/{position}", method = RequestMethod.GET, produces = "application/json")
    private ResponseEntity filterByPosition(@PathVariable("position") String position) {

        return new ResponseEntity(positionRepository.findByPosition(position), HttpStatus.OK);
    }
}
