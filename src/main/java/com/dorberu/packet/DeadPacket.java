package com.dorberu.packet;

import com.dorberu.BattleRoomController;
import com.dorberu.CharacterController;
import com.dorberu.GameServer;

import io.vertx.core.json.JsonObject;

public class DeadPacket extends Packet
{
	public static final int PACKET_ID = 6;

	public static final String KEY_ENEMY_ID = "eId";
	
	public DeadPacket(Integer id, JsonObject data)
	{
		super(id, data);
	}

	@Override
	public void load(String handlerId, GameServer server)
	{
		BattleRoomController battleRoomController = server.getBattleRoomController(handlerId);
		if (battleRoomController == null)
		{
			return;
		}
		CharacterController characterController = battleRoomController.getCharacter(handlerId);
		if (characterController == null)
		{
			return;
		}
		characterController.setLife(0);
		
		send(handlerId, server, characterController.id);
	}

	public static void send(String handlerId, GameServer server, int id)
	{
        JsonObject packet = new JsonObject();
        packet.put(KEY_ID, PACKET_ID);
        packet.put(KEY_ENEMY_ID, id);
        server.publishPacket(handlerId, packet);
	}
}
