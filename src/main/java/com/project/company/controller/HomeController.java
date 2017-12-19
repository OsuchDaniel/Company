package com.project.company.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = {"/persons"}, method = RequestMethod.GET)
    public String Persons() {
        return "persons";
    }

    @RequestMapping(value = {"/search"}, method = RequestMethod.GET)
    public String Search() {
        return "search";
    }

    @RequestMapping(value = {"/add"}, method = RequestMethod.GET)
    public String addPerson() {
        return "add";
    }

    @RequestMapping(value = {"/positions"}, method = RequestMethod.GET)
    public String positions() {
        return "positions";
    }
}