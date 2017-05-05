package com.dorberu;

import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;

public class EventBusMessageHandler
{
    public interface MessageReceiver
    {
        public abstract boolean onMessagePacketBytesReceive(String handlerId,
                                                            String packetData);

        public abstract void onMessageClose();
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
                	String handlerId = buffer.getString(0, 20, "UTF-8");
                	String data = buffer.getString(20, buffer.length(), "UTF-8");
                    _receiver.onMessagePacketBytesReceive(handlerId, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
