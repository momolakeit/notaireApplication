package com.momo.notaireApplication.repositories;

import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RendezVousRepository extends JpaRepository<RendezVous,Long> {
}
