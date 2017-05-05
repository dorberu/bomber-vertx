package com.dorberu;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;

public class WebSocketServer implements EventBusMessageHandler.MessageReceiver
{
	protected static final WebSocketServer _this = new WebSocketServer();

	protected Vertx _vertx;
	protected EventBusMessageHandler _eventBusMessageHandler;
	
	private Map<String, ServerWebSocket> _socketList;
	
    public static WebSocketServer getInstance()
    {
        return _this;
    }

    public boolean onInit(Vertx vertx)
    {
    	System.out.println(getClass().getName() + " DEBUG " + "init");

    	_vertx = vertx;
    	_eventBusMessageHandler = new EventBusMessageHandler(_vertx, this, "websocketserver.local", "websocketserver.global");
    	
    	_socketList = new HashMap<String, ServerWebSocket>();
        HttpServer server = _vertx.createHttpServer();
        server.websocketHandler(websocket ->
        {
            System.out.println(getClass().getName() + " DEBUG " + "connect");
            
            String handlerId = RandomStringUtils.random(20, "0123456789abcdefghijklmnopqrstuvwxyz");
            _socketList.put(handlerId, websocket);
            
            websocket.handler(new Handler<Buffer>()
            {
                @Override
                public void handle(final Buffer buffer)
                {
                    _vertx.eventBus().send("gameserver.local", Buffer.buffer(handlerId).appendBuffer(buffer));
                }
            });

            websocket.closeHandler(new Handler<Void>()
            {
                @Override
                public void handle(Void event)
                {
                	_socketList.remove(handlerId);
                	System.out.println(getClass().getName() + " DEBUG " + "close");
                }
            });
        }).listen(8080);
        
        System.out.println(getClass().getName() + " DEBUG " + "init completed");
        return true;
    }
    
    public void sendMessage(String handlerId, String data)
    {
    	if (_socketList.get(handlerId) != null)
    	{
    		System.out.println(getClass().getName() + " DEBUG " + "sendMessage " + data);
    		_socketList.get(handlerId).writeFinalTextFrame(data);
    	}
    }
    
    @Override
    public boolean onMessagePacketBytesReceive(String handlerId, String packetData) {
        System.out.println(getClass().getName() + " DEBUG " + "onMessagePacketBytesReceive " + handlerId + " : " + packetData);
        sendMessage(handlerId, packetData);
        return true;
    }

    @Override
    public void onMessageClose() {
    }
}
