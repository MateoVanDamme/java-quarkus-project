# Project

We created a docker compose for the project. We ran into a bug, the endpoints were avaiable but only through a browser, we could not fix this so we decided to provide an alternative way to run the project. To test the program in an automated way, run the monolith service using ```gradle monolith:quarkusDev```, and run the [python notebook](https://github.ugent.be/sysdesign-2022-project/sysdesign-2022-project-group-2/blob/c58172b1e2af944cbf097be0c1fcce23505fcb4b/scripts/testProject.ipynb)

The codebase is structured as a multi-module Gradle project, containing the following modules:

* `catalogus-service`: Stores painting and exhibitions.
* `ratings-service`: Handles ratings for paintings.
* `analytics-service`: Provides users with recommendations based on their past ratings.
* `ticket-service`: A service for buying tickets.
* `staff-service`: Keeps track of staff planning

There are no dummy services. All services are fully implemented. 
