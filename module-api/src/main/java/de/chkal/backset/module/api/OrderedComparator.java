package de.chkal.backset.module.api;

import java.util.Comparator;

public class OrderedComparator implements Comparator<Ordered> {

  public static final OrderedComparator INSTANCE = new OrderedComparator();

  @Override
  public int compare(Ordered o1, Ordered o2) {
    return Integer.valueOf(o1.getPriority()).compareTo(Integer.valueOf(o2.getPriority()));
  }

}
