package me.ziden.jplatformer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;
import java.io.IOException;
import java.io.PrintStream;
import javax.swing.JFrame;
import me.ziden.jplatformer.GameTimer.GameTimer;
import me.ziden.jplatformer.level.Level;
import me.ziden.jplatformer.sound.Audio;

public class JPlatformer
  extends Canvas
  implements Runnable
{
  private static final long serialVersionUID = 1L;
  private static final String title = "JPlatformer";
  public static final int width = 512;
  public static final int height = 320;
  public static final int scale = 2;
  public static final int TICKS_PER_SECOND = 30;
  public static GameTimer timer = new GameTimer();
  public InputHandler input;
  private Level level;
  private boolean running;
  private VolatileImage image;
  private Thread thread;
  private int tickCount;
  private Bitmaps bitmaps = new Bitmaps();
  private boolean titleScreen = true;
  private boolean won = false;
  
  public void start()
  {
    this.running = true;
    this.thread = new Thread(this);
    this.thread.start();
  }
  
  public void stop()
  {
    this.running = false;
    try
    {
      if (this.thread != null) {
        this.thread.join();
      }
    }
    catch (InterruptedException e)
    {
      e.printStackTrace(System.out);
    }
  }
  
  private void init()
  {
    try
    {
      this.bitmaps.loadAll();
    }
    catch (IOException e)
    {
      e.printStackTrace(System.out);
    }
    this.level = new Level(20);
    this.input = new InputHandler(this, this.level);
    this.level.setInput(this.input);
    Audio.init();
  }
  
  public void run()
  {
    init();
    long lastTime = System.nanoTime();
    double unprocessed = 0.0D;
    double nsPerTick = 3.3333333333333332E7D;
    int frames = 0;
    int ticks = 0;
    long lastTimer1 = System.currentTimeMillis();
    while (this.running)
    {
      long now = System.nanoTime();
      unprocessed += (now - lastTime) / nsPerTick;
      lastTime = now;
      while (unprocessed >= 1.0D)
      {
        ticks++;
        tick();
        unprocessed -= 1.0D;
      }
      frames++;
      render();
      if (System.currentTimeMillis() - lastTimer1 > 1000L)
      {
        lastTimer1 += 1000L;
        System.out.println(ticks + " ticks, " + frames + " fps");
        frames = 0;
        ticks = 0;
      }
    }
    System.exit(0);
  }
  
  private void tick()
  {
    this.tickCount += 1;
    this.level.tick();
    timer.tick();
  }
  
  private void render()
  {
    BufferStrategy bs = getBufferStrategy();
    if (bs == null)
    {
      requestFocus();
      createBufferStrategy(2);
      bs = getBufferStrategy();
    }
    if (this.image == null) {
      this.image = createVolatileImage(512, 320);
    }
    if (bs != null)
    {
      Graphics2D g = this.image.createGraphics();
      renderGame(g);
      g.dispose();
      
      Graphics gg = bs.getDrawGraphics();
      gg.drawImage(this.image, 0, 0, 1024, 640, 0, 0, 512, 320, null);
      gg.dispose();
      bs.show();
    }
  }
  
  private void renderGame(Graphics2D g)
  {
    g.setColor(new Color(4422071));
    g.fillRect(0, 0, 512, 320);
    if ((!this.titleScreen) && (!this.won))
    {
      g.setColor(new Color(8891903));
      g.fillRect(0, 0, 512, 40);
    }
    AffineTransform af = g.getTransform();
    
    this.level.render(g, this.bitmaps);
    
    g.setTransform(af);
    
    g.setTransform(af);
    g.setFont(new Font("Sans-Serif", 0, 10));
    
    Point2D targetTile = getTileInPixels(this.input.xMouse, this.input.yMouse);
    g.setColor(Color.black);
    Rectangle selectedTile = this.level.getMap().getBoxFromTile(targetTile);
    g.drawRect(selectedTile.x - this.level.getxOffset(), selectedTile.y - this.level.getyOffset(), selectedTile.width, selectedTile.height);
  }
  
  public Point2D getTileInPixels(float x, float y)
  {
    int xTile = (int)(x / 8.0F) + this.level.getxOffset() / 8;
    int yTile = (int)(y / 8.0F) + this.level.getyOffset() / 8;
    Point2D pt = new Point2D.Float(xTile, yTile);
    
    return pt;
  }
  
  public static Dimension redimensionarFrameTotal()
  {
    return new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
  }
  
  public static void main(String[] args)
  {
    JPlatformer tower = new JPlatformer();
    tower.setMinimumSize(new Dimension(1024, 640));
    tower.setMaximumSize(new Dimension(1024, 640));
    tower.setPreferredSize(new Dimension(1024, 640));
    
    JFrame frame = new JFrame("Knights of Monarchy");
    frame.setDefaultCloseOperation(3);
    frame.add(tower);
    frame.pack();
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    
    tower.start();
  }
  
  public void win()
  {
    this.won = true;
  }
}
