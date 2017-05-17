package com.dorberu;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BattleRoomController
{
	private static final int MAX_CHARACTER_COUNT = 4;
	private int _characterCount;
	
	private Map<String, CharacterController> _handlerIds = new HashMap<>();
	
	
    public boolean onInit()
    {
    	return true;
    }
    
    public void onTick()
    {
		for (Entry<String, CharacterController> entry : this._handlerIds.entrySet())
		{
			entry.getValue().onTick();
		}
    }
    
    public boolean isFull()
    {
    	return (this._characterCount == MAX_CHARACTER_COUNT);
    }

    public boolean containsCharacter(String handlerId)
    {
    	return this._handlerIds.containsKey(handlerId);
    }

    public void addCharacter(String handlerId)
    {
    	if (!this._handlerIds.containsKey(handlerId))
    	{
    		CharacterController characterController = new CharacterController();
    		characterController.onInit(handlerId);
    		this._characterCount++;
    		this._handlerIds.put(handlerId, characterController);
    	}
    }

    public void removeCharacter(String handlerId)
    {
    	// TODO : setKill
    	if (this._handlerIds.containsKey(handlerId))
    	{
    		System.out.println(getClass().getName() + " delete " + handlerId);
    		this._handlerIds.remove(handlerId);
    	}
    }
}
