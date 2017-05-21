package com.dorberu.packet;

import com.dorberu.CharacterController;
import com.dorberu.GameServer;

import io.vertx.core.json.JsonObject;

public class AddCharacterPacket extends Packet
{
	public static final int PACKET_ID = 3;

	public static final String KEY_ENEMY_ID = "eId";
	public static final String KEY_ENEMY_POS = "ePos";
	
	public AddCharacterPacket(Integer id, JsonObject data)
	{
		super(id, data);
	}

	@Override
	public void load(String handlerId, GameServer server) {}

	public static void send(String handlerId, GameServer server, CharacterController characterController)
	{
        JsonObject packet = new JsonObject();
        packet.put(KEY_ID, PACKET_ID);
        packet.put(KEY_ENEMY_ID, characterController.id);
        packet.put(KEY_ENEMY_POS, pointToJsonArray(characterController.pos));
        server.publishPacket(handlerId, packet);
	}
}
