package de.chkal.backset.showcase.todo.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import de.chkal.backset.showcase.todo.model.Item;

@ApplicationScoped
public class TodoService {

  @Inject
  private EntityManager entityManager;

  public List<Item> getItems() {
    return entityManager.createQuery("SELECT i FROM Item i", Item.class)
        .getResultList();
  }

  public void addItem(Item item) {

    try {

      entityManager.getTransaction().begin();
      entityManager.persist(item);
      entityManager.getTransaction().commit();

    } catch (RuntimeException e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      throw e;
    }
  }

}
