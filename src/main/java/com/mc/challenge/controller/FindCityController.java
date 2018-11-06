package com.mc.challenge.controller;

import com.mc.challenge.service.FindConnectedCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author M.Kubendranathan
 *
 * This is the main controller of the application. It'll invoke the FindConnectedCityService.isConnected method passing in the parameters
 * to get the response back
 */

@Controller
public class FindCityController {

    @Autowired
    FindConnectedCityService findCity;

    /**
     * method will invoke  FindConnectedCityService.isConnected  to check for connections between 2 points using DFS (Depth First Search)
     *
     * @param city1 = Origin city
     * @param city2 = Destination city
     *
     * @path /connected
     * @return String yes/no
     */

    @RequestMapping("/connected")
    public @ResponseBody
    String isConnected(@RequestParam("city1") String origin, @RequestParam("city2") String destination){

        if (findCity.isConnected(origin, destination)) return "yes";

        return "no";

    }

}
