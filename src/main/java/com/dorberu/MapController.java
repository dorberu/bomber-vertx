package com.dorberu;

import java.awt.Dimension;
import java.awt.Point;

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
}
