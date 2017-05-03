package com.dorberu;

import io.vertx.core.AbstractVerticle;

public class GameVerticle extends AbstractVerticle {
	
	public static final double FPS = 30.0f;
	
	protected static final GameServer _gameServer = GameServer.getInstance();
	
    @Override
    public void start()
    {
    	_gameServer.onInit(vertx);

        Fps fps = new Fps(vertx);
        fps.start(this::onTick, FPS);
    }

    protected void onTick(Long microDelay)
    {
    	_gameServer.onTick(microDelay);
    }
}