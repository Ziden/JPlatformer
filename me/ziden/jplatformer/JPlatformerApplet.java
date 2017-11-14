package me.ziden.jplatformer;

import java.applet.Applet;
import java.awt.BorderLayout;

public class JPlatformerApplet
  extends Applet
{
  private static final long serialVersionUID = 1L;
  private JPlatformer kom = new JPlatformer();
  
  public void init()
  {
    setLayout(new BorderLayout());
    add(this.kom, "Center");
  }
  
  public void start()
  {
    this.kom.start();
  }
  
  public void stop()
  {
    this.kom.stop();
  }
}
