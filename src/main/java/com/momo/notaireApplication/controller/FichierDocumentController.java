package com.momo.notaireApplication.controller;

import com.momo.notaireApplication.mapping.FichierDocumentMapper;
import com.momo.notaireApplication.model.dto.FichierDocumentDTO;
import com.momo.notaireApplication.model.request.CreateFichierDocumentRequestDTO;
import com.momo.notaireApplication.model.request.SignDocumentDTO;
import com.momo.notaireApplication.service.FichierDocumentService;
import org.bouncycastle.cms.CMSException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

@RestController
@RequestMapping("/fichierDocument")
public class FichierDocumentController extends BaseController {
    private FichierDocumentService fichierDocumentService;

    public FichierDocumentController(FichierDocumentService fichierDocumentService) {
        this.fichierDocumentService = fichierDocumentService;
    }

    @PostMapping
    public FichierDocumentDTO createFichierDocument(@RequestBody CreateFichierDocumentRequestDTO createFichierDocumentRequestDTO) {
        return FichierDocumentMapper.instance.toDTO(this.fichierDocumentService.createDocument(
                createFichierDocumentRequestDTO.getClientId(),
                createFichierDocumentRequestDTO.getNotaireId(),
                createFichierDocumentRequestDTO.getRendezVousId(),
                createFichierDocumentRequestDTO.getDescription()));
    }

    @PostMapping("/sign")
    public FichierDocumentDTO signDocument(@RequestBody SignDocumentDTO signDocumentDTO) {
        return FichierDocumentMapper.instance.toDTO(this.fichierDocumentService.signDocument(
                signDocumentDTO.getClientId(),
                signDocumentDTO.getLocation()));
    }

    @PostMapping("/upload/{documentId}")
    public ResponseEntity uploadFichierDocument(@RequestParam("file") MultipartFile file, @PathVariable final Long documentId) throws CertificateException, NoSuchProviderException, CMSException, IOException {
        this.fichierDocumentService.saveDocumentFile(documentId, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{documentId}")
    public FichierDocumentDTO getFichierDocument(@PathVariable Long documentId) {
        return FichierDocumentMapper.instance.toDTO(this.fichierDocumentService.getDocument(documentId));
    }

    @GetMapping("/data/{documentId}")
    public byte[] getFichierDocumentData(@PathVariable Long documentId) {
        return this.fichierDocumentService.getFichierDocumentData(documentId);
    }
}
