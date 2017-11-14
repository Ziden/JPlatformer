package me.ziden.jplatformer;

import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

public class Effects
{
  private List<AnimatedEffect> effectsRunning;
  private List<AnimatedEffect> toRemove;
  
  public static enum EffectType
  {
    SMOKE(0),  SPARK(1);
    
    private final int id;
    
    private EffectType(int id)
    {
      this.id = id;
    }
  }
  
  public void playEffect(int x, int y, EffectType type, int xOffset, int yOffset)
  {
    AnimatedEffect eff = new AnimatedEffect(x, y, type, xOffset, yOffset);
    if (type == EffectType.SMOKE) {
      eff.maxFrame = 6;
    }
    this.effectsRunning.add(eff);
  }
  
  public void playEffect(AnimatedEffect eff)
  {
    this.effectsRunning.add(eff);
  }
  
  public void tick()
  {
    for (AnimatedEffect effect : this.effectsRunning)
    {
      this.effectsRunning.size();
      effect.positionTick();
      effect.tickCount += 1;
      if (effect.tickCount >= effect.ticksToFrame)
      {
        if (effect.rewinding) {
          effect.frame -= 1;
        } else {
          effect.frame += 1;
        }
        effect.tickCount = 0;
      }
      if ((effect.rewinding) && (effect.frame == -1)) {
        this.toRemove.add(effect);
      }
      if (effect.frame == effect.maxFrame) {
        if (effect.rewind)
        {
          effect.rewinding = true;
          effect.frame -= 1;
        }
        else
        {
          this.toRemove.add(effect);
        }
      }
    }
    this.effectsRunning.removeAll(this.toRemove);
    this.toRemove.clear();
  }
  
  public Effects()
  {
    this.effectsRunning = new LinkedList();
    
    this.toRemove = new LinkedList();
  }
  
  public void renderEffects(Graphics2D g, Bitmaps bitmaps, int xoffs, int yoffs)
  {
    for (AnimatedEffect effect : this.effectsRunning) {
      g.drawImage(((java.awt.image.BufferedImage[])bitmaps.animatedEffects.get(Integer.valueOf(effect.type.id)))[effect.frame], (int)effect.x + effect.xOffset - xoffs, (int)effect.y + effect.yOffset - yoffs, null);
    }
  }
  
  public AnimatedEffect getNewEffect(int x, int y, EffectType type, int xOffset, int yOffset)
  {
    return new AnimatedEffect(x, y, type, xOffset, yOffset);
  }
  
  public class AnimatedEffect
    extends Entity
  {
    public boolean rewinding = false;
    public boolean rewind = false;
    public Effects.EffectType type;
    public int frame;
    public int maxFrame;
    public int ticksToFrame = 3;
    public int tickCount = 0;
    public int xOffset = 0;
    public int yOffset = 0;
    
    public AnimatedEffect(int x, int y, Effects.EffectType type, int xOffset, int yOffset)
    {
      this.x = x;
      this.y = y;
      this.type = type;
      this.xOffset = xOffset;
      this.yOffset = yOffset;
      this.width = 16;
      this.height = 16;
    }
  }
}
