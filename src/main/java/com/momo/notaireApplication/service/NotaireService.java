package com.momo.notaireApplication.service;

import com.momo.notaireApplication.exception.NotaireNotFoundException;
import com.momo.notaireApplication.exception.UserNotFoundException;
import com.momo.notaireApplication.model.db.FichierDocument;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.repository.NotaireRepository;
import com.momo.notaireApplication.utils.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotaireService {
    private NotaireRepository notaireRepository;

    @Autowired
    public NotaireService(NotaireRepository notaireRepository) {
        this.notaireRepository = notaireRepository;
    }

    public Notaire getNotaire(Long notaireId) {
        return notaireRepository.findById(notaireId).orElseThrow(NotaireNotFoundException::new);
    }

    public Notaire saveNotaire(Notaire notaire) {
        return this.notaireRepository.save(notaire);
    }
}
