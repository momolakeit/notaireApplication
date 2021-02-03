package com.momo.notaireApplication.repository;

import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Notaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaireRepository extends JpaRepository<Notaire,Long> {
}
