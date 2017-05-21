package com.dorberu;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;

public class GameVerticle extends AbstractVerticle
{	
	public static final double FPS = 30.0f;
	
	protected static final GameServer _gameServer = GameServer.getInstance();
	
    @Override
    public void start()
    {
    	Log.info(getClass().getName(), "start");
    	
    	_gameServer.onInit(vertx);

        Fps fps = new Fps(vertx);
        fps.start(this::onTick, FPS);
        
        DeploymentOptions options = new DeploymentOptions().setWorker(true);
        vertx.deployVerticle("com.dorberu.WebSocketVerticle", options);
    }

    protected void onTick(Long microDelay)
    {
    	_gameServer.onTick(microDelay);
    }
}