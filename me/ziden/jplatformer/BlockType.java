package me.ziden.jplatformer;

public enum BlockType
{
  STONE(2, 2),  DIRT(0, 1),  AIR(1, 0);
  
  private int id;
  private int priority;
  
  private BlockType(int id)
  {
    this.id = id;
  }
  
  private BlockType(int id, int priority)
  {
    this.id = id;
    this.priority = priority;
  }
  
  public int getId()
  {
    return this.id;
  }
  
  public int getPriorioty()
  {
    return this.priority;
  }
}
