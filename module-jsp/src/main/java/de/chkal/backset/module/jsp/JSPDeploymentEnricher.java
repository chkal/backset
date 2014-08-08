package de.chkal.backset.module.jsp;

import io.undertow.jsp.HackInstanceManager;
import io.undertow.jsp.JspServletBuilder;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.ServletInfo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.jasper.deploy.JspPropertyGroup;
import org.apache.jasper.deploy.TagLibraryInfo;
import org.apache.tomcat.InstanceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.chkal.backset.module.api.ConfigManager;
import de.chkal.backset.module.api.DeploymentEnricher;
import de.chkal.backset.module.api.InstanceFactoryFactory;
import de.chkal.backset.module.jsp.config.JSPConfig;
import de.chkal.backset.module.jsp.xml.TaglibParser;

public class JSPDeploymentEnricher implements DeploymentEnricher {

  public static ConfigManager configManager;

  private final Logger log = LoggerFactory.getLogger(JSPDeploymentEnricher.class);

  private final TaglibParser taglibParser = new TaglibParser();

  @Override
  public int getPriority() {
    return 5;
  }

  @Override
  public void enrich(DeploymentInfo deployment, InstanceFactoryFactory factory) {

    InstanceManager instanceManager = new HackInstanceManager();

    HashMap<String, JspPropertyGroup> propertyGroups = new HashMap<String, JspPropertyGroup>();
    HashMap<String, TagLibraryInfo> tagLibraries = new HashMap<String, TagLibraryInfo>();

    for (String file : getTldFileLocations()) {
      TagLibraryInfo coreTagLib = parseTaglibDescriptor(file);
      if (coreTagLib != null) {
        log.debug("Registering taglib: {}", coreTagLib.getUri());
        tagLibraries.put(coreTagLib.getUri(), coreTagLib);
      }
    }

    JspServletBuilder.setupDeployment(deployment, propertyGroups, tagLibraries, instanceManager);

    ServletInfo jspServlet = JspServletBuilder.createServlet("JSPServlet", "*.jsp");

    deployment.addServlet(jspServlet);

  }

  private Iterable<String> getTldFileLocations() {

    List<String> files = new ArrayList<>();

    files.add("META-INF/c.tld");
    files.add("META-INF/x.tld");
    files.add("META-INF/fmt.tld");
    files.add("META-INF/sql.tld");
    files.add("META-INF/fn.tld");

    JSPConfig config = configManager.getConfig(JSPConfig.class);
    if (config != null && config.getTldFiles() != null) {
      files.addAll(config.getTldFiles());
    }

    return files;

  }

  private TagLibraryInfo parseTaglibDescriptor(String name) {

    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream stream = classLoader.getResourceAsStream(name);

    if (stream != null) {
      return taglibParser.parse(stream);
    }

    return null;

  }

}
