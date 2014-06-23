package de.chkal.backset.arquillian;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jboss.arquillian.container.spi.client.container.DeployableContainer;
import org.jboss.arquillian.container.spi.client.container.DeploymentException;
import org.jboss.arquillian.container.spi.client.container.LifecycleException;
import org.jboss.arquillian.container.spi.client.protocol.ProtocolDescription;
import org.jboss.arquillian.container.spi.client.protocol.metadata.HTTPContext;
import org.jboss.arquillian.container.spi.client.protocol.metadata.ProtocolMetaData;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.descriptor.api.Descriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.chkal.backset.server.Bootstrap;

public class BacksetDeployableContainer implements DeployableContainer<BacksetContainerConfiguration> {

  private final Logger log = LoggerFactory.getLogger(BacksetDeployableContainer.class);

  @SuppressWarnings("unused")
  private BacksetContainerConfiguration configuration;

  private File archiveFile;

  private Process process;

  @Override
  public Class<BacksetContainerConfiguration> getConfigurationClass() {
    return BacksetContainerConfiguration.class;
  }

  @Override
  public ProtocolDescription getDefaultProtocol() {
    return new ProtocolDescription("Servlet 3.0");
  }

  @Override
  public void setup(BacksetContainerConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public void start() throws LifecycleException {
    // see deploy()
  }

  @Override
  public ProtocolMetaData deploy(Archive<?> archive) throws DeploymentException {

    archiveFile = createUniqueTempFile();

    archive.as(ZipExporter.class).exportTo(archiveFile);
    log.info("Archive exported to: {}", archiveFile.getAbsolutePath());

    List<String> cmd = new ArrayList<>();
    cmd.add(getJavaBinary());
    cmd.add("-cp");
    cmd.add(archiveFile.getAbsolutePath());
    cmd.add(Bootstrap.class.getName());
    log.info("Executing: " + cmd.toString());

    try {

      ProcessBuilder processBuilder = new ProcessBuilder(cmd);
      processBuilder.redirectErrorStream(true);
      process = processBuilder.start();

      new Thread(new ConsoleConsumer()).start();

      Runtime.getRuntime().addShutdownHook(new Thread(new ContainerShutdown()));

      long timeLeft = 30 * 1000;
      boolean serverStarted = false;
      while (timeLeft > 0 && !serverStarted) {

        long start = System.currentTimeMillis();

        if (isServerStarted()) {
          serverStarted = true;
          break;
        }

        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        timeLeft -= (System.currentTimeMillis() - start);

      }

      if (!serverStarted) {
        // TODO: kill server
        throw new IllegalStateException("Cound not start server in timeout interval");
      }

      ProtocolMetaData metaData = new ProtocolMetaData();
      metaData.addContext(new HTTPContext("foobar", "localhost", 8080));
      return metaData;

    } catch (IOException e) {
      throw new DeploymentException(e.getMessage(), e);
    }

  }

  private boolean isServerStarted() {

    CloseableHttpClient client = null;
    try {

      client = HttpClientBuilder.create().build();
      HttpGet get = new HttpGet("http://localhost:8080/");
      CloseableHttpResponse response = client.execute(get);
      log.info("Got response code: " + response.getStatusLine().getStatusCode());
      return true;

    } catch (IOException e) {
      log.debug("Got: " + e.getMessage());
    }
    return false;

  }

  private File createUniqueTempFile() {
    String tmpDir = System.getProperty("java.io.tmpdir");
    String jarFile = UUID.randomUUID().toString() + ".jar";
    return Paths.get(tmpDir, jarFile).toFile();
  }

  private String getJavaBinary() {
    return Paths.get(System.getProperty("java.home"), "bin", "java").toFile().getAbsolutePath();
  }

  @Override
  public void undeploy(Archive<?> archive) throws DeploymentException {

    log.info("Undeploying: " + archive.getName());

    process.destroy();
    try {
      process.waitFor();
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    }

    if (archiveFile != null && archiveFile.exists()) {
      archiveFile.delete();
    }

  }

  @Override
  public void stop() throws LifecycleException {
  }

  @Override
  public void deploy(Descriptor descriptor) throws DeploymentException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void undeploy(Descriptor arg0) throws DeploymentException {
    throw new UnsupportedOperationException();
  }

  private class ConsoleConsumer implements Runnable {
    @Override
    public void run() {

      final InputStream stream = process.getInputStream();

      try {

        byte[] buf = new byte[32];
        int num;
        while ((num = stream.read(buf)) != -1) {
          System.out.write(buf, 0, num);
        }

      } catch (IOException e) {
      }

    }
  }

  private class ContainerShutdown implements Runnable {
    @Override
    public void run() {
      log.info("Shutdown hook!");
      if (process != null) {
        log.info("Trying to force server shutdown...");
        process.destroy();
        try {
          int code = process.waitFor();
          log.info("Process exit with code: " + code);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

}
