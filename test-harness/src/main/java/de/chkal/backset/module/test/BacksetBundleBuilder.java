package de.chkal.backset.module.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import de.chkal.backset.module.test.merge.ShadePluginArchiveMerger;

public class BacksetBundleBuilder {

  private final JavaArchive application;

  private final JavaArchive backset;

  private final List<Archive<?>> archives = new ArrayList<>();

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
        "org.slf4j:slf4j-api",
        "org.slf4j:jul-to-slf4j",
        "ch.qos.logback:logback-classic",
        "org.reflections:reflections",
        "com.fasterxml.jackson.core:jackson-databind",
        "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml",
        "commons-daemon:commons-daemon"
        ));
    
    // always add this one
    withELModule();

  }

  public static BacksetBundleBuilder create(JavaArchive archive) {
    return new BacksetBundleBuilder(archive);
  }

  public BacksetBundleBuilder withArchives(JavaArchive... list) {
    for (JavaArchive dep : list) {
      this.archives.add(dep);
    }
    return this;
  }

  public BacksetBundleBuilder withMavenDependency(String coords) {
    dependencies.add(coords);
    return this;
  }

  public BacksetBundleBuilder withServletModule() {

    backset.addPackages(true, "de.chkal.backset.module.servlet");

    serviceProviders.put("de.chkal.backset.module.api.Module",
        "de.chkal.backset.module.servlet.ServletModule");

    return this;

  }

  public BacksetBundleBuilder withJSPModule() {

    backset.addPackages(true, "de.chkal.backset.module.jsp");

    serviceProviders.put("de.chkal.backset.module.api.Module",
        "de.chkal.backset.module.jsp.JSPModule");

    dependencies.addAll(Arrays.asList(
        "io.undertow.jastow:jastow",
        "javax.servlet:jstl",
        "javax.servlet.jsp:javax.servlet.jsp-api",
        "org.eclipse.jdt.core.compiler:ecj"
        ));

    return this;

  }

  private BacksetBundleBuilder withELModule() {

    dependencies.addAll(Arrays.asList(
        "javax.el:javax.el-api",
        "org.glassfish:javax.el"
        ));

    return this;

  }

  public BacksetBundleBuilder withMyFacesModule() {

    backset.addPackages(true, "de.chkal.backset.module.myfaces");

    serviceProviders.put("de.chkal.backset.module.api.Module",
        "de.chkal.backset.module.myfaces.MyFacesModule");
    serviceProviders.put("org.apache.myfaces.spi.AnnotationProvider",
        "de.chkal.backset.module.myfaces.BacksetAnnotationProvider");
    serviceProviders.put("org.apache.myfaces.spi.FacesConfigResourceProvider",
        "de.chkal.backset.module.myfaces.BacksetFacesConfigResourceProvider");

    dependencies.addAll(Arrays.asList(
        "org.apache.myfaces.core:myfaces-impl"
        ));

    return this;

  }


  public BacksetBundleBuilder withJerseyModule() {

    backset.addPackages(true, "de.chkal.backset.module.jersey");

    dependencies.addAll(Arrays.asList(
        "org.glassfish.jersey.containers:jersey-container-servlet"
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
        "org.apache.openwebbeans:openwebbeans-el22"
        ));

    return this;

  }

  public BacksetBundleBuilder withWeldModule() {

    backset.addPackages(true, "de.chkal.backset.module.weld");

    backset.addAsManifestResource("META-INF/faces-config.xml", "faces-config.xml");

    serviceProviders.put("de.chkal.backset.module.api.Module",
        "de.chkal.backset.module.weld.WeldModule");
    serviceProviders.put("javax.enterprise.inject.spi.CDIProvider",
        "de.chkal.backset.module.weld.BacksetCDIProvider");

    dependencies.addAll(Arrays.asList(
        "javax.enterprise:cdi-api",
        "org.jboss.weld:weld-core-impl",
        "org.jboss.weld:weld-core-jsf",
        "javax.servlet.jsp:javax.servlet.jsp-api",
        "io.undertow.jastow:jastow"
        ));

    return this;

  }

  public BacksetBundleBuilder withBoneCPModule() {

    backset.addPackages(true, "de.chkal.backset.module.bonecp");

    serviceProviders.put("de.chkal.backset.module.api.Module",
        "de.chkal.backset.module.bonecp.BoneCPModule");

    dependencies.addAll(Arrays.asList(
        "com.jolbox:bonecp"
        ));

    return this;

  }

  public BacksetBundleBuilder withJNDIModule() {

    backset.addPackages(true, "de.chkal.backset.module.jndi");

    serviceProviders.put("de.chkal.backset.module.api.Module",
        "de.chkal.backset.module.jndi.JNDIModule");

    dependencies.addAll(Arrays.asList(
        "org.glassfish.main.web:web-naming"
        ));

    return this;

  }

  public JavaArchive build() {

    for (Entry<String, Collection<String>> entry : serviceProviders.asMap().entrySet()) {
      String iface = entry.getKey();
      String[] impls = entry.getValue().toArray(new String[0]);
      backset.addAsServiceProvider(iface, impls);
    }

    JavaArchive[] artifacts = Maven.resolver()
        .loadPomFromFile("pom.xml")
        .resolve(dependencies)
        .withTransitivity()
        .as(JavaArchive.class);

    return ShadePluginArchiveMerger.create(application)
        .merge(archives)
        .merge(backset)
        .merge(artifacts)
        .getResult();

  }

}
