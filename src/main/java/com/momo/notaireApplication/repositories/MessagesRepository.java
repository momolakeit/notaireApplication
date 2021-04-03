package com.momo.notaireApplication.repositories;

import com.momo.notaireApplication.model.db.Conversation;
import com.momo.notaireApplication.model.db.Messages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessagesRepository extends JpaRepository<Messages,Long> {
}
