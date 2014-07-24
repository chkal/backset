package de.chkal.backset.module.servlet.xml;

import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;

import java.io.InputStream;
import java.util.EventListener;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.chkal.backset.module.api.DeploymentEnricher;
import de.chkal.backset.module.servlet.xml.types.ListenerType;
import de.chkal.backset.module.servlet.xml.types.ParamValueType;
import de.chkal.backset.module.servlet.xml.types.WebAppType;

public class DescriptorDeploymentEnricher implements DeploymentEnricher {

  private final Logger log = LoggerFactory.getLogger(DescriptorDeploymentEnricher.class);

  private final JAXBContext context;

  private final ClassLoader classLoader;

  public DescriptorDeploymentEnricher() {

    classLoader = Thread.currentThread().getContextClassLoader();

    try {
      context = JAXBContext.newInstance(WebAppType.class.getPackage().getName());
    } catch (JAXBException e) {
      throw new IllegalStateException(e);
    }

  }

  @Override
  public void enrich(DeploymentInfo deployment) {

    InputStream webXmlStream = classLoader.getResourceAsStream("webapp/WEB-INF/web.xml");
    if (webXmlStream != null) {
      processWebXml(deployment, webXmlStream);
    }

  }

  protected void processWebXml(DeploymentInfo deployment, InputStream stream) {

    try {

      Unmarshaller unmarshaller = context.createUnmarshaller();
      JAXBElement<?> webappElement = (JAXBElement<?>) unmarshaller.unmarshal(stream);

      if (webappElement.getDeclaredType().equals(WebAppType.class)) {

        WebAppType webapp = (WebAppType) webappElement.getValue();
        processWebApp(deployment, webapp);

      }

    } catch (JAXBException e) {
      log.warn("Failed to parse descriptor", e);
    }

  }

  private void processWebApp(DeploymentInfo deployment, WebAppType webapp) {

    for (JAXBElement<?> jaxbElement : webapp.getModuleNameOrDescriptionAndDisplayName()) {

      // <context-param>
      if (jaxbElement.getDeclaredType().equals(ParamValueType.class)) {
        processContextParam(deployment, (ParamValueType) jaxbElement.getValue());
      }

      // <listener>
      if (jaxbElement.getDeclaredType().equals(ListenerType.class)) {
        processListener(deployment, (ListenerType) jaxbElement.getValue());
      }

    }

  }

  private void processListener(DeploymentInfo deployment, ListenerType value) {
    deployment.addListener(
        Servlets.listener(
            loadClass(value.getListenerClass().getValue(), EventListener.class)));
  }

  @SuppressWarnings("unchecked")
  private <T> Class<T> loadClass(String name, Class<T> expectedType) {
    try {
      return (Class<T>) classLoader.loadClass(name);
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException("Cannot load class: " + name);
    }
  }

  private void processContextParam(DeploymentInfo deployment, ParamValueType param) {
    deployment.addInitParameter(
        param.getParamName().getValue(),
        param.getParamValue().getValue());
  }

}
