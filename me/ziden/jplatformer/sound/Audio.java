package me.ziden.jplatformer.sound;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import me.ziden.jplatformer.JPlatformer;
import kuusisto.tinysound.Music;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

public class Audio
{
  private static HashMap<String, Sound> soundCache = new HashMap();
  private static HashSet<Sound> soundPlayList = new HashSet();
  private static HashSet<Sound> soundHasPlayed = new HashSet();
  
  public static void init()
  {
    soundThread.start();
  }
  
  private static final Thread soundThread = new Thread()
  {
    public void run()
    {
      for (;;)
      {
        if (Audio.soundPlayList.size() > 0)
        {
          for (Sound s : Audio.soundPlayList)
          {
            s.play(0.5D);
            Audio.soundHasPlayed.add(s);
          }
          if (Audio.soundHasPlayed.size() > 0)
          {
            Audio.soundPlayList.removeAll(Audio.soundHasPlayed);
            Audio.soundHasPlayed.clear();
          }
        }
        try
        {
          Thread.sleep(100L);
        }
        catch (InterruptedException ex)
        {
          System.out.println(ex.getMessage());
        }
      }
    }
  };
  
  public void precacheSound() {}
  
  public static void playSound(String name)
  {
    if (!TinySound.isInitialized()) {
      TinySound.init();
    }
    if (soundCache.containsKey(name))
    {
      Sound s = (Sound)soundCache.get(name);
      soundPlayList.add(s);
    }
    else
    {
      Sound s = TinySound.loadSound(JPlatformer.class.getResource("/resource/sounds/" + name + ".wav"));
      soundCache.put(name, s);
      soundPlayList.add(s);
    }
  }
  
  public static void loopMusic(final String path)
  {
    try
    {
      new Thread()
      {
        public void run()
        {
          TinySound.init();
          Music music = TinySound.loadMusic("resource/sounds/" + path + ".wav");
          music.play(true);
        }
      }.start();
    }
    catch (Throwable e)
    {
      e.printStackTrace(System.out);
    }
    TinySound.shutdown();
  }
}
