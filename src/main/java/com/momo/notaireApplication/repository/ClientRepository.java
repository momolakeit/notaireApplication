package com.momo.notaireApplication.repository;

import com.momo.notaireApplication.model.db.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,Long> {
}
