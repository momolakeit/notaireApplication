package com.momo.notaireApplication.controller;

import com.momo.notaireApplication.mapping.FichierDocumentMapper;
import com.momo.notaireApplication.model.dto.FichierDocumentDTO;
import com.momo.notaireApplication.model.request.CreateFichierDocumentRequestDTO;
import com.momo.notaireApplication.service.FichierDocumentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/fichierDocument")
public class FichierDocumentController {
    private FichierDocumentService fichierDocumentService;

    public FichierDocumentController(FichierDocumentService fichierDocumentService) {
        this.fichierDocumentService = fichierDocumentService;
    }

    @PostMapping
    public FichierDocumentDTO createFichierDocument(@RequestParam("file") MultipartFile file, @RequestBody CreateFichierDocumentRequestDTO createFichierDocumentRequestDTO) {
        return FichierDocumentMapper.instance.toDTO(this.fichierDocumentService.createDocument(
                createFichierDocumentRequestDTO.getClientDTO().getId(),
                createFichierDocumentRequestDTO.getNotaireDTO().getId(),
                file));
    }

    @GetMapping("/fichierDocument/{documentId}")
    public FichierDocumentDTO getFichierDocument(@RequestParam Long documentId){
        return FichierDocumentMapper.instance.toDTO(this.fichierDocumentService.getDocument(documentId));
    }
}
