package me.ziden.jplatformer.GameTimer;

public abstract class GameTask
  implements Runnable
{
  long executionTime;
  
  public GameTask(long milliseconds)
  {
    this.executionTime = (System.currentTimeMillis() + milliseconds);
  }
}
