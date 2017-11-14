package me.ziden.jplatformer;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class Bitmaps
{
  public static String paraURL(String s)
  {
    StringBuilder o = new StringBuilder();
    for (char ch : s.toCharArray()) {
      if (isUnsafe(ch))
      {
        o.append('%');
        o.append(toHex(ch / '\020'));
        o.append(toHex(ch % '\020'));
      }
      else
      {
        o.append(ch);
      }
    }
    return o.toString();
  }
  
  private static char toHex(int ch)
  {
    return (char)(ch < 10 ? 48 + ch : 65 + ch - 10);
  }
  
  private static boolean isUnsafe(char ch)
  {
    if ((ch > '') || (ch < 0)) {
      return true;
    }
    return " %$&+,/:;=?@<>#%".indexOf(ch) >= 0;
  }
  
  public HashMap<Integer, BufferedImage[]> sprites = new HashMap();
  public HashMap<Integer, BufferedImage[]> negativeSprites = new HashMap();
  public HashMap<Integer, BufferedImage[]> animatedEffects = new HashMap();
  public BufferedImage[] iconSet;
  public BufferedImage[][] tiles;
  
  public int getTileIndex(BlockType b)
  {
    if (b == BlockType.DIRT) {
      return 1;
    }
    if (b == BlockType.AIR) {
      return 2;
    }
    return -1;
  }
  
  public BufferedImage getTileImage(BlockType b)
  {
    if (b == BlockType.DIRT) {
      return this.tiles[0][0];
    }
    if (b == BlockType.AIR) {
      return this.tiles[0][2];
    }
    if (b == BlockType.STONE) {
      return this.tiles[0][1];
    }
    return null;
  }
  
  public BufferedImage getTileImage(BlockType b, int direction)
  {
    int directionIndex = 1;
    if (direction == 8) {
      directionIndex = 2;
    } else if (direction == 4) {
      directionIndex = 3;
    }
    if (direction == 6) {
      directionIndex = 4;
    }
    if (b == BlockType.DIRT) {
      return this.tiles[directionIndex][0];
    }
    if (b == BlockType.AIR) {
      return this.tiles[directionIndex][2];
    }
    if (b == BlockType.STONE) {
      return this.tiles[directionIndex][1];
    }
    return null;
  }
  
  public void loadAll()
    throws IOException
  {
    BufferedImage icons = ImageIO.read(JPlatformer.class.getResource("/resource/sprites/iconset.png"));
    this.iconSet = new BufferedImage[icons.getWidth() / 8];
    for (int i = 0; i < icons.getWidth() / 8; i++) {
      this.iconSet[i] = clip(icons, i * 8, 0, 8, 8);
    }
    BufferedImage src = ImageIO.read(JPlatformer.class.getResource("/resource/tilesets/tiles.png"));
    this.tiles = new BufferedImage[src.getWidth() / 8][src.getHeight() / 8];
    for (int i = 0; i < 12; i++) {
      for (int j = 0; j < 4; j++) {
        this.tiles[i][j] = clip(src, i * 8, j * 8, 8, 8);
      }
    }
    int effectCounter = 0;
    for (;;)
    {
      BufferedImage effect;
      try
      {
        effect = ImageIO.read(JPlatformer.class.getResource("/resource/effects/effect" + effectCounter + ".png"));
      }
      catch (Throwable t)
      {
        break;
      }
      if (effect == null) {
        break;
      }
      BufferedImage[] effects = new BufferedImage[effect.getWidth() / 16];
      for (int x = 0; x < effect.getWidth() / 16; x++) {
        effects[x] = clip(effect, x * 16, 0, 16, 16);
      }
      this.animatedEffects.put(Integer.valueOf(effectCounter), effects);
      effectCounter++;
    }
    int spriteCounter = 0;
    for (;;)
    {
      BufferedImage chara;
      try
      {
        chara = ImageIO.read(JPlatformer.class.getResource("/resource/sprites/sprites" + spriteCounter + ".png"));
      }
      catch (Throwable t)
      {
        break;
      }
      if (chara == null) {
        break;
      }
      BufferedImage[] sprite = new BufferedImage[chara.getWidth() / 16];
      BufferedImage[] negateSprite = new BufferedImage[chara.getWidth() / 16];
      for (int x = 0; x < chara.getWidth() / 16; x++)
      {
        sprite[x] = clip(chara, x * 16, 0, 16, 20);
        negateSprite[x] = horizontalflip(clip(chara, x * 16, 0, 16, 20));
      }
      this.sprites.put(Integer.valueOf(spriteCounter), sprite);
      
      this.negativeSprites.put(Integer.valueOf(spriteCounter), negateSprite);
      spriteCounter++;
    }
  }
  
  public BufferedImage horizontalflip(BufferedImage bufferedImage)
  {
    AffineTransform tx = AffineTransform.getScaleInstance(-1.0D, 1.0D);
    tx.translate(-bufferedImage.getWidth(null), 0.0D);
    AffineTransformOp op = new AffineTransformOp(tx, 1);
    
    bufferedImage = op.filter(bufferedImage, null);
    return bufferedImage;
  }
  
  public static BufferedImage clip(BufferedImage src, int x, int y, int w, int h)
  {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    BufferedImage newImage = null;
    try
    {
      GraphicsDevice screen = ge.getDefaultScreenDevice();
      GraphicsConfiguration gc = screen.getDefaultConfiguration();
      newImage = gc.createCompatibleImage(w, h, 2);
    }
    catch (Exception e) {}
    if (newImage == null) {
      newImage = new BufferedImage(w, h, 2);
    }
    int[] pixels = new int[w * h];
    src.getRGB(x, y, w, h, pixels, 0, w);
    newImage.setRGB(0, 0, w, h, pixels, 0, w);
    
    return newImage;
  }
}
