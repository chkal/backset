package de.chkal.backset.module.bonecp;

import java.util.UUID;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;

import de.chkal.backset.module.api.Module;
import de.chkal.backset.module.api.ModuleContext;
import de.chkal.backset.module.bonecp.config.BoneConfig;
import de.chkal.backset.module.bonecp.config.BoneDatasource;

public class BoneCPModule implements Module {

  private final Logger log = LoggerFactory.getLogger(BoneCPModule.class);

  @Override
  public int getPriority() {
    return 20;
  }
  
  @Override
  public void init(ModuleContext context) {

    BoneConfig boneConfig = context.getConfigManager().getConfig(BoneConfig.class);
    if (boneConfig != null && boneConfig.getDatasources().size() > 0) {

      log.info("Found {} datasources in configuration file...",
          boneConfig.getDatasources().size());

      for (BoneDatasource boneDatasource : boneConfig.getDatasources()) {

        BoneCPDataSource dataSource = createDataSource(boneDatasource);
        DataSources.add(dataSource.getPoolName(), dataSource);

        if (boneDatasource.getJndiName() != null) {
          bindToJndi(boneDatasource.getJndiName(), dataSource);
        }

        log.info("Database connection pool created: {}", dataSource.getPoolName());

      }

    }

  }

  private void bindToJndi(String jndiName, BoneCPDataSource dataSource) {

    try {

      InitialContext initialContext = new InitialContext();
      initialContext.bind(jndiName, dataSource);

    } catch (NamingException e) {
      throw new IllegalStateException("Failed to bind DataSource to JNDI", e);
    }

  }

  private BoneCPDataSource createDataSource(BoneDatasource boneDatasource) {

    BoneCPConfig config = new BoneCPConfig();

    if (boneDatasource.getName() != null) {
      config.setPoolName(boneDatasource.getName());
    } else {
      config.setPoolName(UUID.randomUUID().toString());
    }

    config.setJdbcUrl(boneDatasource.getJdbcUrl());
    config.setUsername(boneDatasource.getUsername());
    config.setPassword(boneDatasource.getPassword());

    return new BoneCPDataSource(config);

  }

  @Override
  public void destroy() {
    for (BoneCPDataSource ds : DataSources.getAll()) {
      log.info("Shutting down database connection pool: {}", ds.getPoolName());
      ds.close();
    }
  }

}
