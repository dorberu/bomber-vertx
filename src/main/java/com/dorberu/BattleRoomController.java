package com.dorberu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.dorberu.packet.FinishPacket;

public class BattleRoomController
{
	private static final int MAX_CHARACTER_COUNT = 4;
	private GameServer _server;
	private int _characterCount;
	private MapController _mapController;
	private Map<String, CharacterController> _characterControllers = new HashMap<>();
	
	
	public BattleRoomController(GameServer server)
	{
		this._server = server;
	}
	
    public boolean onInit()
    {
    	this._mapController = new MapController();
    	return true;
    }
    
    public void onTick()
    {
    	this._characterControllers.forEach((key, value) -> value.onTick());
    	this._mapController.onTick();
    	this.checkFinish();
    }
    
    public boolean isFull()
    {
    	return (this._characterCount >= MAX_CHARACTER_COUNT);
    }
    
    public List<CharacterController> getCharacterControllers()
    {
    	return this._characterControllers.values().stream().collect(Collectors.toList());
    }
    
    public MapController getMapController()
    {
    	return this._mapController;
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
    
    public List<CharacterController> getLiveCharacters()
    {
    	return this._characterControllers.values().stream().filter(o -> !o.isDead()).collect(Collectors.toList());
    }
    
    private void checkFinish()
    {
    	List<CharacterController> liveCharacterList = this.getLiveCharacters();
    	if (liveCharacterList.size() <= 0)
    	{
    		this.getHandlerIds().forEach(id -> {
    			FinishPacket.send(id, this._server, 0);
    		});
    	}
    	else if (liveCharacterList.size() == 1)
    	{
    		this.getHandlerIds().forEach(id -> {
    			FinishPacket.send(id, this._server, liveCharacterList.get(0).id);
    		});
    	}
    }
}
