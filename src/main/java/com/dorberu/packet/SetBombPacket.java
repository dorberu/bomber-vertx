package com.dorberu.packet;

import java.awt.Point;

import com.dorberu.BattleRoomController;
import com.dorberu.CharacterController;
import com.dorberu.GameServer;

import io.vertx.core.json.JsonObject;

public class SetBombPacket extends Packet
{
	public static final int PACKET_ID = 5;

	public static final String KEY_POS = "pos";
	public static final String KEY_CHARACTER_ID = "cId";
	public static final String KEY_BOMB_POS = "bPos";
	
	public SetBombPacket(Integer id, JsonObject data)
	{
		super(id, data);
	}

	@Override
	public void load(String handlerId, GameServer server)
	{
		Point pos = jsonArrayToPoint(this.data.getJsonArray(KEY_POS));
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
		
		this.result = RESULT_SUCCESS;
		if (!battleRoomController.getMapController().setBomb(handlerId, pos))
		{
			this.result = RESULT_FAIL;
		}
		send(handlerId, server, this.result, characterController.id, pos);
	}

	public static void send(String handlerId, GameServer server, int result, int id, Point pos)
	{
        JsonObject packet = new JsonObject();
        packet.put(KEY_ID, PACKET_ID);
        packet.put(KEY_RESULT, result);
        packet.put(KEY_CHARACTER_ID, id);
        packet.put(KEY_BOMB_POS, pointToJsonArray(pos));
        server.sendPacket(handlerId, packet);
        server.publishPacket(handlerId, packet);
	}
}
