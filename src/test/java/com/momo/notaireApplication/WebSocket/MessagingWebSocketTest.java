package com.momo.notaireApplication.WebSocket;

import com.momo.notaireApplication.jwt.JwtProvider;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Conversation;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.User;
import com.momo.notaireApplication.model.dto.MessagesDTO;
import com.momo.notaireApplication.repositories.ConversationRepository;
import com.momo.notaireApplication.repositories.UserRepository;
import com.momo.notaireApplication.testUtils.ObjectTestUtils;
import com.momo.notaireApplication.utils.webSocket.CustomStompSessionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MessagingWebSocketTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    private CustomStompSessionHandler stompSessionHandler = new CustomStompSessionHandler();

    private String URL;

    private Conversation conversation;

    private WebSocketStompClient webSocketStompClient;

    private List<User> users;

    private String jwtResponse;

    //@BeforeEach
    public void init(){
        URL="http://localhost:"+port+"/addMessage/";
        Client client = ObjectTestUtils.initClient();
        client.setId(null);
        client.setPassword("pass");
        Notaire notaire = ObjectTestUtils.initNotaire();
        notaire.setId(null);
        users = new ArrayList<>(Arrays.asList(client, notaire));
        userRepository.saveAll(users);
        User user = userRepository.findAll().stream().findFirst().get();
        jwtResponse =jwtProvider.generate(user);
        conversation = new Conversation();
        conversation.setUsers(users);
        conversation = conversationRepository.save(conversation);
        conversationRepository.findAll();
        initStompClient();
    }

   // @Test
    public void testAddMessage() throws InterruptedException {
        MessagesDTO messagesDTO = getMessagesDTOWithProperClientId(ObjectTestUtils.findClientInList(users));
        this.stompSessionHandler.messagesDTO = messagesDTO;
        this.stompSessionHandler.conversation = conversation;
        this.stompSessionHandler.PORT = port.toString();
        assertNotNull(this.stompSessionHandler.blockingQueue.take());

    }

    private void initStompClient(){
        this.webSocketStompClient = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))));
        this.webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
        WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
        webSocketHttpHeaders.add("Authorization", "Bearer "+jwtResponse );
        this.webSocketStompClient.connect(URL,webSocketHttpHeaders,stompSessionHandler);

    }

    private MessagesDTO getMessagesDTOWithProperClientId(Client client) {
        MessagesDTO messagesDTO = ObjectTestUtils.initMessageDTO();
        messagesDTO.getUser().setId(client.getId());
        return messagesDTO;
    }

}
