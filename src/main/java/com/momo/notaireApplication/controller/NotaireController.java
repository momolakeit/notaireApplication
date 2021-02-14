package com.momo.notaireApplication.controller;

import com.momo.notaireApplication.mapping.NotaireMapper;
import com.momo.notaireApplication.model.dto.NotaireDTO;
import com.momo.notaireApplication.service.NotaireService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notaire")
public class NotaireController {
    private NotaireService notaireService;

    public NotaireController(NotaireService notaireService) {
        this.notaireService = notaireService;
    }

    @GetMapping("/getNotaire/{notaireId}")
    public NotaireDTO getNotaire(@PathVariable final Long notaireId){
        return this.notaireService.getNotaireDTO(notaireId);
    }
    @GetMapping("/getNotaireByEmail/{notaireEmail:.+}")
    public NotaireDTO getNotaire(@PathVariable final String notaireEmail){
        return this.notaireService.findNotaireDTOByEmail(notaireEmail);
    }

}
