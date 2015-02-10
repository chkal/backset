package de.chkal.backset.showcase.todo.service;

import de.chkal.backset.showcase.todo.model.Item;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
@Transactional
public class TodoService {

  @Inject
  private EntityManager entityManager;

  public List<Item> getItems() {
    return entityManager.createQuery("SELECT i FROM Item i", Item.class)
        .getResultList();
  }

  public void addItem(Item item) {
    entityManager.persist(item);
  }

  public Item toggleDone(long id) {
    Item item = entityManager.find(Item.class, id);
    if (item != null) {
      item.setDone(!item.isDone());
      return item;
    }
    return null;
  }

  public void delete(Long id) {
    Item item = entityManager.find(Item.class, id);
    if (item != null) {
      entityManager.remove(item);
    }
  }

}
