package de.chkal.backset.module.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class OrderedComparatorTest {

  @Test
  public void shouldSortCorrecty() {

    SimpleOrdered first = new SimpleOrdered(5);
    SimpleOrdered second = new SimpleOrdered(10);

    List<Ordered> list = new ArrayList<>();
    list.add(first);
    list.add(second);

    Collections.sort(list, OrderedComparator.INSTANCE);

    assertThat(list)
        .containsSequence(first, second);

  }

  private static class SimpleOrdered implements Ordered {

    private final int priority;

    public SimpleOrdered(int priority) {
      this.priority = priority;
    }

    @Override
    public int getPriority() {
      return priority;
    }

    @Override
    public String toString() {
      return "SimpleOrdered [priority=" + priority + "]";
    }

  }

}
