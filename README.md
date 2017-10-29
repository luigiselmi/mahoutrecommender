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
You can run the recommender as a Java Web application in a servlet container such as Jetty or in a docker container.

### Java Web Application
In case Jetty is used as a servlet container you can create a base for Jetty, as explained in the [Jetty documentation](https://www.eclipse.org/jetty/documentation/current/quickstart-running-jetty.html), and copy the war file in the webapps folder. You can rename the war file as ROOT.war so you will not need to use a context. From the Jetty base folder execute the command

    $ java -jar $JETTY_HOME/start.jar jetty.http.port=8100

### Docker container
You can run the recommender within a docker container by first building the container 

    $ docker build -t doeeet/recommender:v0.1.0 .

and then running it

    $ docker run -d -p 8100:8100 --name recommender doeeet/recommender:v0.1.0

## Use
You can send a request to the web service using the HTML page at http://localhost:8100/ with the id of the user for which you want the recommendation and the number of items to recommend.

## Version
0.1.0

## Troubleshooting
None

## License
Apache 2.0
