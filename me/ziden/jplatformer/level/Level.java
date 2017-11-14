package me.ziden.jplatformer.level;

import java.awt.Graphics2D;
import me.ziden.jplatformer.Bitmaps;
import me.ziden.jplatformer.Block;
import me.ziden.jplatformer.BlockType;
import me.ziden.jplatformer.InputHandler;
import me.ziden.jplatformer.Map;
import me.ziden.jplatformer.Player;

public class Level
{
  private Map map;
  private Player player;
  private InputHandler input;
  private Block selectedBlock;
  private int xOffset = 0;
  private int yOffset = 0;
  
  public Level(int mapsize)
  {
    this.map = new Map(mapsize);
    this.player = new Player();
    this.map.addPlayer(this.player);
    
    this.selectedBlock = new Block(BlockType.DIRT);
  }
  
  public void tick()
  {
    this.map.tick();
    this.input.checkPlayer(this.player, this.map);
  }
  
  public void render(Graphics2D g, Bitmaps bitmaps)
  {
    this.xOffset = ((int)this.player.getX() - 256);
    this.yOffset = ((int)this.player.getY() - 160);
    this.map.render(g, bitmaps, this.xOffset, this.yOffset);
  }
  
  public InputHandler getInput()
  {
    return this.input;
  }
  
  public void setInput(InputHandler input)
  {
    this.input = input;
  }
  
  public Map getMap()
  {
    return this.map;
  }
  
  public void setMap(Map map)
  {
    this.map = map;
  }
  
  public Player getPlayer()
  {
    return this.player;
  }
  
  public void setPlayer(Player player)
  {
    this.player = player;
  }
  
  public int getxOffset()
  {
    return this.xOffset;
  }
  
  public void setxOffset(int xOffset)
  {
    this.xOffset = xOffset;
  }
  
  public int getyOffset()
  {
    return this.yOffset;
  }
  
  public void setyOffset(int yOffset)
  {
    this.yOffset = yOffset;
  }
  
  public Block getSelectedBlock()
  {
    return this.selectedBlock;
  }
}
