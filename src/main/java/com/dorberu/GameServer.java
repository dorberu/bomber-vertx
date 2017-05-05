package com.dorberu;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;

public class GameServer implements EventBusMessageHandler.MessageReceiver
{
	protected static final GameServer _this = new GameServer();
	
	protected Vertx _vertx;
	protected EventBusMessageHandler _eventBusMessageHandler;
	
	Set<String> _handlerIds = new HashSet<>();
	
	long _coolFrame = 0;
	
    public static GameServer getInstance()
    {
        return _this;
    }
    
    public boolean onInit(Vertx vertx)
    {
        System.out.println(getClass().getName() + " DEBUG " + "init");
        
        _vertx = vertx;
        _eventBusMessageHandler = new EventBusMessageHandler(_vertx, this, "gameserver.local", "gameserver.global");
        
        return true;
    }

    protected void onTick(Long microDelay)
    {
        if (++_coolFrame >= 90)
        {
            _coolFrame = 0;
            
			try {
				for (String handlerId : _handlerIds)
				{
					Buffer buffer = Buffer.buffer(handlerId.getBytes("UTF-8"));
					buffer.appendBytes("Hello World!".getBytes("UTF-8"));
		            _vertx.eventBus().send("websocketserver.local", buffer);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
        }
    }
    
    @Override
    public boolean onMessagePacketBytesReceive(String handlerId, String packetData) {
        System.out.println(getClass().getName() + " DEBUG " + "onMessagePacketBytesReceive " + handlerId + " : " + packetData);
        _handlerIds.add(handlerId);
        return true;
    }

    @Override
    public void onMessageClose() {
    }
}
