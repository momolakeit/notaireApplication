package com.momo.notaireApplication.repository;

import com.momo.notaireApplication.model.db.FichierDocument;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FichierDocumentRepository extends JpaRepository<FichierDocument,Long> {
}
