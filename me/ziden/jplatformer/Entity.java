package me.ziden.jplatformer;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;

public class Entity
{
  protected int width = 8;
  protected int height = 8;
  protected boolean applyGravity = false;
  protected Point2D.Float aceleration = new Point2D.Float(0.0F, 0.0F);
  protected float acelerationSpeed = 0.2F;
  protected float standardAcelerationSpeed = 0.2F;
  protected float maxSpeed = 4.0F;
  protected float desacelerationSpeed = 0.2F;
  protected float standardDesacelerationSpeed = 0.2F;
  protected Rectangle box = new Rectangle();
  protected float weight = 0.8F;
  protected float x;
  protected float y;
  
  public void gravityTick()
  {
    if (this.applyGravity)
    {
      float aceY = this.aceleration.y;
      if (aceY < 10.0F) {
        aceY += 0.8F;
      }
      this.aceleration.y = aceY;
    }
  }
  
  public void positionTick()
  {
    this.x = ((float)(this.x + this.aceleration.getX()));
    this.y = ((float)(this.y + this.aceleration.getY()));
    
    this.box.setBounds((int)this.x + 2, (int)this.y, this.width, this.height);
  }
  
  public Point2D.Float getAceleration()
  {
    return this.aceleration;
  }
  
  public void setAceleration(Point2D.Float aceleration)
  {
    this.aceleration = aceleration;
  }
  
  public float getAcelerationSpeed()
  {
    return this.acelerationSpeed;
  }
  
  public void setAcelerationSpeed(float acelerationSpeed)
  {
    this.acelerationSpeed = acelerationSpeed;
  }
  
  public Rectangle getBox()
  {
    return this.box;
  }
  
  public void setBox(Rectangle box)
  {
    this.box = box;
  }
  
  public float getDesacelerationSpeed()
  {
    return this.desacelerationSpeed;
  }
  
  public void setDesacelerationSpeed(float desacelerationSpeed)
  {
    this.desacelerationSpeed = desacelerationSpeed;
  }
  
  public boolean isApplyGravity()
  {
    return this.applyGravity;
  }
  
  public void setApplyGravity(boolean g)
  {
    this.applyGravity = g;
  }
  
  public boolean isInAir()
  {
    return this.aceleration.y != 0.0F;
  }
  
  public boolean isFalling()
  {
    if (this.aceleration.y > 0.0F) {
      return true;
    }
    return false;
  }
  
  public float getMaxSpeed()
  {
    return this.maxSpeed;
  }
  
  public void setMaxSpeed(float maxSpeed)
  {
    this.maxSpeed = maxSpeed;
  }
  
  public float getX()
  {
    return this.x;
  }
  
  public void setX(float x)
  {
    this.x = x;
  }
  
  public float getY()
  {
    return this.y;
  }
  
  public void setY(float y)
  {
    this.y = y;
  }
}
