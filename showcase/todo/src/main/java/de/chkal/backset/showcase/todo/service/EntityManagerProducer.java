package de.chkal.backset.showcase.todo.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ApplicationScoped
public class EntityManagerProducer {

  private EntityManagerFactory entityManagerFactory;

  @PostConstruct
  public void init() {
    entityManagerFactory = Persistence.createEntityManagerFactory("todo");
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
