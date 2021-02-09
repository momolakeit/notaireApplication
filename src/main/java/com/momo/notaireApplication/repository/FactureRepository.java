package com.momo.notaireApplication.repository;

import com.momo.notaireApplication.model.db.Facture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactureRepository extends JpaRepository<Facture,Long> {
}
