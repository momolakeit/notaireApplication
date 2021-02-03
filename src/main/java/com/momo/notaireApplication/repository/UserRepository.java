package com.momo.notaireApplication.repository;

import com.momo.notaireApplication.model.db.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
