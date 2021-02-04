package com.momo.notaireApplication.service;

import com.momo.notaireApplication.model.db.FichierDocument;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.repository.NotaireRepository;
import com.momo.notaireApplication.utils.ListUtil;
import org.springframework.stereotype.Service;

@Service
public class NotaireService {
    private NotaireRepository notaireRepository;

    public NotaireService(NotaireRepository notaireRepository) {
        this.notaireRepository = notaireRepository;
    }

    //TODO gerer avec une erreur 400 dans le orElse
    public Notaire getNotaire(Long notaireId) {
        return notaireRepository.findById(notaireId).orElse(null);
    }

    public Notaire ajouterFichierDocument(Notaire notaire, FichierDocument fichierDocument) {
        notaire.setFichierDocuments(ListUtil.initList(notaire.getFichierDocuments()));
        notaire.getFichierDocuments().add(fichierDocument);
        return notaire;
    }

    public Notaire saveNotaire(Notaire notaire) {
        return this.notaireRepository.save(notaire);
    }
}
