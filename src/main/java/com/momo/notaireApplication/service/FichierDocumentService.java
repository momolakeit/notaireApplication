package com.momo.notaireApplication.service;

import com.momo.notaireApplication.exception.FichierDocumentNotFoundException;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.FichierDocument;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.repository.FichierDocumentRepository;
import com.momo.notaireApplication.service.encryption.EncryptionService;
import com.momo.notaireApplication.utils.ListUtil;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class FichierDocumentService {
    private CloudMersiveService cloudMersiveService;
    private FichierDocumentRepository fichierDocumentRepository;
    private NotaireService notaireService;
    private ClientService clientService;
    private EncryptionService encryptionService;
    private static final Logger LOGGER = LoggerFactory.getLogger(FichierDocumentService.class);
    private String PDF_FILETYPE = "pdf";


    public FichierDocumentService(CloudMersiveService cloudMersiveService, FichierDocumentRepository fichierDocumentRepository, NotaireService notaireService, ClientService clientService, EncryptionService encryptionService) {
        this.cloudMersiveService = cloudMersiveService;
        this.fichierDocumentRepository = fichierDocumentRepository;
        this.notaireService = notaireService;
        this.clientService = clientService;
        this.encryptionService = encryptionService;
    }

    public FichierDocument createDocument(Long clientId, Long notaireId, MultipartFile file) throws IOException {
        byte[] bytes = getBytesDuFichier(file);
        Notaire notaire = this.notaireService.getNotaire(notaireId);
        Client client = this.clientService.findClient(clientId);
        FichierDocument fichierDocument = initDocument(bytes, notaire, client);
        linkDocumentAndItems(notaire, client, fichierDocument);
        return fichierDocument;

    }
    //todo gerer exception d'encryption , decryptage
    public FichierDocument getDocument(Long documentId){
        FichierDocument fichierDocument = fichierDocumentRepository.findById(documentId).orElseThrow(FichierDocumentNotFoundException::new);
        try{
            fichierDocument.setData(encryptionService.decryptData(fichierDocument.getData()));
        }catch (Exception e){
            LOGGER.info("Erreur dans le decryptage de donnée", e);
        }
        return fichierDocument;

    }
    private FichierDocument initDocument(byte[] bytes, Notaire notaire, Client client) {
        FichierDocument fichierDocument = new FichierDocument();
        fichierDocument.setNotaire(notaire);
        fichierDocument.setClient(new ArrayList<>(Arrays.asList(client)));
        try{
            fichierDocument.setData(encryptionService.encryptData(bytes));

        }catch (Exception e)
        {
            LOGGER.info("Erreur lors de l'encryption du fichier PDF :",e);
        }
        fichierDocument = saveDocument(fichierDocument);
        return fichierDocument;
    }

    private void linkDocumentAndItems(Notaire notaire, Client client, FichierDocument fichierDocument) {
        linkNotaireEtDocument(notaire, fichierDocument);
        linkClientEtDocument(client, fichierDocument);
    }

    private void linkClientEtDocument(Client client, FichierDocument fichierDocument) {
        ListUtil.ajouterObjectAListe(fichierDocument,client.getFichierDocuments());
        this.clientService.saveClient(client);
    }

    private void linkNotaireEtDocument(Notaire notaire, FichierDocument fichierDocument) {
        ListUtil.ajouterObjectAListe(fichierDocument,notaire.getFichierDocuments());
        this.notaireService.saveNotaire(notaire);
    }

    private FichierDocument saveDocument(FichierDocument fichierDocument) {
        return this.fichierDocumentRepository.save(fichierDocument);
    }

    private byte[] getBytesDuFichier(MultipartFile file) throws IOException {
        byte[] bytes;
        if (!FilenameUtils.getExtension(file.getOriginalFilename()).equalsIgnoreCase(PDF_FILETYPE)) {
            bytes = cloudMersiveService.convertDocxToPDF(file.getBytes());
        } else {
            bytes = file.getBytes();
        }
        return bytes;
    }
}
