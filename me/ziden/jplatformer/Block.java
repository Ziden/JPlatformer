package me.ziden.jplatformer;

public class Block
{
  private BlockType blockType;
  
  public Block(BlockType blocktype)
  {
    this.blockType = blocktype;
  }
  
  public BlockType getBlockType()
  {
    return this.blockType;
  }
  
  public void setBlockType(BlockType blockType)
  {
    this.blockType = blockType;
  }
}
