package com.dorberu.packet;

import com.dorberu.BattleRoomController;
import com.dorberu.GameServer;

import io.vertx.core.json.JsonObject;

public class LoginPacket extends Packet
{
	public static final int PACKET_ID = 1;
	
	public LoginPacket(Integer id, JsonObject data)
	{
		super(id, data);
	}
	
	public void load(String handlerId, GameServer server)
	{
        boolean isNew = true;
        BattleRoomController emptyBattleRoomController = null;
        for (BattleRoomController battleRoomController : server.battleRoomControllerList)
        {
        	if (battleRoomController.containsCharacter(handlerId))
        	{
        		isNew = false;
        	}
        	if (!battleRoomController.isFull())
        	{
        		emptyBattleRoomController = battleRoomController;
        	}
        }
        
        if (isNew)
        {
            if (emptyBattleRoomController == null)
            {
            	emptyBattleRoomController = new BattleRoomController();
            	server.battleRoomControllerList.add(emptyBattleRoomController);
            }
        	emptyBattleRoomController.addCharacter(handlerId);
        }
        
        JsonObject packet = new JsonObject();
        packet.put(KEY_ID, PACKET_ID);
        packet.put(KEY_RESULT, 1);
        server.sendPacket(handlerId, packet);
	}
}
