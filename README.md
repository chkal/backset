# Backset [![Build Status](https://travis-ci.org/chkal/backset.svg?branch=master)](https://travis-ci.org/chkal/backset)

The aim of Backset is to provide
[Spring Boot](http://projects.spring.io/spring-boot/)/[Dropwizard](http://dropwizard.io/)
like developer experience for Java EE.

## Index

* [Overview](#overview)
* [Getting started](#getting-started)
* [Core concepts](#core-concepts)
  * [Project structure](#project-structure)
  * [Required Dependencies](#required-dependencies)
  * [Developing in your IDE](#developing-in-your-ide)
  * [Building executable JARs](#building-executable-jars)
  * [Managing Configuration](#configuration)
  * [Configuration Options](#configuration-options)
    * [Logging](#logging)
    * [Connectors](#connectors)
* [Modules](#modules)
  * [Servlet](#servlet)
  * [Weld](#weld)
  * [BoneCP](#bonecp)
  * [Hibernate](#hibernate)
  * [Jersey](#jersey)
  * [MyFaces](#myfaces)
  * [JSP](#jsp)

## Overview

The idea is that you create your web application based on the technologies
you are familiar with: CDI, JAX-RS, JSF, JPA and so on. But instead of deploying a WAR
to some application server, you simply run the Maven build which will produce a executable
uberjar. Now you can start you application like this:

    java -jar myapp.jar

You application will include **everything** required for running it. This makes
deploying and setting up a dev environment VERY easy and straight forward.

Currently Backset supports the following Java EE technologies:

  * Servlet 3.1
  * JSP 2.3
  * EL 3.0
  * CDI 1.2
  * JAX-RS 2.0
  * JPA 2.1
  * JSF 2.2
  * JDBC DataSources via JNDI

The following technologies are currently on the roadmap:

  * JTA
  * EJB

## Getting started

Backset is currently in early development stage. The latest version available in Maven Central
is `1.0.0.Alpha1`. It's fine to use this version for your first experiments. If you want to give
the bleeding edge version a try, you will have to build it yourself:

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

### Required Dependencies

To use Backset in your project, you will have to add the following dependencies to your `pom.xml`

```xml
<!-- The core Backset dependency -->
<dependency>
  <groupId>de.chkal.backset</groupId>
  <artifactId>backset-server</artifactId>
  <version>${backset.version}</version>
</dependency>

<!-- All the modules are optional -->
<dependency>
  <groupId>de.chkal.backset</groupId>
  <artifactId>backset-module-?????</artifactId>
  <version>${backset.version}</version>
</dependency>
```

So you basically need to add `backset-server` and all the modules you would like to use. Have a look at the
[Modules section](#modules) for more details about the modules available.

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
2. You can provide a YAML configuration file when starting Backset by specifying it
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

### Configuration Options

The following sub sections will describe the core configuration options of Backset. Please
note that there are many more configuration options for specific modules which are
described in the corresponding module sections.

#### Logging

The first thing you will typically have to configure is the logging. Backset uses Logback for
logging. In most cases you can use a configuration like this configure the logging:

```yaml
logging:
  level: INFO
  loggers:
    de.chkal.backset: DEBUG
    com.acme.myapp: TRACE
```

This will set the default logging level to `INFO` which is usually a good choice. Additionally you
can change the log level for individual loggers. To do so, just type the package name under the
`loggers` key and set the log level accordingly.

Future versions may support to reference a `logback.xml` file which contains full Logback
configuration. Any patches welcome. ;)

#### Connectors

Backset uses Undertow as the embedded web container. You can configure some core features of Undertow
using the `undertow` configuration section.

A full configuration example looks like this:

```yaml
undertow:
  ioThreads: 50
  workerThreads: 50
  connectors:
    - type: http
      port: 8080
    - type: ajp
      host: 127.0.0.1
      port: 8009
  contextParams:
    javax.faces.PROJECT_STAGE: Development
    com.acme.myapp.SOMETHING: Some value
```

First you can control the number of IO and worker threads. If you don't specify these values, Backset
will use Undertow's defaults.

The `connectors` elements allows you to define a list of connectors. Each connector has a type, a host
address and a port. The type is either `http` or `ajp`. The host address allows you to control which
network interface Undertow will use for this connector. In the example above, the AJP connector is bound
to the local interface, so that connections are only possible from the same host. If you don't specify the
host address, Backset will use `0.0.0.0`. The third property of a connector is the port. If you don't specify
the port, Backset will use 8080 or 8009 depending on the port type.

In some situations it may be useful to set servlet context parameters depending on your runtime environment.
This will for example allow you to put JSF into development mode while developing without having to add
the context parameter to your main `web.xml`. You can use the `contextParams` element to specify a
mapping from context parameter keys to their values.

## Modules

This sections will describe the optional Backset modules which you can add to your project to get
support for different Java EE technologies.

### Servlet

Most applications should add the Backset servlet module by adding it to their
`pom.xml` like this:

```xml
<dependency>
  <groupId>de.chkal.backset</groupId>
  <artifactId>backset-module-servlet</artifactId>
  <version>${backset.version}</version>
</dependency>
```

This modules provides the following features:

 * Discovery and parsing of `web.xml` and `web-fragment.xml` files.
 * Discovery and registration of classes annotated with `@WebServlet`, `@WebFilter`
   and similar annotations.

### Weld

Backset uses [JBoss Weld](http://weld.cdi-spec.org/) as its CDI implementation. If you want
to use CDI in your application, add the following dependency:

```xml
<dependency>
  <groupId>de.chkal.backset</groupId>
  <artifactId>backset-module-weld</artifactId>
  <version>${backset.version}</version>
</dependency>
```

There is currently one problem with the CDI support in Backset. Standard application servers use the
`/META-INF/beans.xml` file to identify JAR files for which CDI should be enabled. As Backset merges
all JAR files into a big fat JAR, this won't work very well.

For this reason you currently have to tell Backset which packages should be considered to contain
CDI beans. In a typical application this will be your root application package and the root package
names of all CDI extension libraries your are using.

See this configuration for an example:

```yml
weld:
  packages:
    - com.acme.myapp
    - org.apache.deltaspike
```

I'm currently looking for other ways to fix this problem. However, specifying the packages this way
manually seems to work fine for most applications.

### BoneCP

Backset uses [BoneCP](http://jolbox.com/) as the JDBC connection pool. If you want to access your
database via JDBC, you should add this module to your `pom.xml`.

```xml
<dependency>
  <groupId>de.chkal.backset</groupId>
  <artifactId>backset-module-bonecp</artifactId>
  <version>${backset.version}</version>
</dependency>
```

The BoneCP module allows you to define datasources in your Backset configuration. A full example
looks like this:

```yaml
bonecp:
  datasources:
    - name: 'TestDataSource'
      jndiName: 'java:/comp/env/TestDataSource'
      driverClass: 'org.h2.Driver'
      jdbcUrl: 'jdbc:h2:mem:simple-test'
      username: 'sa'
      password: ''
```

Each datasource has a `name` and a `jndiName`. Both are optional, but you should set at least one of them.
If you set the name, you can get a reference to the `java.sql.DataSource` using Backset's `DataSources`
class which offers static methods to lookup datasources by name. If you set the jndiName, you will
be able lookup the datasource from JNDI. This is especially useful if you are using JPA and want
to use `<non-jta-data-source>` in your `persistence.xml` to refer to the datasource.

The other properties are straight forward. For each data source you have to define the name of the
JDBC driver class, the JDBC URL and the credentials required to connect to the database.

### Hibernate

There is currently no Hibernate module in Backset. But the reason for this is, that there is no
integration code required to use Hibernate. So you can simply add the Hibernate dependency directly:

```xml
<dependency>
  <groupId>org.hibernate</groupId>
  <artifactId>hibernate-entitymanager</artifactId>
  <version>${hibernate.version}</version>
</dependency>
```

Now you can add a `persistence.xml` file to your project like you would do with a standard application.
An `persistence.xml` could look like this:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.0" xmlns="http://java.sun.com/xml/ns/persistence">

  <persistence-unit name="myapp" transaction-type="RESOURCE_LOCAL">

    <non-jta-data-source>java:/comp/env/TestDataSource</non-jta-data-source>

    <!-- Add your entities here -->
    <class>de.chkal.backset.showcase.todo.model.Item</class>

  </persistence-unit>

</persistence>
```

Easy, isn't it? Please refer to the [BoneCP](#bonecp) section for details on how to create a JDBC datasource.

Now you will typically just create a CDI bean which manages the lifecycle of the `EntityManager`.
Such an class could look like this:

```java
@ApplicationScoped
public class EntityManagerProducer {

  private EntityManagerFactory entityManagerFactory;

  @PostConstruct
  public void init() {
    entityManagerFactory = Persistence.createEntityManagerFactory("myapp");
  }

  @Produces
  @RequestScoped
  public EntityManager getEntityManager() {
    return entityManagerFactory.createEntityManager();
  }

  public void disposeEntityManager(@Disposes EntityManager entityManager) {
    entityManager.close();
  }

  @PreDestroy
  public void shutdown() {
    entityManagerFactory.close();
  }

}
```

This setup will create one `EntityManager` instance for each request. You can now simply inject the
`EntityManager` into any of your CDI beans like this:

```java
@ApplicationScoped
public class TodoService {

  @Inject
  private EntityManager entityManager;

  /* more code */

}
```

If you want to use declarative transaction management, you should consider using the
[Apache DeltaSpike JPA Module](http://deltaspike.apache.org/documentation/jpa.html). See the `todo` showcase
application for a detailed example.

Backset may add support for `@PersistenceContext` and `@PersistenceUnit` at a later point in time.

### Jersey

Backset uses [Jersey](https://jersey.java.net/) as the JAX-RS implementation. So if you want
to create JAX-RS services, add the Jersey module to your `pom.xml`.

```xml
<dependency>
  <groupId>de.chkal.backset</groupId>
  <artifactId>backset-module-jersey</artifactId>
  <version>${backset.version}</version>
</dependency>
```

There is no additional configuration required for the Jersey module. Just create you application
class and your resources classes and they will be picked up automatically.

### MyFaces

Backset uses [Apache MyFaces](http://myfaces.apache.org/) as the JSF implementation. To use JSF
in your application, add this module to your `pom.xml`:

```xml
<dependency>
  <groupId>de.chkal.backset</groupId>
  <artifactId>backset-module-myfaces</artifactId>
  <version>${backset.version}</version>
</dependency>
```

There is no additional configuration required for this module.

### JSP

If you want to use JSP files in your application, add this module to your `pom.xml`:

```xml
<dependency>
  <groupId>de.chkal.backset</groupId>
  <artifactId>backset-module-jsp</artifactId>
  <version>${backset.version}</version>
</dependency>
```

This module uses Jastow under the hood. You can register custom TLD files using a configuration like
this:

```yaml
jsp:
  tldFiles:
    - foobar.tld
```

