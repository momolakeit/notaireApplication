package com.momo.notaireApplication.repositories;

import com.momo.notaireApplication.model.db.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,Long> {
}
