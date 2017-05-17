package com.dorberu;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
        System.out.println(getClass().getName() + " onInit");
        
        this.vertx = vertx;
        this.eventBusMessageHandler = new EventBusMessageHandler(this.vertx, this, "gameserver.local", "gameserver.global");
        this.battleRoomControllerList = new ArrayList<>();
        
        System.out.println(getClass().getName() + " onInit completed");
        return true;
    }

    protected void onTick(Long microDelay)
    {
        for (BattleRoomController battleRoomController : this.battleRoomControllerList)
        {
        	battleRoomController.onTick();
        }
    }
    
    public void sendPacket(String handlerId, JsonObject packet)
    {
    	try {
			Buffer buffer = Buffer.buffer(handlerId.getBytes("UTF-8"));
			buffer.appendBytes(packet.toString().getBytes("UTF-8"));
			this.vertx.eventBus().send("websocketserver.local", buffer);
	        System.out.println(getClass().getName() + " sendPacket handlerId: " + handlerId + ", packetData: " + packet.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }
    
    @Override
    public void receivePacket(String handlerId, JsonObject packetData)
    {
        System.out.println(getClass().getName() + " receivePacket handlerId: " + handlerId + ", packetData: " + packetData);
        Packet packet = Packet.getPacket(packetData);
        if (packet != null)
        {
            packet.load(handlerId, this);
        }
        else
        {
            System.out.println(getClass().getName() + " handlerId: " + handlerId + " packetId not found. packetData: " + packetData);
        }
    }
}
