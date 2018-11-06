package com.mc.challenge.service;

/**
 * @author M.Kubendranathan
 *
 * This service interface class for the implementation that
 * will execute DFS algorithm to find if 2 paths are connected with the given dataset.
 */
public interface FindConnectedCityService {

    /**
     * This method will execute the validaion of the 2 points to see if they are connected.
     * @param origin    origin city
     * @param dest      destination city
     * @return boolean  result
     */
    boolean isConnected(String origin, String dest);
}
