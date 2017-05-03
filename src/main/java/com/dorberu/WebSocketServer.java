package com.dorberu;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;

public class WebSocketServer
{
	protected static final WebSocketServer _this = new WebSocketServer();

	protected Vertx _vertx;
	
    public static WebSocketServer getInstance()
    {
        return _this;
    }

    public boolean onInit(Vertx vertx)
    {
    	System.out.println(getClass().getName() + " DEBUG " + "init");

    	_vertx = vertx;
        HttpServer server = vertx.createHttpServer();
        server.websocketHandler(websocket ->
        {
            System.out.println(getClass().getName() + " DEBUG " + "connect");
            
            websocket.handler(new Handler<Buffer>()
            {
                @Override
                public void handle(final Buffer data)
                {
                    vertx.eventBus().send("test.local", data);
                }
            });

            websocket.closeHandler(new Handler<Void>()
            {
                @Override
                public void handle(Void event)
                {
                	System.out.println(getClass().getName() + " DEBUG " + "close");
                }
            });
        }).listen(8080);
        
        System.out.println(getClass().getName() + " DEBUG " + "init completed");
        return true;
    }
}
