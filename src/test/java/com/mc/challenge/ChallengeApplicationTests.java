package com.mc.challenge;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

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


    @Test
    public void directConnectionsTest()  {

        // both cities are same and exists
        Assert.assertEquals("yes",restTemplate.getForObject("http://localhost:" + port + "/connected?city1=Toronto&city2=Toronto",String.class));

        // both cities are different and exists directly connected
        Assert.assertEquals("yes",restTemplate.getForObject("http://localhost:" + port + "/connected?city1=Toronto&city2=New York",String.class));

        // both cities exists but NOT connected
        Assert.assertEquals("no",restTemplate.getForObject("http://localhost:" + port + "/connected?city1=Toronto&city2=Honolulu",String.class));

        // both cities are different and exists direct
        Assert.assertEquals("yes",restTemplate.getForObject("http://localhost:" + port + "/connected?city1=Pickering&city2=Toronto",String.class));

    }

    /*
    @Test
    public void inDirectConnectionsTest() {

        // both cities are same and exists
        Assert.assertEquals("yes",restTemplate.getForObject("http://localhost:" + port + "/connected?city1=Toronto&city2=Toronto",String.class));

        // both cities are different and exists directly connected
        Assert.assertEquals("yes",restTemplate.getForObject("http://localhost:" + port + "/connected?city1=Toronto&city2=Toronto",String.class));

        // both cities exists but NOT connected
        Assert.assertEquals("no",restTemplate.getForObject("http://localhost:" + port + "/connected?city1=Toronto&city2=Honolulu",String.class));

        // both cities are different and exists direct
        Assert.assertEquals("yes",restTemplate.getForObject("http://localhost:" + port + "/connected?city1=Toronto&city2=Toronto",String.class));

    }
    */

}
