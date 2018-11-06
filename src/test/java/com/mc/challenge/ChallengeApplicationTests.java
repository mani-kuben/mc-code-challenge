package com.mc.challenge;

import com.mc.challenge.service.FindConnectedCityService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author M.Kubendranathan
 *
 * Spring Boot JUnit application class to check if 2 points are connected in a given map
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChallengeApplicationTests {

    @Test
    public void contextLoads() {
    }


    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    FindConnectedCityService findConnectedCityService;

    /**
     *  This method will test the Controller method directly for all test scenarios - INVALID DATA SET
     */
    @Test
    public void validateData() {


        // both cities are not passed in
        Assert.assertEquals("no",restTemplate.getForObject("http://localhost:" + port + "/connected?city1=&city2=",String.class));

        // missing destination
        Assert.assertEquals("no",restTemplate.getForObject("http://localhost:" + port + "/connected?city1=Toronto&city2=",String.class));

        // missing origin
        Assert.assertEquals("no",restTemplate.getForObject("http://localhost:" + port + "/connected?city1=&city2=Toronto",String.class));

        // both cities are same - but none exists in the file
        Assert.assertEquals("no",restTemplate.getForObject("http://localhost:" + port + "/connected?city1=NOT exists&city2=NOT exists",String.class));

    }


    /**
     *  This method will test the Controller method directly for all test scenarios - DIRECT connections between 2 pointers in a map
     */
    @Test
    public void directConnectionsTest()  {

        // both cities are same and exists
        Assert.assertEquals("yes",restTemplate.getForObject("http://localhost:" + port + "/connected?city1=Boston&city2=New York",String.class));

    }

    /**
     *  This method will test the Controller method directly for all test scenarios - indirect connections between 2 pointers in a map
     */
    @Test
    public void inDirectConnectionsTest_DFS_Search() {

        Assert.assertEquals("yes",restTemplate.getForObject("http://localhost:" + port + "/connected?city1=Boston&city2=Newark",String.class));

        Assert.assertEquals("yes",restTemplate.getForObject("http://localhost:" + port + "/connected?city1=Boston&city2=Philadelphia",String.class));

        Assert.assertEquals("no",restTemplate.getForObject("http://localhost:" + port + "/connected?city1=Philadelphia&city2=Albany",String.class));

    }


    /**
     *  This method will test the Service method directly for all test scenarios
     */
    @Test
    public void executeTestsForServiceClass(){

        Assert.assertTrue(findConnectedCityService.isConnected("Boston", "New York"));
        Assert.assertTrue(findConnectedCityService.isConnected("Boston", "Newark"));
        Assert.assertTrue(findConnectedCityService.isConnected("Boston", "Philadelphia"));
        Assert.assertFalse(findConnectedCityService.isConnected("Philadelphia", "Albany"));


    }
}
