# Dockerfile for the Doeeet Recommender (DR)
#
# 1) Build an image using this docker file. Run the following docker command
#
#    $ docker build -t eisbonn/recommender:v0.1.0 .
#
# 2) Test DR in a container. Run the following docker command for testing
#
#    $ docker run --rm -it -v /storage/mahout:/storage mahout -p 8100:8100 --network=doeeet-net --name recommender eisbonn/recommender:v0.1.0 /bin/bash
#
#    Check that the signal files are available in the docker volume
#
# 3) In order to run the container in a network (e.g. doeeet-net) use the command
#
#    docker run -d -v /storage/mahout:/storage/mahout -p 8100:8100 --network=doeeet-net --name recommender eisbonn/recommender:v0.1.0
#
# 4) The container can be inspected using the command
#
#    $ docker exec -it recommender /bin/bash


# Pull base image
FROM ubuntu
MAINTAINER Luigi Selmi <luigiselmi@gmail.com>

RUN apt-get update && apt-get -y install locales
RUN locale-gen en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

# Install Java 8.
RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y  software-properties-common && \
    add-apt-repository ppa:webupd8team/java -y && \
    apt-get update && \
    echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections && \
    apt-get install -y oracle-java8-installer && \
    apt-get clean

# Define JAVA_HOME environment variable
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

# Install  network tools (ifconfig, netstat, ping, ip)
RUN apt-get update && \
    apt-get install -y net-tools && \
    apt-get install -y iputils-ping && \
    apt-get install -y iproute2

# Install vi for editing
RUN apt-get update && \
    apt-get install -y vim

# Install Jetty
WORKDIR /tmp
RUN wget http://central.maven.org/maven2/org/eclipse/jetty/jetty-distribution/9.4.7.v20170914/jetty-distribution-9.4.7.v20170914.tar.gz && \
    tar xvf jetty-distribution-9.4.7.v20170914.tar.gz && \
    rm jetty-distribution-9.4.7.v20170914.tar.gz && \
    cd /opt && \
    mkdir frameworks && \
    cd frameworks && \
    mkdir jetty && \
    mv /tmp/jetty-distribution-9.4.7.v20170914 /opt/frameworks/jetty/jetty-9.4.7

# Create the folder for the data volume that will contain the signal files
WORKDIR /
RUN mkdir storage && \
    cd storage && \
    mkdir mahout

# Install the recommender web application
COPY target/doeeetrecommender.war /opt/frameworks/jetty/jetty-9.4.7/webapps/ROOT.war

# Start the doeeet recommender
WORKDIR /opt/frameworks/jetty/jetty-9.4.7
CMD java -jar start.jar jetty.http.port=8100
