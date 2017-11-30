package com.project.company.entities;
import org.springframework.stereotype.Component;
import java.util.Set;
import javax.persistence.*;
import java.util.List;

@Entity
@Component
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(fetch= FetchType.EAGER, mappedBy = "position")
    private Set<Person> persons;

    public String position;

    public Position() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    @Override
    public String toString() {
        return "Position{" +
                "id=" + id +
                ", persons=" + persons +
                ", position='" + position + '\'' +
                '}';
    }
}
