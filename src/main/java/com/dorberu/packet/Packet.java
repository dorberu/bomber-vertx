package com.dorberu.packet;

import java.awt.Dimension;
import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.dorberu.GameServer;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public abstract class Packet
{
	public static final Map<Integer, Class<? extends Packet>> packetList = new HashMap<Integer, Class<? extends Packet>>() {
		private static final long serialVersionUID = 6549022361595693194L;
	{
		put(LoginPacket.PACKET_ID, LoginPacket.class);
		put(LogoutPacket.PACKET_ID, LogoutPacket.class);
		put(AddCharacterPacket.PACKET_ID, AddCharacterPacket.class);
		put(MovePacket.PACKET_ID, MovePacket.class);
	}};

	public static final String KEY_ID = "id";
	public static final String KEY_RESULT = "result";

	public static final int RESULT_FAIL = 0;
	public static final int RESULT_SUCCESS = 1;
	
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
	
	public static JsonArray pointToJsonArray(Point point)
	{
		return new JsonArray(Arrays.asList(point.x, point.y));
	}
	
	public static JsonArray pointArrayToJsonArray(Point[] pointArray)
	{
		JsonArray resultArray = new JsonArray();
		for (int i = 0; i < pointArray.length; i++)
		{
			resultArray.add(pointToJsonArray(pointArray[i]));
		}
		return resultArray;
	}
	
	public static JsonArray dimToJsonArray(Dimension dim)
	{
		return new JsonArray(Arrays.asList(dim.width, dim.height));
	}
	
	public static JsonArray intArrayToJsonArray(int[] intArray)
	{
		JsonArray resultArray = new JsonArray();
		for (int i = 0; i < intArray.length; i++)
		{
			resultArray.add(intArray[i]);
		}
		return resultArray;
	}
	
	public static JsonArray intArrayToJsonArray(int[][] intArray)
	{
		JsonArray resultArray = new JsonArray();
		for (int i = 0; i < intArray.length; i++)
		{
			resultArray.add(intArrayToJsonArray(intArray[i]));
		}
		return resultArray;
	}
	
	public static int[] jsonArrayToIntArray(JsonArray jsonArray)
	{
		int[] intArray = new int[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++)
		{
			intArray[i] = jsonArray.getInteger(i);
		}
		return intArray;
	}
	
	public static Point jsonArrayToPoint(JsonArray jsonArray)
	{
		int[] intArray = jsonArrayToIntArray(jsonArray);
		Point point = new Point(intArray[0], intArray[1]);
		return point;
	}
	
	public int id;
	public int result;
	public JsonObject data;
	
	public Packet(Integer id, JsonObject data)
	{
		this.id = id.intValue();
		this.result = RESULT_SUCCESS;
		this.data = data;
	}
	
	public abstract void load(String handlerId, GameServer server);
}
