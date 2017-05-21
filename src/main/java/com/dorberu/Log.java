package com.dorberu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Log
{
	public static final int LEVEL_ERROR = 1;
	public static final int LEVEL_WARNING = 2;
	public static final int LEVEL_INFO = 3;
	public static final int LEVEL_TRACE = 4;

	public static final String TAG_ERROR = "[ERROR]";
	public static final String TAG_WARNING = "[WARN]";
	public static final String TAG_INFO = "[INFO]";
	public static final String TAG_TRACE = "[TRACE]";
	
	public static final Map<String, List<String>> backLog = new HashMap<>();
	public static final int BACK_LIMIT = 10;
	
	public static int currentLevel = LEVEL_INFO;
	
	public static void log(String tag, String name, String...strings)
	{
		String log = tag + " " + name;
		for (String s : strings)
		{
			log = log + ", " + s;
		}
		
		List<String> back = backLog.get(tag); 
		if (back == null)
		{
			List<String> addBack = new ArrayList<>();
			addBack.add(log);
			backLog.put(tag, addBack);
		}
		else if (back.size() <= 0 || !back.contains(log))
		{
			backLog.get(tag).add(log);
			int size = backLog.get(tag).size();
			if (size > BACK_LIMIT)
			{
				backLog.put(tag, backLog.get(tag).subList(size - BACK_LIMIT, size));
			}
		}
		else
		{
			return;
		}
		
		System.out.println(log);
	}
	
	public static void error(String name, String...strings)
	{
		if (LEVEL_ERROR <= currentLevel)
		{
			log(TAG_ERROR, name, strings);
		}
	}
	
	public static void warn(String name, String...strings)
	{
		if (LEVEL_WARNING <= currentLevel)
		{
			log(TAG_WARNING, name, strings);
		}
	}
	
	public static void info(String name, String...strings)
	{
		if (LEVEL_INFO <= currentLevel)
		{
			log(TAG_INFO, name, strings);
		}
	}
	
	public static void trace(String name, String...strings)
	{
		if (LEVEL_TRACE <= currentLevel)
		{
			log(TAG_TRACE, name, strings);
		}
	}
}
