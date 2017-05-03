package com.dorberu;

import io.vertx.core.AbstractVerticle;

public class WebSocketVerticle extends AbstractVerticle
{
	public static final double FPS = 30.0f;
	
	protected static final WebSocketServer _webSocketServer = WebSocketServer.getInstance();
	
    @Override
    public void start()
    {
    	System.out.println(getClass().getName() + " DEBUG " + "start");

    	_webSocketServer.onInit(vertx);
    }
}