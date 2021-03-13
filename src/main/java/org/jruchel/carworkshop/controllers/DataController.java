package org.jruchel.carworkshop.controllers;

import org.jruchel.carworkshop.automation.SecuredMapping;
import org.jruchel.carworkshop.models.data.Data;
import org.jruchel.carworkshop.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/data")
public class DataController {

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @SecuredMapping(path = "/opinions", method = RequestMethod.GET, role = "moderator")
    public Data getOpinionsData() {
        return dataService.getOpinionsData();
    }

    @SecuredMapping(path = "/orders", method = RequestMethod.GET, role = "moderator")
    public Data getOrdersData() {
        return dataService.getOrdersData();
    }

}
