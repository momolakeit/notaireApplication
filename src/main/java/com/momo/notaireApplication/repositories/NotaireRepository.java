package com.momo.notaireApplication.repositories;

import com.momo.notaireApplication.model.db.Notaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotaireRepository extends JpaRepository<Notaire,Long> {
    Optional<Notaire> findByEmailAdress(String email);
}
