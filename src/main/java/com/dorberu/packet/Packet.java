package com.dorberu.packet;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.dorberu.GameServer;

import io.vertx.core.json.JsonObject;

public abstract class Packet
{
	public static final Map<Integer, Class<? extends Packet>> packetList = new HashMap<Integer, Class<? extends Packet>>() {
		private static final long serialVersionUID = 6549022361595693194L;
	{
		put(LoginPacket.PACKET_ID, LoginPacket.class);
		put(LogoutPacket.PACKET_ID, LogoutPacket.class);
	}};

	public static final String KEY_ID = "id";
	public static final String KEY_RESULT = "result";
	
	public static Packet getPacket(JsonObject data)
	{
		if (!data.containsKey(KEY_ID))
		{
			return null;
		}
		
		int id = data.getInteger(KEY_ID);
		if (packetList.get(id) == null)
		{
			return null;
		}
		
		try {
			return packetList.get(id).getConstructor(Integer.class, JsonObject.class).newInstance(id, data);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int id;
	public JsonObject data;
	
	public Packet(Integer id, JsonObject data)
	{
		this.id = id.intValue();
		this.data = data;
	}
	
	public abstract void load(String handlerId, GameServer server);
}
