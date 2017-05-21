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

	@Override
	public void load(String handlerId, GameServer server)
	{
		BattleRoomController battleRoomController = server.getBattleRoomController(handlerId);
		if (battleRoomController != null)
		{
			battleRoomController.removeCharacter(handlerId);
		}
        
        JsonObject packet = new JsonObject();
        packet.put(KEY_ID, PACKET_ID);
        packet.put(KEY_RESULT, result);
        server.sendPacket(handlerId, packet);
	}
	
	public static JsonObject getDummyPacket()
	{
		JsonObject data = new JsonObject();
		data.put(KEY_ID, PACKET_ID);
		return data;
	}
}
