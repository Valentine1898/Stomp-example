import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Stomp {

    public static void main(String[] args) throws InterruptedException {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        CountDownLatch latch = new CountDownLatch(1);

        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new SimpleMessageConverter());


        String url = "ws://api.buycrypt.com/streams";
        StompSessionHandler sessionHandler = new StompSessionHandlerAdapter() {

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                super.handleFrame(headers, payload);
                System.out.println(payload);
                System.out.println(headers);
            }


            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                System.out.println("Connected");
                super.afterConnected(session, connectedHeaders);

                session.subscribe("/topic/fastTrade/info/sell", new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders stompHeaders) {
                        return byte[].class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        System.out.println("Received greeting " + new String((byte[]) payload));
                    }

                });



            }
        };
        stompClient.connect(url, sessionHandler);
        latch.await();




    }


}
