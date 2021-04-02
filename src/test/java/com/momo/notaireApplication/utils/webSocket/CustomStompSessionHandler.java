package com.momo.notaireApplication.utils.webSocket;

import com.momo.notaireApplication.model.db.Conversation;
import com.momo.notaireApplication.model.dto.ConversationDTO;
import com.momo.notaireApplication.model.dto.MessagesDTO;
import com.momo.notaireApplication.testUtils.ObjectTestUtils;
import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
/**For testing websocket**/
public class CustomStompSessionHandler extends StompSessionHandlerAdapter {

    public BlockingQueue<ConversationDTO> blockingQueue = new ArrayBlockingQueue(1);

    public MessagesDTO messagesDTO;

    public Conversation conversation;

    public String PORT;

    @Override
    public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
        stompSession.subscribe("/conversation/"+conversation.getId(),this);
        stompSession.send("/app/addMessage/"+conversation.getId(),messagesDTO);
    }

    @Override
    public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {

    }

    @Override
    public void handleTransportError(StompSession stompSession, Throwable throwable) {

    }

    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
        return null;
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object o) {
            blockingQueue.add((ConversationDTO)o);
    }
}
