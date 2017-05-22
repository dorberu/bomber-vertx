package com.dorberu;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MapController
{
	public static final Dimension BLOCK_SIZE = new Dimension(100, 100);
	public static final Dimension BLOCK_NUM = new Dimension(21, 21);
	public static final Dimension MAP_SIZE = new Dimension(BLOCK_SIZE.width * BLOCK_NUM.width, BLOCK_SIZE.height * BLOCK_NUM.height);
	
	public static final Point[] START_POINTS = new Point[]
	{
		new Point(BLOCK_SIZE.width, BLOCK_SIZE.height),
		new Point(BLOCK_SIZE.width * (BLOCK_NUM.width - 2), BLOCK_SIZE.height * (BLOCK_NUM.height - 2)),
		new Point(BLOCK_SIZE.width * (BLOCK_NUM.width - 2), BLOCK_SIZE.height),
		new Point(BLOCK_SIZE.width, BLOCK_SIZE.height * (BLOCK_NUM.height - 2))
	};
	
	// TODO : load file
	private static int[][] _map = null;
	public static int[][] getMap()
	{
		if (_map == null) {
			int[][] map = new int[BLOCK_NUM.width][BLOCK_NUM.height];
			for (int i = 0; i < map.length; i++)
			{
				for (int j = 0; j < map[i].length; j++)
				{
					map[i][j] = 0;
					if (i == 0 || i == map.length - 1 || j == 0 || j == map[i].length - 1 || (i % 2 == 0 && j % 2 == 0))
					{
						map[i][j] = 1;
					}
				}
			}
			_map = map;
		}
		return _map;
	}
	
	class Bomb
	{
		public String handlerId;
		public Point point;
		public int countDown;
		
		public Bomb(String handlerId, Point point)
		{
			this.handlerId = handlerId;
			this.point = point;
			this.countDown = 3 * (int) GameVerticle.FPS;
		}
		
		public void onTick()
		{
			this.countDown = Math.max(this.countDown, 0);
		}
	}
	
	private List<Bomb> _bombList = new ArrayList<>();
	
	public boolean setBomb(String handlerId, Point point)
	{
		List<Bomb> bombList = this._bombList.stream().filter(o -> o.equals(point)).collect(Collectors.toList());
		if (bombList.size() > 0)
		{
			return false;
		}
		return true;
	}
	
	public void onTick()
	{
		this._bombList.stream().forEach(o -> o.onTick());
		this._bombList = this._bombList.stream().filter(o -> o.countDown > 0).collect(Collectors.toList());
	}
}
