package me.ziden.jplatformer.GameTimer;

import java.util.LinkedList;
import java.util.List;

public class GameTimer
{
  private List<GameTask> tasks = new LinkedList();
  
  public void addTask(GameTask t)
  {
    if (this.tasks.isEmpty()) {
      this.tasks.add(t);
    }
    if (t.executionTime > ((GameTask)this.tasks.get(this.tasks.size() - 1)).executionTime)
    {
      this.tasks.add(this.tasks.size(), t);
      return;
    }
    for (int index = 0; index < this.tasks.size(); index++) {
      if (((GameTask)this.tasks.get(index)).executionTime > t.executionTime)
      {
        this.tasks.add(index, t);
        break;
      }
    }
  }
  
  private List<GameTask> hasRunned = new LinkedList();
  
  public void tick()
  {
    if (this.tasks.size() == 0) {
      return;
    }
    for (int index = 0; index < this.tasks.size(); index++) {
      if (((GameTask)this.tasks.get(index)).executionTime <= System.currentTimeMillis())
      {
        ((GameTask)this.tasks.get(index)).run();
        this.hasRunned.add(this.tasks.get(index));
        if ((index != this.tasks.size() - 1) && 
          (((GameTask)this.tasks.get(index)).executionTime > System.currentTimeMillis())) {
          break;
        }
      }
    }
    for (GameTask gt : this.hasRunned) {
      this.tasks.remove(gt);
    }
    this.hasRunned.clear();
  }
}
