package com.momo.notaireApplication.repositories;

import com.momo.notaireApplication.model.db.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RendezVousRepository extends JpaRepository<RendezVous,Long> {
    List<RendezVous> findByUsers_Id(Long id);
}
