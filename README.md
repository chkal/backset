# Backset [![Build Status](https://travis-ci.org/chkal/backset.svg?branch=master)](https://travis-ci.org/chkal/backset)

The aim of Backset is to provide
[Spring Boot](http://projects.spring.io/spring-boot/)/[Dropwizard](http://dropwizard.io/)
like developer experience for Java EE.

The idea is that you create your web application based on the technologies
you are familiar with: CDI, JAX-RS, JSF, JPA and so on. But instead of deploying a WAR
to some application server, you simply run the Maven build which will produce a executable
uberjar. Now you can start you application like this:

    java -jar myapp.jar

You application will include **everything** required for running it. This makes
deploying and setting up a dev environment VERY easy and straight forward.

## Features

Currently Backset supports the following Java EE technologies:

  * Servlet 3.1
  * JSP 2.3
  * EL 3.0
  * JSF 2.2
  * CDI 1.2
  * JPA 2.1
  * JDBC DataSources via JNDI

The following technologies are currently on the roadmap:

  * JAX-RS
  * JTA
  * EJB

## Getting started

Backset is currently in early development stage. So there are some manual steps for you
to take if you want to give it a try.

First you should clone the source and build it:

```
$ git clone https://github.com/chkal/backset.git
$ cd backset && mvn -DskipTests install
```

The `showcase` directory contains some sample applications. The `todo` app is probably
the most complete one. It shows how to create a simple task manager based on
CDI, JSF and JPA.

You can start the `todo` application with a simple `java -jar` command:

```
$ java -jar showcase/todo/target/backset-showcase-todo-1.0-SNAPSHOT.jar
```

After the application has started, you can access it by opening the following URL in
your browser:

[http://localhost:8080/index.jsf](http://localhost:8080/index.jsf)

A Maven archetype is currently *work in progress*. So stay tuned.

## Core concepts

### Project structure

There are some important difference between classic web applications and a web application
built with Backset.

The first difference is that you will have to use `jar` instead of a `war` packaging for
your project. So your `pom.xml` will typically look like this:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" ....>
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.example</groupId>
  <artifactId>myapp</artifactId>
  <version>1.0-SNAPSHOT</version>

  <packaging>jar</packaging>

  <!-- ... more .... -->

</project>
```

The second important difference is that the content of your web application (JSPs, JavaScript, CSS, etc)
has to be placed in a directory called `webapp` on your classpath.

So you will **NOT** place your files in:

    src/main/webapp

Instead use:

    src/main/resources/webapp

So your project structure will typically look like this:

```
myapp
|-- pom.xml
`-- src
    `-- main
        |-- java
        |   `-- <your classes>
        `-- resources
            `-- webapp
                |-- index.jsp
                `-- WEB-INF
                    `-- web.xml
```

### Configuration

TODO

### Developing in your IDE

TODO

### Building executable JARs

Backset uses the [Maven Shade Plugin](http://maven.apache.org/plugins/maven-shade-plugin/)
to create an executable uberjar.

Just add the following ugly configuration snippet to your `pom.xml`.

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-shade-plugin</artifactId>
  <version>2.3</version>
  <executions>
    <execution>
      <phase>package</phase>
      <goals>
        <goal>shade</goal>
      </goals>
      <configuration>
        <createDependencyReducedPom>false</createDependencyReducedPom>
        <transformers>
          <transformer implementation="de.chkal.backset.maven.shade.DefaultRelocationsTransformer" />
          <transformer implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer" />
          <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
            <manifestEntries>
              <Main-Class>de.chkal.backset.server.Bootstrap</Main-Class>
            </manifestEntries>
          </transformer>
        </transformers>
      </configuration>
    </execution>
  </executions>
  <dependencies>
    <dependency>
      <groupId>de.chkal.backset</groupId>
      <artifactId>backset-maven-tools</artifactId>
      <version>${backset.version}</version>
    </dependency>
  </dependencies>
</plugin>
```

Now you can run this command to create the uberjar:

```
mvn package
```

You can then run your application using a simple `java -jar` command:

```
java -jar target/myapp.jar
```

Easy, isn't it. :)



