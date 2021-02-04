package com.momo.notaireApplication.service;

import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.FichierDocument;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.repository.DocumentRepository;
import com.momo.notaireApplication.utils.ListUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

@Service
public class FichierDocumentService {
    private CloudMersiveService cloudMersiveService;
    private DocumentRepository documentRepository;
    private NotaireService notaireService;
    private ClientService clientService;
    private String PDF_FILETYPE = "pdf";


    public FichierDocumentService(CloudMersiveService cloudMersiveService, DocumentRepository documentRepository, NotaireService notaireService, ClientService clientService) {
        this.cloudMersiveService = cloudMersiveService;
        this.documentRepository = documentRepository;
        this.notaireService = notaireService;
        this.clientService = clientService;
    }

    public FichierDocument createDocument(Long clientId, Long notaireId, MultipartFile file) throws IOException {
        byte[] bytes = getBytesDuFichier(file);
        Notaire notaire = this.notaireService.getNotaire(notaireId);
        Client client = this.clientService.findClient(clientId);
        FichierDocument fichierDocument = initDocument(bytes, notaire, client);
        linkDocumentAndItems(notaire, client, fichierDocument);
        return fichierDocument;

    }

    private FichierDocument initDocument(byte[] bytes, Notaire notaire, Client client) {
        FichierDocument fichierDocument = new FichierDocument();
        fichierDocument.setNotaire(notaire);
        fichierDocument.setData(bytes);
        fichierDocument.setClient(new ArrayList<>(Arrays.asList(client)));
        fichierDocument = saveDocument(fichierDocument);
        return fichierDocument;
    }

    private void linkDocumentAndItems(Notaire notaire, Client client, FichierDocument fichierDocument) {
        linkNotaireEtDocument(notaire, fichierDocument);
        linkClientEtDocument(client, fichierDocument);
    }

    private void linkClientEtDocument(Client client, FichierDocument fichierDocument) {
        client.setFichierDocuments(ListUtil.initList(client.getFichierDocuments()));
        client.getFichierDocuments().add(fichierDocument);
        this.clientService.saveClient(client);
    }

    private void linkNotaireEtDocument(Notaire notaire, FichierDocument fichierDocument) {
        notaire.setFichierDocuments(ListUtil.initList(notaire.getFichierDocuments()));
        notaire.getFichierDocuments().add(fichierDocument);
        this.notaireService.saveNotaire(notaire);
    }

    private FichierDocument saveDocument(FichierDocument fichierDocument) {
        return this.documentRepository.save(fichierDocument);
    }

    private byte[] getBytesDuFichier(MultipartFile file) throws IOException {
        byte[] bytes;
        if (!file.getContentType().equalsIgnoreCase(PDF_FILETYPE)) {
            bytes = cloudMersiveService.convertDocxToPDF(file.getBytes());
        } else {
            bytes = file.getBytes();
        }
        return bytes;
    }
}
