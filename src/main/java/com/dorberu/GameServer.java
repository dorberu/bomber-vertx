package com.dorberu;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.dorberu.packet.Packet;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;

public class GameServer implements EventBusMessageHandler.MessageReceiver
{
	protected static final GameServer _this = new GameServer();
	
	protected Vertx vertx;
	protected EventBusMessageHandler eventBusMessageHandler;
	
	public List<BattleRoomController> battleRoomControllerList;
	
    public static GameServer getInstance()
    {
        return _this;
    }
    
    public boolean onInit(Vertx vertx)
    {
        Log.info(getClass().getName(), "onInit");
        
        this.vertx = vertx;
        this.eventBusMessageHandler = new EventBusMessageHandler(this.vertx, this, "gameserver.local", "gameserver.global");
        this.battleRoomControllerList = new ArrayList<>();

        Log.info(getClass().getName(), "onInit", "complete");
        return true;
    }

    protected void onTick(Long microDelay)
    {
    	this.battleRoomControllerList.forEach(o -> o.onTick());
    }
    
    public BattleRoomController addBattleRoomController()
    {
    	BattleRoomController battleRoomController = new BattleRoomController();
    	this.battleRoomControllerList.add(battleRoomController);
    	return battleRoomController;
    }
    
    public BattleRoomController getBattleRoomController(String handlerId)
    {
    	return this.battleRoomControllerList.stream().filter(o -> o.containsCharacter(handlerId)).findFirst().orElse(null);
    }
    
    public BattleRoomController getEmptyBattleRoomController()
    {
    	return this.battleRoomControllerList.stream().filter(o -> !o.isFull()).findFirst().orElse(null);
    }
    
    public void sendPacket(String handlerId, JsonObject packet)
    {
    	try {
			Buffer buffer = Buffer.buffer(handlerId.getBytes("UTF-8"));
			buffer.appendBytes(packet.toString().getBytes("UTF-8"));
			this.vertx.eventBus().send("websocketserver.local", buffer);
	        Log.info(getClass().getName(), "sendPacket", "handlerId: " + handlerId, "packetData: " + packet.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }
    
    public List<CharacterController> getEnemies(String handlerId)
    {
    	BattleRoomController battleRoomController = this.getBattleRoomController(handlerId);
    	if (battleRoomController != null)
    	{
    		return battleRoomController.getCharacterControllers().stream().filter(ch -> !ch.handlerId.equals(handlerId)).collect(Collectors.toList());
    	}
    	return null;
    }
    
    public void publishPacket(String handlerId, JsonObject packet)
    {
    	BattleRoomController battleRoomController = this.getBattleRoomController(handlerId);
    	if (battleRoomController != null)
    	{
        	battleRoomController.getHandlerIds().stream().filter(id -> !id.equals(handlerId)).forEach(otherId -> {
        		this.sendPacket(otherId, packet);
        	});
    	}
    }
    
    @Override
    public void receivePacket(String handlerId, JsonObject packetData)
    {
    	Log.trace(getClass().getName(), "receivePacket", "handlerId: " + handlerId, "packetData: " + packetData);
        Packet packet = Packet.getPacket(packetData);
        if (packet != null)
        {
            packet.load(handlerId, this);
        }
        else
        {
        	Log.error(getClass().getName(), "receivePacket", "packetId not found", "handlerId: " + handlerId, "packetData: " + packetData);
        }
    }
}
