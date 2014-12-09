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


### Developing in your IDE

Running a Backset application in your IDE is very easy. The nice thing about Backset is that
your application ships with the server. So you just have to point your IDE to the Backset
main class which contains an ordinary `main()` method.

In case of Eclipse simply import the project into your workspace and create a new
*Run Configuration* like this:

![Eclipse Run Configuration](http://i.imgur.com/xRfakSj.png)

Just make sure to select the correct *Main class*:

    de.chkal.backset.server.Bootstrap

Now click the *Run* button and your application will start up.

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

Easy, isn't it? :)

### Configuration

Backset uses [YAML](http://www.yaml.org/) files for configuration. A typically configuration
file looks like this.

```yaml
undertow:
  connectors:
    - type: http
      port: 8080
logging:
  level: INFO
  loggers:
    de.chkal.backset: DEBUG
```

The top-level elements in the configuration files are called *sections*
(`undertow` and `logging` in this case). Typically each component and module defines
its own section.

There are basically two ways to provide configuration parameters:

1. Backset will looks for a file called `backset.yml` on your classpath.
2. You can provide a YAML configuration file when starting Backset by specifiyng it
   as the first command line argument (example: `java -jar myapp.jar config.yml`).

When a component or module tries to access a section from the configuration, Backset performs
the following steps:

1. If a config file has been specified on the command line and it contains
   the relevant section, use this section.
2. If there is a `backset.yml` on the classpath and it contains the relevant
   section, use this section.
3. Use the fallbacks as specified by the module or component.

This allows you to specify a fallback configuration using a `backset.yml` on your classpath
which can be overwritten by a custom configuration specified on the commandline.
