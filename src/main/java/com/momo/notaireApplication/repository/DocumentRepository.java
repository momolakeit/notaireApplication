package com.momo.notaireApplication.repository;

import com.momo.notaireApplication.model.db.Document;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DocumentRepository extends JpaRepository<Document,Long> {
}
