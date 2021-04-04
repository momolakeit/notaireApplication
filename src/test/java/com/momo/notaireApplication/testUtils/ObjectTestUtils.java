package com.momo.notaireApplication.testUtils;

import com.momo.notaireApplication.mapping.UserMapper;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.User;
import com.momo.notaireApplication.model.dto.ConversationDTO;
import com.momo.notaireApplication.model.dto.MessagesDTO;
import com.momo.notaireApplication.model.dto.RendezVousDTO;
import com.momo.notaireApplication.model.dto.UserDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObjectTestUtils {
    private static final String NOM = "nom";

    private static final String PRENOM = "prenom";

    private static final String EMAIL = "email";

    private static final String STRIPE_ACCOUNT_ID = "accountId";

    private static final String MESSAGE_DEFAULT = "salut";

    public static Notaire findNotaireInList(List<User> users) {
        return (Notaire) users
                .stream()
                .filter(user -> user instanceof Notaire)
                .findFirst()
                .get();
    }

    public static Client findClientInList(List<User> users) {
        return (Client) users
                .stream()
                .filter(user -> user instanceof Client)
                .findFirst()
                .get();
    }

    public static Notaire initNotaire() {
        Notaire notaire = new Notaire();
        notaire.setId(1L);
        notaire.setNom(NOM);
        notaire.setPrenom(PRENOM);
        notaire.setEmailAdress(EMAIL);
        notaire.setStripeAccountId(STRIPE_ACCOUNT_ID);
        notaire.setRendezVous(new ArrayList<>());
        return notaire;
    }

    public static Client initClient() {
        Client client = new Client();
        client.setId(1L);
        client.setNom(NOM);
        client.setPrenom(PRENOM);
        client.setEmailAdress(EMAIL);
        client.setRendezVous(new ArrayList<>());
        return client;
    }
    public static MessagesDTO initMessageDTO() {
        MessagesDTO messagesDTO = new MessagesDTO();
        messagesDTO.setUser(UserMapper.instance.toDTO(initClient()));
        messagesDTO.setMessage(MESSAGE_DEFAULT);
        return messagesDTO;
    }
    public static ConversationDTO conversationDTO(List<User> users){
        List<UserDTO> usersDTO = users.stream()
                .map(UserMapper.instance::toDTO)
                .collect(Collectors.toList());
        ConversationDTO conversationDTO = new ConversationDTO();
        conversationDTO.setUsers(usersDTO);
        conversationDTO.setMessages(Collections.singletonList(initMessageDTO()));
        return conversationDTO;
    }
    public static RendezVousDTO rendezVousDTO(){
        RendezVousDTO rendezVousDTO = new RendezVousDTO();
        rendezVousDTO.setId(1L);
        return rendezVousDTO;
    }

    public static void assertUsers(String nom, String prenom, String email, User userToAssert) {
        assertEquals(nom, userToAssert.getNom());
        assertEquals(email, userToAssert.getEmailAdress());
        assertEquals(prenom, userToAssert.getPrenom());
    }
}
