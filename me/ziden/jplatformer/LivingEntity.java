package me.ziden.jplatformer;

import java.awt.Rectangle;
import java.awt.geom.Point2D.Float;
import me.ziden.jplatformer.GameTimer.GameTask;
import me.ziden.jplatformer.GameTimer.GameTimer;
import me.ziden.jplatformer.sound.Audio;

public abstract class LivingEntity
  extends Entity
{
  protected boolean canJump = true;
  protected float maxJump = 7.0F;
  protected float jumpHeight = 0.0F;
  protected float jumpSpeed = 4.0F;
  protected boolean jumping = false;
  protected boolean stunned = false;
  protected int facing = 6;
  protected int direction = 0;
  protected int ticksToAnimate = 5;
  protected int tickAnim = 0;
  protected int sprite = 0;
  protected int maxFrame = 3;
  protected int frame = 0;
  
  public LivingEntity(int x, int y)
  {
    this.box = new Rectangle(x, x, 8, 8);
    this.x = x;
    this.y = y;
  }
  
  public Rectangle getFutureBox()
  {
    return new Rectangle((int)(this.x + 2.0F + this.aceleration.x), (int)(this.y + 3.0F + this.aceleration.y), 12, 16);
  }
  
  public boolean isMoving()
  {
    if ((this.aceleration.x == 0.0F) && (this.aceleration.y == 0.0F)) {
      return false;
    }
    return true;
  }
  
  public void gravityTick()
  {
    if ((this.applyGravity) && (!this.jumping))
    {
      float aceY = this.aceleration.y;
      if (aceY < 10.0F) {
        aceY += 0.8F;
      }
      this.aceleration.y = aceY;
    }
    else if (this.jumping)
    {
      this.aceleration.y = (-this.jumpSpeed);
      this.jumpHeight += this.jumpSpeed;
      if (this.jumpHeight > 16.0F)
      {
        this.jumping = false;
        this.canJump = false;
        this.applyGravity = true;
      }
    }
    else
    {
      this.aceleration.y = 0.0F;
      this.jumpHeight = 0.0F;
      this.canJump = true;
    }
  }
  
  public void hitWall(int wallDirection)
  {
    if (!this.stunned) {
      if (wallDirection == 8)
      {
        this.aceleration.x = 0.0F;
        this.aceleration.y = 0.1F;
      }
      else if (((this.aceleration.x > 0.0F) && (this.aceleration.x > 3.5D)) || ((this.aceleration.x < 0.0F) && (this.aceleration.x < -3.5D)))
      {
        Audio.playSound("hitwall2");
        this.stunned = true;
        
        this.aceleration.x = (-this.aceleration.x / 4.0F);
        if ((!this.jumping) && (!this.applyGravity)) {
          this.aceleration.y = -3.5F;
        }
        this.jumping = false;
        this.canJump = false;
        this.applyGravity = true;
        this.frame = 4;
        
        GameTask task = new GameTask(100L)
        {
          public void run()
          {
            GameTask t = new GameTask(400L)
            {
              public void run()
              {
                LivingEntity.this.stunned = false;
              }
            };
            JPlatformer.timer.addTask(t);
            
            LivingEntity.this.frame = 5;
          }
        };
        JPlatformer.timer.addTask(task);
      }
      else
      {
        this.aceleration.x = 0.0F;
      }
    }
  }
  
  public void calculateNextPosition()
  {
    if (this.stunned) {
      return;
    }
    if (this.direction == 6)
    {
      float aceX = this.aceleration.x;
      if (aceX < 0.0F)
      {
        if (aceX < this.maxSpeed) {
          if (isInAir()) {
            aceX += this.desacelerationSpeed;
          } else {
            aceX = (float)(aceX + this.desacelerationSpeed * 1.5D);
          }
        }
        if (aceX > this.maxSpeed) {
          aceX = this.maxSpeed;
        }
        this.aceleration.x = aceX;
      }
      else
      {
        if (aceX < this.maxSpeed) {
          aceX += this.acelerationSpeed;
        }
        if (aceX > this.maxSpeed) {
          aceX = this.maxSpeed;
        }
        this.aceleration.x = aceX;
      }
    }
    else if (this.direction == 4)
    {
      float aceX = this.aceleration.x;
      if (aceX > 0.0F)
      {
        if (aceX > this.maxSpeed * -1.0F) {
          if (isInAir()) {
            aceX -= this.desacelerationSpeed;
          } else {
            aceX = (float)(aceX - this.desacelerationSpeed * 1.5D);
          }
        }
        if (aceX < this.maxSpeed * -1.0F) {
          aceX = this.maxSpeed * -1.0F;
        }
        this.aceleration.x = aceX;
      }
      else
      {
        if (aceX > this.maxSpeed * -1.0F) {
          aceX -= this.acelerationSpeed;
        }
        if (aceX < this.maxSpeed * -1.0F) {
          aceX = this.maxSpeed * -1.0F;
        }
        this.aceleration.x = aceX;
      }
    }
    else if (this.direction == 0)
    {
      if (this.aceleration.x > 0.0F)
      {
        if (this.aceleration.x - this.desacelerationSpeed < 0.0F) {
          this.aceleration.x = 0.0F;
        } else {
          this.aceleration.x -= this.desacelerationSpeed;
        }
      }
      else if (this.aceleration.x + this.desacelerationSpeed > 0.0F) {
        this.aceleration.x = 0.0F;
      } else {
        this.aceleration.x += this.desacelerationSpeed;
      }
    }
  }
  
  public abstract void tick();
  
  public boolean isCanJump()
  {
    return this.canJump;
  }
  
  public void setCanJump(boolean canJump)
  {
    this.canJump = canJump;
  }
  
  public int getDirection()
  {
    return this.direction;
  }
  
  public void setDirection(int direction)
  {
    if ((direction != 0) && 
      (!isInAir())) {
      setFacing(this.direction);
    }
    this.direction = direction;
  }
  
  public int getFacing()
  {
    return this.facing;
  }
  
  public void setFacing(int facing)
  {
    this.facing = facing;
  }
  
  public int getFrame()
  {
    return this.frame;
  }
  
  public void setFrame(int frame)
  {
    this.frame = frame;
  }
  
  public float getJumpHeight()
  {
    return this.jumpHeight;
  }
  
  public void setJumpHeight(float jumpHeight)
  {
    this.jumpHeight = jumpHeight;
  }
  
  public float getJumpSpeed()
  {
    return this.jumpSpeed;
  }
  
  public void setJumpSpeed(float jumpSpeed)
  {
    this.jumpSpeed = jumpSpeed;
  }
  
  public boolean isJumping()
  {
    return this.jumping;
  }
  
  public void setJumping(boolean jumping)
  {
    this.jumping = jumping;
  }
  
  public int getMaxFrame()
  {
    return this.maxFrame;
  }
  
  public void setMaxFrame(int maxFrame)
  {
    this.maxFrame = maxFrame;
  }
  
  public float getMaxJump()
  {
    return this.maxJump;
  }
  
  public void setMaxJump(float maxJump)
  {
    this.maxJump = maxJump;
  }
  
  public int getSprite()
  {
    return this.sprite;
  }
  
  public void setSprite(int sprite)
  {
    this.sprite = sprite;
  }
  
  public boolean isStunned()
  {
    return this.stunned;
  }
  
  public void setStunned(boolean stunned)
  {
    this.stunned = stunned;
  }
  
  public int getTickAnim()
  {
    return this.tickAnim;
  }
  
  public void setTickAnim(int tickAnim)
  {
    this.tickAnim = tickAnim;
  }
  
  public int getTicksToAnimate()
  {
    return this.ticksToAnimate;
  }
  
  public void setTicksToAnimate(int ticksToAnimate)
  {
    this.ticksToAnimate = ticksToAnimate;
  }
}
