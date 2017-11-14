package me.ziden.jplatformer;

import java.awt.Rectangle;
import java.awt.geom.Point2D.Float;
import java.util.UUID;

public class Player
  extends LivingEntity
{
  public Player()
  {
    super(2, 0);
    this.width = 12;
    this.height = 14;
  }
  
  private UUID id = UUID.randomUUID();
  private boolean animForward = true;
  private int slipSmokeTickCount = 5;
  private int slipSmokeCount = 0;
  private Weapon weapon = null;
  private int currentIndex = 0;
  private int[] walkFrames = { 0, 1, 2 };
  
  public void tick()
  {
    animationTick();
    this.tickAnim += 1;
  }
  
  public void equipWeapon(Weapon w)
  {
    this.weapon = w;
    int[] frameset = { 6, 7, 8 };
    this.walkFrames = frameset;
  }
  
  public void unequipWeapon(Weapon w)
  {
    this.weapon = null;
    int[] frameset = { 0, 1, 2 };
    this.walkFrames = frameset;
  }
  
  private void animationTick()
  {
    if (this.stunned) {
      return;
    }
    if ((this.jumping) || (this.aceleration.y < 0.0F))
    {
      if (this.weapon != null) {
        this.frame = 8;
      } else {
        this.frame = 9;
      }
      return;
    }
    if ((this.applyGravity) && (this.aceleration.y > 0.0F))
    {
      if (this.weapon != null) {
        this.frame = 6;
      } else {
        this.frame = 10;
      }
      return;
    }
    if ((this.aceleration.getX() == 0.0D) && (this.aceleration.getY() == 0.0D))
    {
      this.frame = this.walkFrames[1];
    }
    else
    {
      if ((this.aceleration.x >= this.maxSpeed) || (this.aceleration.x <= -this.maxSpeed)) {
        this.ticksToAnimate = 3;
      } else if ((this.aceleration.x >= this.maxSpeed / 3.0F) || (this.aceleration.x <= -this.maxSpeed / 3.0F)) {
        this.ticksToAnimate = 4;
      } else {
        this.ticksToAnimate = 6;
      }
      if (((this.aceleration.x > 1.0F) && (this.direction == 4) && (this.weapon == null)) || ((this.weapon != null) && (this.weapon.allowSlide)))
      {
        this.slipSmokeCount += 1;
        if (this.slipSmokeCount >= this.slipSmokeTickCount)
        {
          Effects.AnimatedEffect eff = Map.mapEffects.getNewEffect(getFutureBox().x, (int)this.y + 10, Effects.EffectType.SMOKE, 0, 0);
          eff.maxFrame = 6;
          eff.ticksToFrame = 1;
          Map.mapEffects.playEffect(eff);
          this.slipSmokeCount = 0;
        }
        else
        {
          this.slipSmokeCount += 1;
        }
        this.frame = 3;
      }
      else if (((this.aceleration.x < -1.0F) && (this.direction == 6) && (this.weapon == null)) || ((this.weapon != null) && (this.weapon.allowSlide)))
      {
        if (this.slipSmokeCount >= this.slipSmokeTickCount)
        {
          Effects.AnimatedEffect eff = Map.mapEffects.getNewEffect(getFutureBox().x, (int)this.y + 10, Effects.EffectType.SMOKE, 0, 0);
          eff.maxFrame = 6;
          eff.ticksToFrame = 1;
          Map.mapEffects.playEffect(eff);
          this.slipSmokeCount = 0;
        }
        else
        {
          this.slipSmokeCount += 3;
        }
        this.frame = 3;
      }
      else if (this.tickAnim >= this.ticksToAnimate)
      {
        this.tickAnim = 0;
        if (this.currentIndex == this.walkFrames.length - 1) {
          this.animForward = false;
        } else if (this.currentIndex == 0) {
          this.animForward = true;
        }
        if (this.animForward) {
          this.currentIndex += 1;
        } else {
          this.currentIndex -= 1;
        }
        if (this.currentIndex < this.walkFrames.length) {
          this.frame = this.walkFrames[this.currentIndex];
        }
      }
    }
  }
  
  public void setFacing(int f)
  {
    if (this.weapon == null)
    {
      this.facing = f;
      return;
    }
    if ((f == 6) && (this.aceleration.x > 0.0F)) {
      this.facing = f;
    }
    if ((f == 4) && (this.aceleration.x < 0.0F)) {
      this.facing = f;
    }
  }
  
  public UUID getId()
  {
    return this.id;
  }
  
  public void setId(UUID id)
  {
    this.id = id;
  }
}
