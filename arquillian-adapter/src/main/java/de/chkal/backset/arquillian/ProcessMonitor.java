package de.chkal.backset.arquillian;

public class ProcessMonitor implements Runnable {

  private final Process process;

  private boolean finished = false;

  public ProcessMonitor(Process process) {
    this.process = process;
  }

  @Override
  public void run() {

    try {

      process.waitFor();
      this.finished = true;

    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    }

  }

  public boolean isFinished() {
    return finished;
  }

}

