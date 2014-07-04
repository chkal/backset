package de.chkal.backset.module.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import de.chkal.backset.module.test.merge.SimpleArchiveMerger;

public class BacksetBundleBuilder {

  private final JavaArchive application;

  private final JavaArchive backset;

  private final List<String> dependencies = new ArrayList<>();

  private final Multimap<String, String> serviceProviders = LinkedListMultimap.create();

  private BacksetBundleBuilder(JavaArchive archive) {

    this.application = archive;
    this.backset = ShrinkWrap.create(JavaArchive.class);

    backset
        .addPackages(true, "de.chkal.backset.core")
        .addPackages(true, "de.chkal.backset.server")
        .addPackages(true, "de.chkal.backset.module.api");

    dependencies.addAll(Arrays.asList(
        "io.undertow:undertow-core",
        "io.undertow:undertow-servlet",
        "org.slf4j:slf4j-simple",
        "org.reflections:reflections",
        "com.fasterxml.jackson.core:jackson-databind",
        "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml"
        ));

  }

  public static BacksetBundleBuilder create(JavaArchive archive) {
    return new BacksetBundleBuilder(archive);
  }

  public BacksetBundleBuilder withMyFacesModule() {

    backset.addPackages(true, "de.chkal.backset.module.myfaces");

    serviceProviders.put("de.chkal.backset.module.api.Module",
        "de.chkal.backset.module.myfaces.MyFacesModule");
    serviceProviders.put("org.apache.myfaces.spi.AnnotationProvider",
        "de.chkal.backset.module.myfaces.MyFacesAnnotationProvider");

    dependencies.addAll(Arrays.asList(
        "org.apache.myfaces.core:myfaces-impl",
        "de.odysseus.juel:juel-api",
        "de.odysseus.juel:juel-impl",
        "de.odysseus.juel:juel-spi"
        ));

    return this;

  }

  public BacksetBundleBuilder withOpenWebBeansModule() {

    backset.addPackages(true, "de.chkal.backset.module.owb");

    serviceProviders.put("de.chkal.backset.module.api.Module",
        "de.chkal.backset.module.owb.OpenWebBeansModule");

    dependencies.addAll(Arrays.asList(
        "javax.enterprise:cdi-api",
        "org.apache.openwebbeans:openwebbeans-spi",
        "org.apache.openwebbeans:openwebbeans-impl",
        "org.apache.openwebbeans:openwebbeans-web",
        "org.apache.openwebbeans:openwebbeans-el22",
        "de.odysseus.juel:juel-api",
        "de.odysseus.juel:juel-impl",
        "de.odysseus.juel:juel-spi"
        ));

    return this;

  }

  public BacksetBundleBuilder withWeldModule() {

    backset.addPackages(true, "de.chkal.backset.module.weld");

    serviceProviders.put("de.chkal.backset.module.api.Module",
        "de.chkal.backset.module.weld.WeldModule");
    serviceProviders.put("org.jboss.weld.environment.Container",
        "de.chkal.backset.module.weld.BacksetContainer");
    serviceProviders.put("javax.faces.application.ApplicationFactory",
        "org.jboss.weld.environment.servlet.jsf.WeldApplicationFactory");

    dependencies.addAll(Arrays.asList(
        "javax.enterprise:cdi-api",
        "org.jboss.weld:weld-core",
        "org.jboss.weld.servlet:weld-servlet-core",
        "javax.servlet.jsp:javax.servlet.jsp-api",
        "io.undertow.jastow:jastow",
        "de.odysseus.juel:juel-api",
        "de.odysseus.juel:juel-impl",
        "de.odysseus.juel:juel-spi"
        ));

    return this;

  }

  public JavaArchive build() {

    for (Entry<String, Collection<String>> entry : serviceProviders.asMap().entrySet()) {
      String iface = entry.getKey();
      String[] impls = entry.getValue().toArray(new String[0]);
      backset.addAsServiceProvider(iface, impls);
    }

    JavaArchive[] other = Maven.resolver()
        .loadPomFromFile("pom.xml")
        .resolve(dependencies)
        .withTransitivity()
        .as(JavaArchive.class);

    return SimpleArchiveMerger.create(application)
        .merge(backset)
        .merge(other)
        .getResult();

  }

}
