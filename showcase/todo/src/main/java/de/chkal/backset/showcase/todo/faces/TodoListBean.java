package de.chkal.backset.showcase.todo.faces;

import de.chkal.backset.showcase.todo.model.Item;
import de.chkal.backset.showcase.todo.service.TodoService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class TodoListBean {

  @Inject
  private TodoService todoService;

  private List<Item> items = null;

  @PostConstruct
  public void fetchItemList() {
    items = todoService.getItems();
  }

  public void toggleDone(Item item) {
    todoService.toggleDone(item.getId());
    fetchItemList();
  }

  public void delete(Long id) {
    todoService.delete(id);
    fetchItemList();
  }

  public List<Item> getItems() {
    return items;
  }
}
