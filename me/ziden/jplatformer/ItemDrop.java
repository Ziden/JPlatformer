package me.ziden.jplatformer;

public class ItemDrop
  extends Entity
{
  private ItemType itemType;
  
  public ItemDrop(ItemType t)
  {
    this.itemType = t;
    this.applyGravity = true;
  }
  
  public ItemType getItemType()
  {
    return this.itemType;
  }
  
  public void setItemType(ItemType itemType)
  {
    this.itemType = itemType;
  }
}
