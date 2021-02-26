package com.momo.notaireApplication.service;

import com.momo.notaireApplication.exception.validation.notFound.NotaireNotFoundException;
import com.momo.notaireApplication.mapping.NotaireMapper;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.dto.NotaireDTO;
import com.momo.notaireApplication.repositories.NotaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class NotaireService {
    private NotaireRepository notaireRepository;

    @Autowired
    public NotaireService(NotaireRepository notaireRepository) {
        this.notaireRepository = notaireRepository;
    }

    public Notaire getNotaire(Long notaireId) {
        return notaireRepository.findById(notaireId).orElseThrow(NotaireNotFoundException::new);
    }
    public NotaireDTO getNotaireDTO(Long notaireId) {
        return NotaireMapper.instance.toDTO(this.getNotaire(notaireId));
    }
    public Notaire findNotaireByEmail(String email) {
        return notaireRepository.findByEmailAdress(email).orElseThrow(NotaireNotFoundException::new);
    }
    public NotaireDTO findNotaireDTOByEmail(String email) {
        return NotaireMapper.instance.toDTO(this.findNotaireByEmail(email));
    }

    public Notaire saveNotaire(Notaire notaire) {
        return this.notaireRepository.save(notaire);
    }
}
