package me.ziden.jplatformer;

import java.util.ArrayList;
import java.util.List;

public class ThreadSafeList<Type>
{
  public boolean locked = false;
  private List<Type> theList = new ArrayList();
  private List<Type> buffer = new ArrayList();
  
  public void save()
  {
    this.locked = true;
  }
  
  public boolean isUpdated()
  {
    return this.locked;
  }
  
  public List<Type> getList()
  {
    if (this.locked)
    {
      this.theList.clear();
      this.theList.addAll(this.buffer);
      this.buffer.clear();
      this.locked = false;
    }
    return this.theList;
  }
  
  public void add(Type t)
  {
    this.buffer.add(t);
  }
}
