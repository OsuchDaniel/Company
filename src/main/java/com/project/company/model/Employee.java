package com.project.company.model;

public class Employee {
    String name;
    String surname;
    String email;
    String positionName;


    public Employee() {
    }

    public Employee(String name, String surname, String email, String positionName) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.positionName = positionName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
}
