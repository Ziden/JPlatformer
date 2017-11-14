package me.ziden.jplatformer;

public class Border
{
  public int x;
  public int y;
  public BlockType tileIndex;
  public int direction;
  
  public Border(int x, int y, int d, BlockType tileIndex)
  {
    this.x = x;
    this.y = y;
    this.direction = d;
    this.tileIndex = tileIndex;
  }
}
