package com.mc.challenge.controller;

import com.mc.challenge.processor.FindCity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FindCityController {


    @Autowired
    FindCity findCity;

    @RequestMapping("/connected")
    public @ResponseBody
    String isConnected(@RequestParam("city1") String origin, @RequestParam("city2") String destination){


        if (findCity.isConnected(origin, destination)) return "yes";

        return "no";
        
    }

}
