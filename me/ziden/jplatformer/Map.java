package me.ziden.jplatformer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Point2D.Float;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import me.ziden.jplatformer.sound.Audio;

public final class Map
{
  private final int off = 4;
  private HashMap<String, Rectangle> dRects = new HashMap();
  private Block[][] map;
  private int w;
  private int h;
  public static Effects mapEffects = new Effects();
  private List<Player> players = new LinkedList();
  private ThreadSafeList<ItemDrop> itemDrops = new ThreadSafeList();
  
  public void setTile(Point2D tile, BlockType block)
  {
    this.map[((int)tile.getX())][((int)tile.getY())].setBlockType(block);
    calculateTileBorders();
  }
  
  public Block getTile(Point2D tile)
  {
    return this.map[((int)tile.getX())][((int)tile.getY())];
  }
  
  public void teleportEntity(LivingEntity e, int x, int y)
  {
    e.setX(x * 8);
    e.setY(y * 8);
  }
  
  public void addPlayer(Player p)
  {
    this.players.add(p);
    teleportEntity(p, 2, 2);
  }
  
  public Map(int s)
  {
    this.w = (s + 4);
    this.h = (s + 4);
    this.map = new Block[this.w][this.h];
    for (int x = 0; x < this.w; x++) {
      for (int y = 0; y < this.h; y++) {
        if (y == this.h - 1) {
          setTile(x, y, BlockType.STONE);
        } else if ((x < 2) || (x > this.w - 3) || (y < 2) || (y > this.h - 3)) {
          setTile(x, y, BlockType.DIRT);
        } else {
          setTile(x, y, BlockType.AIR);
        }
      }
    }
    ItemDrop pick = new ItemDrop(ItemType.PICKAXE);
    pick.x = 70.0F;
    pick.y = 25.0F;
    this.itemDrops.add(pick);
    calculateTileBorders();
    this.itemDrops.save();
  }
  
  public void setTile(int x, int y, BlockType b)
  {
    this.map[x][y] = new Block(b);
  }
  
  private ThreadSafeList<Border> tileBorders = new ThreadSafeList();
  public static final int DIR_UP = 8;
  public static final int DIR_LEFT = 4;
  public static final int DIR_RIGHT = 6;
  public static final int DIR_DOWN = 2;
  public static final int DIR_NONE = 0;
  
  public void calculateTileBorders()
  {
    for (int x = 0; x < this.w; x++) {
      for (int y = 0; y < this.h; y++)
      {
        if ((x == this.w - 1) || (this.map[x][y].getBlockType().getPriorioty() > this.map[(x + 1)][y].getBlockType().getPriorioty())) {
          this.tileBorders.add(new Border(x + 1, y, 6, this.map[x][y].getBlockType()));
        }
        if ((x == 0) || (this.map[x][y].getBlockType().getPriorioty() > this.map[(x - 1)][y].getBlockType().getPriorioty())) {
          this.tileBorders.add(new Border(x - 1, y, 4, this.map[x][y].getBlockType()));
        }
        if ((y == 0) || (this.map[x][y].getBlockType().getPriorioty() > this.map[x][(y - 1)].getBlockType().getPriorioty())) {
          this.tileBorders.add(new Border(x, y - 1, 2, this.map[x][y].getBlockType()));
        }
        if ((y == this.h - 1) || (this.map[x][y].getBlockType().getPriorioty() > this.map[x][(y + 1)].getBlockType().getPriorioty())) {
          this.tileBorders.add(new Border(x, y + 1, 8, this.map[x][y].getBlockType()));
        }
      }
    }
    this.tileBorders.save();
  }
  
  public Rectangle debugPoint(Point2D p)
  {
    return new Rectangle((int)p.getX(), (int)p.getY(), 2, 2);
  }
  
  public boolean isPassable(Block tile)
  {
    if (tile.getBlockType() == BlockType.AIR) {
      return true;
    }
    return false;
  }
  
  private Rectangle getBoxFromTile(int x, int y)
  {
    if (x > this.w) {
      x = this.w;
    }
    if (y > this.h) {
      y = this.h;
    }
    Rectangle r = new Rectangle(x * 8, y * 8, 8, 8);
    return r;
  }
  
  public Rectangle getBoxFromTile(Point2D tile)
  {
    return new Rectangle((int)tile.getX() * 8, (int)tile.getY() * 8, 8, 8);
  }
  
  public Point2D getTileEntityIs(LivingEntity p)
  {
    Point2D pt = new Point2D.Float();
    pt.setLocation(p.getX() / 8.0F, p.getY() / 8.0F + 1.0F);
    return pt;
  }
  
  public Point2D getTilePointIs(Point2D p)
  {
    Point2D pt = new Point2D.Float();
    
    pt.setLocation((int)p.getX() / 8, (int)p.getY() / 8);
    
    return pt;
  }
  
  public void checkIfEntityShouldFall(LivingEntity p)
  {
    try
    {
      if (p.isApplyGravity())
      {
        Point2D rightFeet = new Point2D.Double(p.getBox().getX() + p.getBox().getWidth() - 3.0D, p.getBox().getY() + p.getBox().getHeight() + 2.0D + p.getAceleration().y);
        Point2D leftFeet = new Point2D.Double(p.getBox().getX() + 1.0D, p.getBox().getY() + p.getBox().getHeight() + 2.0D + p.getAceleration().y);
        
        Point2D stepRight = getTilePointIs(rightFeet);
        Point2D stepLeft = getTilePointIs(leftFeet);
        if ((getTile(stepLeft).getBlockType() == BlockType.AIR) && (getTile(stepRight).getBlockType() == BlockType.AIR))
        {
          p.setApplyGravity(true);
        }
        else
        {
          if (p.isFalling())
          {
            Effects.AnimatedEffect eff = mapEffects.getNewEffect(p.getFutureBox().x, (int)stepRight.getY() * 8 - 10, Effects.EffectType.SMOKE, 0, 0);
            eff.maxFrame = 6;
            eff.ticksToFrame = 2;
            mapEffects.playEffect(eff);
            Audio.playSound("hitfloor");
          }
          p.setApplyGravity(false);
          p.y = ((float)stepRight.getY() * 8.0F - 16.0F);
        }
      }
    }
    catch (Throwable t)
    {
      t.printStackTrace(System.out);
      p.setApplyGravity(false);
    }
  }
  
  public void checkHeadHit(LivingEntity p)
  {
    try
    {
      if (p.getAceleration().y < 0.0F)
      {
        Point2D rightFeet = new Point2D.Double(p.getBox().getX() + p.getBox().getWidth() - 3.0D, p.getBox().getY() + p.getBox().getHeight() + 2.0D + p.getAceleration().y);
        Point2D leftFeet = new Point2D.Double(p.getBox().getX() + 1.0D, p.getBox().getY() + p.getBox().getHeight() + 2.0D + p.getAceleration().y);
        
        Point2D stepRight = getTilePointIs(rightFeet);
        Point2D stepLeft = getTilePointIs(leftFeet);
        
        stepRight.setLocation(stepRight.getX(), stepRight.getY() - 2.0D);
        stepLeft.setLocation(stepLeft.getX(), stepLeft.getY() - 2.0D);
        if ((getTile(stepLeft).getBlockType() != BlockType.AIR) || (getTile(stepRight).getBlockType() != BlockType.AIR))
        {
          if (p.getAceleration().y < -3.0F)
          {
            Effects.AnimatedEffect eff = mapEffects.getNewEffect(p.getFutureBox().x, (int)stepRight.getY() * 8 - 10, Effects.EffectType.SPARK, 0, 8);
            eff.maxFrame = 4;
            eff.ticksToFrame = 1;
            if (p.getAceleration().x > 0.0F) {
              eff.aceleration.x = -1.0F;
            } else {
              eff.aceleration.x = 1.0F;
            }
            eff.aceleration.y = 2.0F;
            eff.rewind = true;
            mapEffects.playEffect(eff);
          }
          p.setCanJump(false);
          p.setJumping(false);
          p.setApplyGravity(true);
          p.getAceleration().y = 0.0F;
        }
      }
    }
    catch (Throwable t)
    {
      t.printStackTrace(System.out);
      p.setApplyGravity(false);
    }
  }
  
  public void tick()
  {
    for (Player p : this.players)
    {
      checkIfEntityShouldFall(p);
      checkHeadHit(p);
      
      p.gravityTick();
      
      p.tick();
      
      p.calculateNextPosition();
      
      HashSet collisions = getEntityColisions(p);
      if ((collisions.contains(Integer.valueOf(6))) && (p.getAceleration().x > 0.0F)) {
        p.hitWall(6);
      } else if ((collisions.contains(Integer.valueOf(4))) && (p.getAceleration().x < 0.0F)) {
        p.hitWall(4);
      }
      p.positionTick();
    }
    for (ItemDrop i : this.itemDrops.getList())
    {
      i.gravityTick();
      i.positionTick();
      Point2D tile = getTilePointIs(new Point2D.Float(i.x, i.y));
      if (!isPassable(getTile(tile)))
      {
        i.y = ((int)(tile.getY() - 1.0D) * 8);
        i.applyGravity = false;
      }
    }
    mapEffects.tick();
  }
  
  public void forceMoveEntity(LivingEntity e, int direction)
  {
    if ((e instanceof Player))
    {
      Player p = (Player)this.players.get(this.players.indexOf(e));
      if (!e.isStunned()) {
        p.setDirection(direction);
      }
    }
  }
  
  public HashSet<Integer> getEntityColisions(LivingEntity p)
  {
    int tryDir = 0;
    HashSet<Integer> collisions = new HashSet();
    try
    {
      Point2D playerTile = getTileEntityIs(p);
      
      tryDir = 6;
      
      Block next1 = this.map[((int)playerTile.getX() + 2)][((int)playerTile.getY() - 1)];
      Block next2 = this.map[((int)playerTile.getX() + 2)][((int)playerTile.getY())];
      
      Rectangle colision = getBoxFromTile((int)playerTile.getX() + 2, (int)playerTile.getY() - 1);
      colision.setSize(8, 16);
      if (((!isPassable(next1)) || (!isPassable(next2))) && (p.getFutureBox().intersects(colision))) {
        collisions.add(Integer.valueOf(6));
      }
      tryDir = 4;
      Point2D playerTile2 = getTileEntityIs(p);
      Block next12 = this.map[((int)playerTile2.getX())][((int)playerTile2.getY() - 1)];
      Block next22 = this.map[((int)playerTile2.getX())][((int)playerTile2.getY())];
      Rectangle colision2 = getBoxFromTile((int)playerTile2.getX(), (int)playerTile2.getY() - 1);
      colision2.setSize(8, 16);
      if (((!isPassable(next12)) || (!isPassable(next22))) && (p.getFutureBox().intersects(colision2))) {
        collisions.add(Integer.valueOf(4));
      }
    }
    catch (Throwable t)
    {
      t.printStackTrace(System.out);
      
      collisions.add(Integer.valueOf(tryDir));
    }
    return collisions;
  }
  
  public void render(Graphics2D g, Bitmaps bits, int xoffs, int yoffs)
  {
    renderMap(g, bits, xoffs, yoffs);
    renderBorders(g, bits, xoffs, yoffs);
    renderPlayers(g, bits, xoffs, yoffs);
    renderItems(g, bits, xoffs, yoffs);
    
    mapEffects.renderEffects(g, bits, xoffs, yoffs);
  }
  
  public void renderItems(Graphics2D g, Bitmaps bits, int xoffs, int yoffs)
  {
    for (ItemDrop i : this.itemDrops.getList()) {
      g.drawImage(bits.iconSet[i.getItemType().id], (int)i.x - xoffs, (int)i.y - yoffs, null);
    }
  }
  
  public void renderBorders(Graphics2D g, Bitmaps bits, int xoffs, int yoffs)
  {
    for (Border b : this.tileBorders.getList()) {
      g.drawImage(bits.getTileImage(b.tileIndex, b.direction), b.x * 8 - xoffs, b.y * 8 - yoffs, null);
    }
  }
  
  public void renderMap(Graphics2D g, Bitmaps bits, int xoffs, int yoffs)
  {
    for (int x = 0; x < this.w; x++) {
      for (int y = 0; y < this.h; y++) {
        g.drawImage(bits.getTileImage(this.map[x][y].getBlockType()), x * 8 - xoffs, y * 8 - yoffs, null);
      }
    }
    g.setColor(Color.BLACK);
    for (Rectangle rect : this.dRects.values()) {
      g.fillRect(rect.x - xoffs, rect.y - yoffs, rect.width, rect.height);
    }
  }
  
  public void renderPlayers(Graphics2D g, Bitmaps bits, int xoffs, int yoffs)
  {
    for (Player p : this.players)
    {
      if (p.getFacing() == 0) {
        p.setFacing(p.getDirection());
      }
      if (p.getFacing() == 6) {
        g.drawImage(((java.awt.image.BufferedImage[])bits.negativeSprites.get(Integer.valueOf(p.getSprite())))[p.getFrame()], null, (int)p.getX() - xoffs, (int)p.getY() - yoffs - 2);
      } else if (p.getFacing() == 4) {
        g.drawImage(((java.awt.image.BufferedImage[])bits.sprites.get(Integer.valueOf(p.getSprite())))[p.getFrame()], null, (int)p.getX() - xoffs, (int)p.getY() - yoffs - 2);
      } else {
        System.out.println(p.getFacing());
      }
    }
  }
  
  public int getH()
  {
    return this.h;
  }
  
  public void setH(int h)
  {
    this.h = h;
  }
  
  public int getW()
  {
    return this.w;
  }
  
  public void setW(int w)
  {
    this.w = w;
  }
}
