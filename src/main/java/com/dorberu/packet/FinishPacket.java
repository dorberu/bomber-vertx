package com.dorberu.packet;

import com.dorberu.GameServer;

import io.vertx.core.json.JsonObject;

public class FinishPacket extends Packet
{
	public static final int PACKET_ID = 7;

	public static final String KEY_CHARACTER_ID = "cId";
	
	public FinishPacket(Integer id, JsonObject data)
	{
		super(id, data);
	}

	@Override
	public void load(String handlerId, GameServer server) {}

	public static void send(String handlerId, GameServer server, int id)
	{
        JsonObject packet = new JsonObject();
        packet.put(KEY_ID, PACKET_ID);
        packet.put(KEY_CHARACTER_ID, id);
        server.publishPacket(handlerId, packet);
	}
}
