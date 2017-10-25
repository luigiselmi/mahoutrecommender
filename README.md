Doeeet Recommender
==================
Items are recommended to users based on collaborative filtering. The implicit feedbacks, e.g. views of details pages of items, are collected and used to recommend
similar items to users. The recommendations are computed using algorithms from Apache Mahout. The recommendations are available as a web service. The user sends the userid and the number of items to be recommended and the service returns the items and the score. The service can use different algorithms with different data models. A servlet is started and is initialized with a data model in the web.xml file.

## Prerequisites 
You need Java 8 and Maven to build the code. Jetty is used as a servlet container.

## Install
    
    $ git clone https://github.com/luigiselmi/mahoutrecommender.git

## Build
The recommender is a web service and it must be packaged as a war file and installed in a servlet container such as Jetty.
    
    $ mvn install

## Run
In case Jetty is used as a servlet container you can create a base for Jetty, as explained in the Jetty documentation <add link> and copy the war file in the webapps folder. From the Jetty base folder execute the command

    $ java -jar $JETTY_HOME/start.jar
    
## Version
0.0.1
## Troubleshooting
None
## License
Apache 2.0