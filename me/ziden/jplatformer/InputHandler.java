package me.ziden.jplatformer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.HashSet;
import me.ziden.jplatformer.level.Level;
import me.ziden.jplatformer.sound.Audio;

public class InputHandler
  implements KeyListener, MouseListener, MouseMotionListener
{
  private Level level;
  
  public InputHandler(JPlatformer parent, Level level)
  {
    parent.addKeyListener(this);
    parent.addMouseListener(this);
    parent.addMouseMotionListener(this);
    this.level = level;
  }
  
  private HashSet<Integer> keysOn = new HashSet();
  public float xMouse;
  public float yMouse;
  
  public void keyTyped(KeyEvent e) {}
  
  public void keyPressed(KeyEvent e)
  {
    this.keysOn.add(Integer.valueOf(e.getKeyCode()));
  }
  
  public void keyReleased(KeyEvent e)
  {
    if (this.keysOn.contains(Integer.valueOf(e.getKeyCode()))) {
      this.keysOn.remove(Integer.valueOf(e.getKeyCode()));
    }
  }
  
  public HashSet<Integer> getKeysOn()
  {
    return this.keysOn;
  }
  
  public void checkPlayer(Player p, Map m)
  {
    if (p.isStunned()) {
      return;
    }
    if (this.keysOn.contains(Integer.valueOf(32)))
    {
      if (p.isCanJump())
      {
        if (!p.isJumping()) {
          Audio.playSound("jump2");
        }
        p.setJumping(true);
      }
    }
    else
    {
      if (p.isJumping()) {
        p.setCanJump(false);
      }
      p.setJumping(false);
      p.setApplyGravity(true);
    }
    if (this.keysOn.contains(Integer.valueOf(68))) {
      m.forceMoveEntity(p, 6);
    } else if (this.keysOn.contains(Integer.valueOf(65))) {
      m.forceMoveEntity(p, 4);
    } else {
      m.forceMoveEntity(p, 0);
    }
  }
  
  public void mouseClicked(MouseEvent e)
  {
    calculateClickedTile(this.xMouse, this.yMouse, this.level);
  }
  
  public void mousePressed(MouseEvent e) {}
  
  public void mouseReleased(MouseEvent e) {}
  
  public void mouseEntered(MouseEvent e) {}
  
  public void mouseExited(MouseEvent e)
  {
    this.xMouse = -1.0F;
  }
  
  public void mouseDragged(MouseEvent me)
  {
    this.xMouse = (me.getX() / 2);
    this.yMouse = (me.getY() / 2);
  }
  
  public void mouseMoved(MouseEvent e)
  {
    this.xMouse = (e.getX() / 2);
    this.yMouse = (e.getY() / 2);
  }
  
  private void calculateClickedTile(float xMouse, float yMouse, Level level)
  {
    int xTile = (int)(xMouse / 8.0F) + level.getxOffset() / 8;
    int yTile = (int)(yMouse / 8.0F) + level.getyOffset() / 8;
    Point2D pt = new Point2D.Float(xTile, yTile);
    BlockType type = null;
    try
    {
      type = level.getMap().getTile(pt).getBlockType();
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
      e.printStackTrace(System.out);
    }
    if (type != level.getSelectedBlock().getBlockType()) {
      level.getMap().setTile(pt, level.getSelectedBlock().getBlockType());
    }
  }
}
