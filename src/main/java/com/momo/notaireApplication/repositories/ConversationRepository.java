package com.momo.notaireApplication.repositories;

import com.momo.notaireApplication.model.db.Conversation;
import com.momo.notaireApplication.model.db.Facture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation,Long> {
}
