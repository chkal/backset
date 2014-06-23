package de.chkal.backset.module.test;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public class Deployments {

  public static Archive<?> getBacksetBase() {

    JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
        .addPackages(true, "de.chkal.backset.core")
        .addPackages(true, "de.chkal.backset.server")
        .addPackages(true, "de.chkal.backset.module.api");

    JavaArchive[] dependencies = Maven.resolver()
        .loadPomFromFile("pom.xml")
        .resolve(
            "io.undertow:undertow-core",
            "io.undertow:undertow-servlet",
            "org.slf4j:slf4j-simple",
            "org.reflections:reflections"
        )
        .withTransitivity()
        .as(JavaArchive.class);

    for (JavaArchive dependency : dependencies) {
      archive.merge(dependency);
    }

    return archive;

  }

  public static Archive<?> getBacksetMyFaces() {

    JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
        .addPackages(true, "de.chkal.backset.module.myfaces")
        .addAsServiceProvider("de.chkal.backset.module.api.Module",
            "de.chkal.backset.module.myfaces.MyFacesModule")
        .addAsServiceProvider("org.apache.myfaces.spi.AnnotationProvider",
            "de.chkal.backset.module.myfaces.MyFacesAnnotationProvider");

    JavaArchive[] dependencies = Maven.resolver()
        .loadPomFromFile("pom.xml")
        .resolve(
            "org.apache.myfaces.core:myfaces-impl",
            "de.odysseus.juel:juel-api",
            "de.odysseus.juel:juel-impl",
            "de.odysseus.juel:juel-spi"
        )
        .withTransitivity()
        .as(JavaArchive.class);

    for (JavaArchive dependency : dependencies) {
      archive.merge(dependency);
    }

    return archive;

  }

}
