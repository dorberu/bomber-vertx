package com.dorberu;

public class CharacterController
{
	public String handlerId;
	
	private int _coolTime = 0;
	
    public boolean onInit(String handlerId)
    {
    	this.handlerId = handlerId;
    	return true;
    }
    
    public void onTick()
    {
    	if (this._coolTime++ > 90)
    	{
    		System.out.println(getClass().getName() + " onTick handlerId: " + this.handlerId);
    		this._coolTime = 0;
    	}
    }

}
