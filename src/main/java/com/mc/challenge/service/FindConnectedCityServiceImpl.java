package com.mc.challenge.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author M.Kubendranathan
 *
 * This service implementation class will execute DFS algorithm to find if 2 paths are connected with the given dataset.
 */
@Component
public class FindConnectedCityServiceImpl implements FindConnectedCityService{

    private final String CLASS_NAME = FindConnectedCityServiceImpl.class.getName();

    // this parameter will hold the Map of the tree with all the connections
    private Multimap<String, String> cityList;

    // This tree will hold all the unique pointers which will be used for data validation
    private TreeSet<String>  uniqueCityNames;

    private static final Logger logger = Logger.getLogger(FindConnectedCityServiceImpl.class.getName());

    // input file
    @Value("${code-challenge.file-name}")
    private String fileName;

    /**
     * init method - read csv FILE and initialize the Tree to be searched for
     * @throws IOException IOException is thrown
     */
    @PostConstruct
    public void init() throws IOException {

        String METHOD_NAME = "init";
        logger.entering(CLASS_NAME, METHOD_NAME);

        cityList = ArrayListMultimap.create();
        uniqueCityNames = new TreeSet<>();

        // read the input file
        Reader reader = Files.newBufferedReader( Paths.get(new ClassPathResource(fileName).getPath()));

        CSVReader csvReader = new CSVReader(reader);

        // read all data and store it in a temp list
        List<String[]> dataRead = csvReader.readAll();

        int numberOfRecordsRead = dataRead.size();
        for (String[] city : dataRead) {
            try {
                // add both points to the main tree
                cityList.put(city[0].trim(), city[1].trim());

                // add reverse connections to the main tree (if A is connected to B, then B is also connected to A)
                cityList.put(city[1].trim(), city[0].trim());

            } catch (Exception e) {
                // ignorning any exceptions if specific records fails.
                e.printStackTrace();
                logger.log(Level.WARNING, "Failed processing city {0}", city);
            }

        }

        // add all the city to the unique name list
        uniqueCityNames.addAll(cityList.keySet());

        logger.log(Level.INFO, "FindConnectedCityService init completed. Number of records read {0} ", new Object[] {numberOfRecordsRead});

        logger.exiting(CLASS_NAME, METHOD_NAME);

    }

    /**
     *   caching the data to ensure we don't process the same cities again. Current solution using simple provider..
     *   Ideally, we would use a solution that can scale on a production system (ie: JSR107 supported options).
     *
     *   given 2 points, this method will check if they are connected or not.
    */

    @Cacheable("connectedCities")
    public boolean isConnected(String origin, String dest) {

        String METHOD_NAME = "isConnected";
        logger.entering(CLASS_NAME, METHOD_NAME);

        boolean result;

        // TC#1 : check for invalid data-set
        // TC#2 : origin and destination must exists
        if ((StringUtils.isEmpty(origin) || StringUtils.isEmpty(dest)) || (StringUtils.isEmpty(origin) && StringUtils.isEmpty(dest))
                || (!(uniqueCityNames.contains(origin)) || !uniqueCityNames.contains(dest))
                || (!(uniqueCityNames.contains(origin)) && !uniqueCityNames.contains(dest)))
            return false;


        // TC#3 : origin same as destination
        if (origin.equals(dest) && uniqueCityNames.contains(origin)) {
            logger.log(Level.INFO, "Origin {0} same as destination {1} " , new Object[] {origin, dest} );
            return true;
        }

        Map<String, Collection<String>> tempList = cityList.asMap();

        // select the node  with all the direct connections from the source
        Collection<String> directConnections = tempList.get(origin);

        // direct match found in the Tree.
        if (directConnections.contains(dest)) {
            logger.log(Level.INFO, "Origin {0} to Destination {1} is FOUND ", new Object[] {origin, dest});
            return true;
        }

        // no immediate direct connections are found in the Tree..  let's execute DFS to check for any indirect connections.
        result = depthFirstSearch(origin,dest,directConnections);
        logger.log(Level.INFO, "Origin {0} to Destination {1} is {2} ", new Object[] {origin, dest, result});

        logger.exiting(CLASS_NAME, METHOD_NAME);
        return result;
    }

    /**
     *  This method uses Depth First Search to search through the nodes (recursively)
     */
    private boolean depthFirstSearch(String origin, String dest, Collection<String> directNodes) {

        String METHOD_NAME = "depthFirstSearch";
        logger.entering(CLASS_NAME, METHOD_NAME);

        boolean result = false;

        Iterator<String> itrDirectNodes = directNodes.iterator();
        String visited = origin;

        while (itrDirectNodes.hasNext()){
            origin = itrDirectNodes.next();

            // pick the immediate nodes of the traversal and travel down the node until finding the match
            Map<String, Collection<String>> fullTree = cityList.asMap();
            Collection<String> directConnections = fullTree.get(origin);

            if (directConnections != null) {
                // found the match - recurse out of the loop
                if (directConnections.contains(dest)) return true;

                // remove the visited nodes
                directConnections.remove(origin);
                directConnections.remove(visited);

                result = depthFirstSearch(origin, dest, directConnections);

                if(result) break;
            }
        }

        logger.exiting(CLASS_NAME, METHOD_NAME);
        return result;
    }

}
