Collaborative Filtering Recommender
===================================
Items are recommended to users based on collaborative filtering. The implicit feedbacks, e.g. views of details pages of items, 
are collected and used to recommend similar items to users. The recommendations are computed using algorithms from Apache Mahout. 
The data used by Mahout for the collaborative filtering must contain for each record the userID (long), the itemID (long) and a 
value (double). Nonetheless the itemIDs are assumed to be of type string. Mahout provides a special hash map with the mappings 
between the string Id and the numeric Id. The data model is built from the user navigation log files using the code in the 
[producer repository](https://github.com/luigiselmi/mahoutdataproducer). The file containing the aggregated records must be set 
in the config.properties file. The recommendations are available as a web service. The user sends the userid and the number of 
items to be recommended and the service returns the items and the score. The service can use different algorithms with different 
data models. A servlet is started and is initialized with a data model in the web.xml file. The signals are read from a base file, 
e.g. signals.csv, and from other files in the same folder whose name begins in the same way as the base file, say signals-20180424.csv. 
The signals data are kept in a Mahout [FileDataModel](https://mahout.apache.org/docs/0.13.0/api/docs/mahout-mr/org/apache/mahout/cf/taste/impl/model/file/FileDataModel.html) sending a request to the refresh API.
If a new signal files have been created and the last refresh was before the minimum reload interval time the data model is reloaded 
and the recommendations updated.

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
In order to use the docker container you first need to create the volume containing the data. This can be done creating a container that maps the path of the file in the host machine (e.g. /home/dataproducer/signals.csv) to the path set in the config.properties file where the recommender running in the container will look for the data (e.g /recommender/signals.csv). The volume container can be based on a small linux docker image such as alpine. It must be a member of the same docker network of the container with the recommender (e.g. doeeet-net). The network must be created before the containers. 

    $ docker run -it -v /home/dataproducer/signals.csv:/recommender/signals.csv:ro --network=doeeet-net --name mahout-data alpine /bin/sh 

After the execution of the command you can see the file in the volume container in /recommender/signals.csv. You can leave the volume container typing Ctrl+P and Ctrl+Q. 

You can build the docker image with the recommender using the Dockerfile provided in this repository executing the command

    $ docker build -t doeeet/recommender:v0.1.0 .

After the image is built you can run it passing the name of the volume where it can reach the data and the name of the network it will be a member of.

    $ docker run -d -p 8100:8100 --volumes-from mahout-data --network=doeeet-net --name recommender doeeet/recommender:v0.1.0

## Use
You can send a request to the web service using the HTML page at http://localhost:8100/ with the id of the user for which you want the recommendation and the number of items to recommend, ore use curl

    $ curl 'http://localhost:8100/recommend?userID=21585&howMany=2'
    
The response is a JSON stream with a ranked list of recommended items 

    {
     "userID":21585,
     "recommendedItems": [
	      {
	        "itemID": "40526f54-3c60-4d87-9628-368524cff90c",
	        "value": 3.945368528366089
	      },
	      {
	        "itemID": "b352ac86-28ca-4c21-b586-c0edfa19ac0b",
	        "value": 3.4908947944641113
	      }
      ]
    }
    
In order to update the recommendations when a new signal file is available you can send the following request to the recommender

    $ curl 'http://localhost:8100/refresh'

The recommender will reply with a message containing the updated number of users and items.
 
## Version
0.1.0

## Troubleshooting
None

## License
Apache 2.0
