package com.dorberu;

import io.vertx.core.Vertx;

public class GameServer
{
	protected static final GameServer _this = new GameServer();
	
	protected Vertx _vertx;
	
	long _coolFrame = 0;
	
    public static GameServer getInstance()
    {
        return _this;
    }
    
    public boolean onInit(Vertx vertx)
    {
        System.out.println(getClass().getName() + " DEBUG " + "init");
        _vertx = vertx;
        return true;
    }

    protected void onTick(Long microDelay)
    {
        if (++_coolFrame >= 90)
        {
            _coolFrame = 0;
            System.out.println(getClass().getName() + " DEBUG " + " onTick " + microDelay);
        }
    }
}
