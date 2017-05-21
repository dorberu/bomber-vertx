package com.dorberu.packet;

import java.awt.Point;

import com.dorberu.BattleRoomController;
import com.dorberu.CharacterController;
import com.dorberu.GameServer;

import io.vertx.core.json.JsonObject;

public class MovePacket extends Packet
{
	public static final int PACKET_ID = 4;

	public static final String KEY_PLAYER_POS = "pos";
	public static final String KEY_PLAYER_ADD = "add";
	public static final String KEY_ENEMY_ID = "eId";
	public static final String KEY_ENEMY_POS = "ePos";
	public static final String KEY_ENEMY_ADD = "eAdd";
	
	public MovePacket(Integer id, JsonObject data)
	{
		super(id, data);
	}

	@Override
	public void load(String handlerId, GameServer server)
	{
		Point pos = jsonArrayToPoint(this.data.getJsonArray(KEY_PLAYER_POS));
		Point add = jsonArrayToPoint(this.data.getJsonArray(KEY_PLAYER_ADD));
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
		characterController.update(pos, add);
		send(handlerId, server, characterController);
	}

	public static void send(String handlerId, GameServer server, CharacterController characterController)
	{
        JsonObject packet = new JsonObject();
        packet.put(KEY_ID, PACKET_ID);
        packet.put(KEY_ENEMY_ID, characterController.id);
        packet.put(KEY_ENEMY_POS, pointToJsonArray(characterController.pos));
        packet.put(KEY_ENEMY_ADD, pointToJsonArray(characterController.add));
        server.publishPacket(handlerId, packet);
	}
}
