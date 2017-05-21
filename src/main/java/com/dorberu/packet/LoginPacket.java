package com.dorberu.packet;

import java.awt.Point;
import java.util.List;

import com.dorberu.BattleRoomController;
import com.dorberu.CharacterController;
import com.dorberu.GameServer;
import com.dorberu.MapController;

import io.vertx.core.json.JsonObject;

public class LoginPacket extends Packet
{
	public static final int PACKET_ID = 1;

	public static final String KEY_BLOCK_SIZE = "bSize";
	public static final String KEY_BLOCK_NUM = "bNum";
	public static final String KEY_MAP = "mapInfo";
	public static final String KEY_PLAYER_ID = "pId";
	public static final String KEY_PLAYER_POS = "pPos";
	public static final String KEY_ENEMY_IDS = "eIds";
	public static final String KEY_ENEMY_POSS = "ePoss";
	
	public LoginPacket(Integer id, JsonObject data)
	{
		super(id, data);
	}

	@Override
	public void load(String handlerId, GameServer server)
	{
		BattleRoomController battleRoomController = server.getBattleRoomController(handlerId);
		if (battleRoomController == null)
		{
			battleRoomController = server.getEmptyBattleRoomController();
			if (battleRoomController == null)
			{
				battleRoomController = server.addBattleRoomController();
			}
			battleRoomController.addCharacter(handlerId);
		}
		
		CharacterController characterController = battleRoomController.getCharacter(handlerId);
		
        JsonObject packet = new JsonObject();
        packet.put(KEY_ID, PACKET_ID);
        packet.put(KEY_RESULT, result);
        packet.put(KEY_BLOCK_SIZE, dimToJsonArray(MapController.BLOCK_SIZE));
        packet.put(KEY_BLOCK_NUM, dimToJsonArray(MapController.BLOCK_NUM));
        packet.put(KEY_MAP, intArrayToJsonArray(MapController.getMap()));
        packet.put(KEY_PLAYER_ID, characterController.id);
        packet.put(KEY_PLAYER_POS, pointToJsonArray(characterController.pos));
        
        List<CharacterController> enemies = server.getEnemies(handlerId);
        int[] enemyIds = new int[enemies.size()];
        Point[] enemyPoss = new Point[enemies.size()];
        int count = 0;
        enemies.stream().forEach(enemy -> {
        	enemyIds[count] = enemy.id;
        	enemyPoss[count] = enemy.pos;
        });
        packet.put(KEY_ENEMY_IDS, intArrayToJsonArray(enemyIds));
        packet.put(KEY_ENEMY_POSS, pointArrayToJsonArray(enemyPoss));
        
        server.sendPacket(handlerId, packet);
        
        AddCharacterPacket.send(handlerId, server, characterController);
	}
}
