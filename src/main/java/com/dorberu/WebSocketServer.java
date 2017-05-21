package com.dorberu;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;

import com.dorberu.packet.LogoutPacket;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;

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
    	Log.info(getClass().getName(), "onInit");

    	_vertx = vertx;
    	_eventBusMessageHandler = new EventBusMessageHandler(_vertx, this, "websocketserver.local", "websocketserver.global");
    	
    	_socketList = new HashMap<String, ServerWebSocket>();
        HttpServer server = _vertx.createHttpServer();
        server.websocketHandler(websocket ->
        {
            String handlerId = RandomStringUtils.random(EventBusMessageHandler.HANDLER_SIZE, "0123456789abcdefghijklmnopqrstuvwxyz");
            _socketList.put(handlerId, websocket);
            Log.info(getClass().getName(), "connect", "handlerId: " + handlerId);
            
            websocket.handler(new Handler<Buffer>()
            {
                @Override
                public void handle(final Buffer buffer)
                {
                    try {
                    	Buffer sendBuffer = Buffer.buffer(handlerId.getBytes("UTF-8")).appendBuffer(buffer);
						_vertx.eventBus().send("gameserver.local", sendBuffer);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
                }
            });

            websocket.closeHandler(new Handler<Void>()
            {
                @Override
                public void handle(Void event)
                {
                	_socketList.remove(handlerId);
                	try {
	                	Buffer logoutBuffer = Buffer.buffer(LogoutPacket.getDummyPacket().toString().getBytes("UTF-8"));
	                	Buffer buffer = Buffer.buffer(handlerId.getBytes("UTF-8")).appendBuffer(logoutBuffer);
	                	_vertx.eventBus().send("gameserver.local", buffer);
                	} catch (UnsupportedEncodingException e) {
            			e.printStackTrace();
            		}
                }
            });
        }).listen(8080);

    	Log.info(getClass().getName(), "onInit", "complete");
        return true;
    }
    
    public void sendMessage(String handlerId, JsonObject data)
    {
    	if (_socketList.get(handlerId) != null)
    	{
    		_socketList.get(handlerId).writeFinalTextFrame(data.toString());
            Log.trace(getClass().getName(), "sendMessage", "handlerId: " + handlerId, "packetData: " + data.toString());
    	}
    }
    
    @Override
    public void receivePacket(String handlerId, JsonObject packetData)
    {
        Log.trace(getClass().getName(), "receivePacket", "handlerId: " + handlerId, "packetData: " + packetData);
    	sendMessage(handlerId, packetData);
    }
}
