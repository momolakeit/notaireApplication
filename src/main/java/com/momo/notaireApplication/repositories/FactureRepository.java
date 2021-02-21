package com.momo.notaireApplication.repositories;

import com.momo.notaireApplication.model.db.Facture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactureRepository extends JpaRepository<Facture,Long> {
}
