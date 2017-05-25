package com.dorberu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.dorberu.packet.FinishPacket;

public class BattleRoomController
{
	private static final int PHASE_INIT = 1;
	private static final int PHASE_READY = 2;
	private static final int PHASE_PLAY = 3;
	private static final int PHASE_JUDGE = 4;
	private static final int PHASE_FINISH = 5;
	
	private static final int MAX_CHARACTER_COUNT = 4;
	private GameServer _server;
	private int _phase;
	private int _coolTime;
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
    	this._phase = PHASE_INIT;
    	this._coolTime = (int) GameVerticle.FPS;
    	return true;
    }
    
    public void onTick()
    {
        switch (this._phase)
        {
        case PHASE_INIT:
        	this._phase = PHASE_READY;
        	break;
        case PHASE_READY:
        	if (this._characterCount >= 2)
        	{
        		this._phase = PHASE_PLAY;
        	}
        	break;
        case PHASE_PLAY:
        	this._characterControllers.forEach((key, value) -> value.onTick());
        	this._mapController.onTick();
        	if (this.isJudge())
        	{
        		this._phase = PHASE_JUDGE;
        	}
        	break;
        case PHASE_JUDGE:
        	if (this.isFinish())
        	{
        		this.finish();
        		this._phase = PHASE_FINISH;
        	}
        	break;
        }
    }
    
    public boolean enableJoin()
    {
    	if (this._phase >= PHASE_JUDGE)
    	{
    		return false;
    	}
    	else if (this._characterCount >= MAX_CHARACTER_COUNT)
    	{
    		return false;
    	}
    	return true;
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
    
    private boolean isJudge()
    {
    	List<CharacterController> liveCharacterList = this.getLiveCharacters();
    	return liveCharacterList.size() <= 1;
    }
    
    private boolean isFinish()
    {
    	return Math.max(--this._coolTime, 0) == 0;
    }
    
    private void finish()
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
