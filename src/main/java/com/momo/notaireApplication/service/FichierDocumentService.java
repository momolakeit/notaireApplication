package com.momo.notaireApplication.service;

import com.momo.notaireApplication.exception.validation.notFound.FichierDocumentNotFoundException;
import com.momo.notaireApplication.model.db.*;
import com.momo.notaireApplication.repositories.FichierDocumentRepository;
import com.momo.notaireApplication.service.encryption.EncryptionService;
import com.momo.notaireApplication.service.pdf.CloudMersiveService;
import com.momo.notaireApplication.service.pdf.ITextService;
import com.momo.notaireApplication.utils.ListUtil;
import org.apache.commons.io.FilenameUtils;
import org.bouncycastle.cms.CMSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;

@Service
@Transactional
public class FichierDocumentService {
    private CloudMersiveService cloudMersiveService;
    private FichierDocumentRepository fichierDocumentRepository;
    private EncryptionService encryptionService;
    private ITextService iTextService;
    private UserService userService;
    private RendezVousService rendezVousService;
    private static final Logger LOGGER = LoggerFactory.getLogger(FichierDocumentService.class);
    private String PDF_FILETYPE = "pdf";

    public FichierDocumentService(CloudMersiveService cloudMersiveService, FichierDocumentRepository fichierDocumentRepository, EncryptionService encryptionService, ITextService iTextService, UserService userService, RendezVousService rendezVousService) {
        this.cloudMersiveService = cloudMersiveService;
        this.fichierDocumentRepository = fichierDocumentRepository;
        this.encryptionService = encryptionService;
        this.iTextService = iTextService;
        this.userService = userService;
        this.rendezVousService = rendezVousService;
        this.PDF_FILETYPE = PDF_FILETYPE;
    }

    //todo gerer le commentaire passé pour le fichier document
    public FichierDocument createDocument(Long clientId, Long notaireId, Long rendezVousId) {
        Notaire notaire = (Notaire) userService.getUser(notaireId);
        Client client = (Client) userService.getUser(clientId);
        RendezVous rendezVous = rendezVousService.getRendezVous(rendezVousId);
        FichierDocument fichierDocument = initDocument(notaire, client,rendezVous);
        linkDocumentAndItems(notaire, client, fichierDocument,rendezVous);
        return fichierDocument;

    }

    //todo gerer exception d'encryption , decryptage
    public FichierDocument getDocument(Long documentId) {
        FichierDocument fichierDocument = fichierDocumentRepository.findById(documentId).orElseThrow(FichierDocumentNotFoundException::new);
        try {
            fichierDocument.setData(encryptionService.decryptData(fichierDocument.getData()));
        } catch (Exception e) {
            LOGGER.info("Erreur dans le decryptage de donnée", e);
        }
        return fichierDocument;

    }

    public byte[] getFichierDocumentData(Long id) {
        return getDocument(id).getData();
    }

    public FichierDocument signDocument(Long documentId, String location) {
        FichierDocument fichierDocument = getDocument(documentId);
        fichierDocument.setData(iTextService.sign(fichierDocument.getData(), fichierDocument.getDescription(), location));
        return saveDocument(fichierDocument);
    }

    public void saveDocumentFile(Long fichierDocumentId, MultipartFile file) throws CertificateException, NoSuchProviderException, CMSException, IOException {
        byte[] bytes = getBytesDuFichier(file);
        FichierDocument fichierDocument = getDocument(fichierDocumentId);
        fichierDocument.setData(encryptionService.encryptData(bytes));
        saveDocument(fichierDocument);
    }

    private FichierDocument initDocument(Notaire notaire, Client client, RendezVous rendezVous) {
        FichierDocument fichierDocument = new FichierDocument();
        fichierDocument.setUsers(new ArrayList<>(Arrays.asList(client, notaire)));
        fichierDocument.setRendezVous(rendezVous);
        fichierDocument = saveDocument(fichierDocument);
        return fichierDocument;
    }

    private void linkDocumentAndItems(Notaire notaire, Client client, FichierDocument fichierDocument,RendezVous rendezVous) {
        linkUserEtFichier(client, fichierDocument);
        linkUserEtFichier(notaire, fichierDocument);
        linkRendezVousEtFichier(rendezVous,fichierDocument);
    }

    private void linkUserEtFichier(User user, FichierDocument fichierDocument) {
        ListUtil.ajouterObjectAListe(fichierDocument, user.getFichierDocuments());
        userService.saveUser(user);
    }
    private void linkRendezVousEtFichier(RendezVous rendezVous,FichierDocument fichierDocument){
        rendezVous.setFichierDocument(fichierDocument);
        rendezVousService.saveRendezVous(rendezVous);
    }

    private FichierDocument saveDocument(FichierDocument fichierDocument) {
        return this.fichierDocumentRepository.save(fichierDocument);
    }

    private byte[] getBytesDuFichier(MultipartFile file) {
        byte[] bytes = null;
        try {
            if (!FilenameUtils.getExtension(file.getOriginalFilename()).equalsIgnoreCase(PDF_FILETYPE)) {
                bytes = cloudMersiveService.convertDocxToPDF(file.getBytes());
            } else {
                bytes = file.getBytes();
            }
        } catch (IOException e) {
            LOGGER.info("Erreur lors de l'obtention du fichierdocument", e);
        }
        return bytes;
    }
}
