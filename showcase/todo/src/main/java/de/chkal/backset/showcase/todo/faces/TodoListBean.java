package de.chkal.backset.showcase.todo.faces;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.chkal.backset.showcase.todo.model.Item;
import de.chkal.backset.showcase.todo.service.TodoService;

@Named
@RequestScoped
public class TodoListBean {

  @Inject
  private TodoService todoService;

  private List<Item> items = null;

  @PostConstruct
  public void init() {
    items = todoService.getItems();
  }

  public List<Item> getItems() {
    return items;
  }

}
