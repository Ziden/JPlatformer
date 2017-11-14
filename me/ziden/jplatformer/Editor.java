package me.ziden.jplatformer;

import java.awt.Graphics2D;
import me.ziden.jplatformer.level.Level;

public class Editor
{
  public Level level;
  public InputHandler input;
  public final int levelSize;
  
  public Editor(int levelSize)
  {
    this.levelSize = levelSize;
  }
  
  public void tick()
  {
    this.level.tick();
  }
  
  public void render(Graphics2D g, Bitmaps bitmaps)
  {
    this.level.render(g, bitmaps);
  }
  
  public void setInput(InputHandler input)
  {
    this.input = input;
  }
}
