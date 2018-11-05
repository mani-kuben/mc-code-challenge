# mc-code-challenge

Master Card Challenge

Swagger API - https://app.swaggerhub.com/apis-docs/mani.kuben/find-connected-cities/1.0.0

Given a dataset contaning connecting cities, this API will execute DFS algorithm to find if 2 input cities are connected. Two cities will be connected as long as there's a path to go from 1 city to another with-in the given data set.

For Example - with given data set

Boston, New York Philadelphia, Newark Newark, Boston Trenton, Albany

The following will be returned by the API

http://localhost:8080/connected?origin=Boston&destination=Newark Should return yes

http://localhost:8080/connected?origin=Boston&destination=Philadelphia Should return yes

http://localhost:8080/connected?origin=Philadelphia&destination=Albany Should return no

This springboot application will execute Depth First Search algorithm to find the path. The result will be cached to use in subsequent requests.
