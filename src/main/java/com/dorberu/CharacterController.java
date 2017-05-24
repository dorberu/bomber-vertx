package com.dorberu;

import java.awt.Point;

public class CharacterController
{
	public String handlerId;
	public int id;
	public int life;
	public Point pos;
	public Point add;
	
    public void onInit(String handlerId, int id)
    {
    	this.handlerId = handlerId;
    	this.id = id;
    	this.life = 1;
    	this.pos = MapController.START_POINTS[(id - 1) % MapController.START_POINTS.length];
    	this.add = new Point(0, 0);
    }
    
    public void onTick() {}
    
    public void update(Point pos, Point add)
    {
    	this.pos = pos;
    	this.add = add;
    }
    
    public void setLife(int life)
    {
    	this.life = life;
    }
    
    public boolean isDead()
    {
    	return this.life <= 0;
    }
}
