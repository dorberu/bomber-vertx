package com.dorberu;

import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;

public class EventBusMessageHandler
{
	public static int HANDLER_SIZE = 20;
	
    public interface MessageReceiver
    {
        public abstract void receivePacket(String handlerId, JsonObject packetData);
    }

    protected String _globalAddr;
    protected String _localAddr;
    protected MessageReceiver _receiver;
    private MessageConsumer<Buffer> _consumer;
    
    public EventBusMessageHandler(Vertx vertx,
                                  MessageReceiver receiver,
                                  String localAddress,
                                  String globalAddress)
    {
        _receiver = receiver;
        if (globalAddress != null)
        {
            _globalAddr = globalAddress;
            _consumer = vertx.eventBus().consumer(globalAddress);
        }
        if (localAddress != null && (globalAddress == null || !localAddress.equals(globalAddress)))
        {
            _localAddr = localAddress;
            _consumer = vertx.eventBus().consumer(localAddress);
        }

        if (_consumer != null) {
            _consumer.handler(message -> {
                try {
                	Buffer buffer = message.body();
                	String handlerId = buffer.getString(0, HANDLER_SIZE, "UTF-8");
                	String json = buffer.getString(HANDLER_SIZE, buffer.length(), "UTF-8");
                	
                	JsonObject data = new JsonObject(json);
            		_receiver.receivePacket(handlerId, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
