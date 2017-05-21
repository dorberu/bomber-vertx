package com.dorberu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BattleRoomController
{
	private static final int MAX_CHARACTER_COUNT = 4;
	private int _characterCount;
	private Map<String, CharacterController> _characterControllers = new HashMap<>();
	
	
    public boolean onInit()
    {
    	return true;
    }
    
    public void onTick()
    {
    	this._characterControllers.forEach((key, value) -> value.onTick());
    }
    
    public boolean isFull()
    {
    	return (this._characterCount >= MAX_CHARACTER_COUNT);
    }
    
    public List<CharacterController> getCharacterControllers()
    {
    	return this._characterControllers.values().stream().collect(Collectors.toList());
    }
    
    public Set<String> getHandlerIds()
    {
    	return this._characterControllers.keySet();
    }

    public boolean containsCharacter(String handlerId)
    {
    	return this._characterControllers.containsKey(handlerId);
    }

    public void addCharacter(String handlerId)
    {
    	if (!this.containsCharacter(handlerId))
    	{
    		this._characterCount++;
    		CharacterController characterController = new CharacterController();
    		characterController.onInit(handlerId, this._characterCount);
    		this._characterControllers.put(handlerId, characterController);
    	}
    }
    
    public CharacterController getCharacter(String handlerId)
    {
    	return this._characterControllers.get(handlerId);
    }

    public void removeCharacter(String handlerId)
    {
    	if (this.containsCharacter(handlerId))
    	{
    		Log.info(getClass().getName(), "delete", "handlerId: " + handlerId);
    		this._characterControllers.remove(handlerId);
    	}
    }
}
