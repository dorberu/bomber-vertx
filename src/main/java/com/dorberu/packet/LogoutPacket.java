package com.dorberu.packet;

import com.dorberu.BattleRoomController;
import com.dorberu.GameServer;

import io.vertx.core.json.JsonObject;

public class LogoutPacket extends Packet
{
	public static final int PACKET_ID = 2;
	
	public LogoutPacket(Integer id, JsonObject data)
	{
		super(id, data);
	}
	
	public void load(String handlerId, GameServer server)
	{
		if (server.battleRoomControllerList.size() == 0)
		{
			return;
		}
		
        for (BattleRoomController battleRoomController : server.battleRoomControllerList)
        {
        	if (battleRoomController.containsCharacter(handlerId))
        	{
        		battleRoomController.removeCharacter(handlerId);
        	}
        }
        
        JsonObject packet = new JsonObject();
        packet.put(KEY_ID, PACKET_ID);
        packet.put(KEY_RESULT, 1);
        server.sendPacket(handlerId, packet);
	}
	
	public static JsonObject getDummyPacket()
	{
		JsonObject data = new JsonObject();
		data.put(KEY_ID, PACKET_ID);
		return data;
	}
}
