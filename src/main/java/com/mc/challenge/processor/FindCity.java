package com.mc.challenge.processor;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class FindCity {

    private final String CLASS_NAME = FindCity.class.getName();

    Multimap<String, String> cityList;
    TreeSet<String>  uniqueCityNames;

    Logger logger = Logger.getLogger(FindCity.class.getName());

    public FindCity(){

        uniqueCityNames = new TreeSet<>();

        // for initial testing purposes.. to be removed and read from the file.
        cityList = ArrayListMultimap.create();

        cityList.put("Ajax", "Toronto");        // Ajax to Barrie  test case (indirect via Scarborough - origin to destination)
        cityList.put("Toronto", "Oshawa");
        cityList.put("Toronto", "Oakville");
        cityList.put("Ajax", "Kingston");
        cityList.put("Toronto", "New York");
        cityList.put("New York", "Houston");    // Dallas to Houston test case (indirect from dest to dest list)
        cityList.put("New York", "San Jose");
        cityList.put("San Jose", "Dallas");
        cityList.put("Oshawa", "Ottawa");
        cityList.put("Ottawa", "Montreal");
        cityList.put("Barrie", "Scarborough");

        cityList.put("Scarborough", "Barrie");
        cityList.put("Oshawa", "Scarborough");

        cityList.put("Scarborough", "Oshawa");  // Oshawa to Scarborough test case (indirect from origin to origin)
        cityList.put("Pickering", "Toronto");
        cityList.put("Victoria", "Ajax");

        cityList.put("Honolulu", "Hilo");

        uniqueCityNames.addAll(cityList.keySet());
        uniqueCityNames.addAll(cityList.values());

    }

    // given 2 points, this method will check if they are connected or not.
    public boolean isConnected(String origin, String dest) {

        String METHOD_NAME = "isConnected";
        logger.entering(CLASS_NAME, METHOD_NAME);

        boolean result = false;

        // TC#1 : check for invalid data-set
        // TC#2 : origin and destination must exists
        if ((StringUtils.isEmpty(origin) || StringUtils.isEmpty(dest)) || (StringUtils.isEmpty(origin) && StringUtils.isEmpty(dest))
                || (!(uniqueCityNames.contains(origin)) || !uniqueCityNames.contains(dest))
                || (!(uniqueCityNames.contains(origin)) && !uniqueCityNames.contains(dest)))
            return false;


        // TC#3 : origin same as destination
        if (origin.equals(dest) && uniqueCityNames.contains(origin)) {
            logger.log(Level.INFO, "Origin {0} same as destination {1} " , new Object[] {origin, dest} );
            logger.log(Level.INFO, "Origin <" + origin );
            return true;
        }

        Map<String, Collection<String>> tempList = cityList.asMap();

        // create a new list with all the direct connections from the source
        Collection<String> directConnections = tempList.get(origin);

        // direct match
        if (directConnections.contains(dest)) {
            logger.log(Level.INFO, "Origin {0} to Destination {1} is FOUND ", new Object[] {origin, dest});
            return true;
        }

        // evaluate indirect matches

        //
        if (tempList.containsValue(origin)){  // origin city is in the destination list
            // reverse lookup of all  cities from origin list
            // tempList.

        }


        result = findIndirectConnection(origin,dest,directConnections);
        logger.log(Level.INFO, "Origin {0} to Destination {1} is {2} ", new Object[] {origin, dest, result});

        logger.exiting(CLASS_NAME, METHOD_NAME);
        return result;
    }

    /*
        This recursive method will find the indirect connections between the 2 points.

        Using the full 'map' - it'll check if the input (direct points) match with the destination.
     */
    private boolean findIndirectConnection(String origin, String dest, Collection<String> subList) {

        String METHOD_NAME = "findIndirectConnection";
        logger.entering(CLASS_NAME, METHOD_NAME);

        boolean result = false;
        Iterator<String> subCity = subList.iterator();

        String dupCity = origin;
        while (subCity.hasNext()){
            origin = subCity.next();

            Map<String, Collection<String>> tempList = cityList.asMap();
            Collection<String> subList2 = tempList.get(origin);

            if (subList2 != null) {

                // found the records
                if (subList2.contains(dest)) return true;


                // remove the origin from the sublist (otherwise will get stuck in infinite loop)
                subList2.remove(origin);
                subList2.remove(dupCity);

                result = findIndirectConnection(origin, dest, subList2);

                if(result) break;
            }
        }

        logger.exiting(CLASS_NAME, METHOD_NAME);
        return result;
    }

}
