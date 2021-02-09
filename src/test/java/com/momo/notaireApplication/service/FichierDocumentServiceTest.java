package com.momo.notaireApplication.service;

import com.momo.notaireApplication.exception.FichierDocumentNotFoundException;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.FichierDocument;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.repository.FichierDocumentRepository;
import com.momo.notaireApplication.service.encryption.EncryptionService;
import com.momo.notaireApplication.testUtils.TestDocumentUtils;
import org.bouncycastle.cms.CMSException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FichierDocumentServiceTest {
    @InjectMocks
    private FichierDocumentService fichierDocumentService;

    @Mock
    private FichierDocumentRepository fichierDocumentRepository;

    @Mock
    private NotaireService notaireService;

    @Mock
    private ClientService clientService;

    @Mock
    private CloudMersiveService cloudMersiveService;

    @Mock
    private EncryptionService encryptionService;

    private final String NOM_FICHIER_WORD = "testDocuments.docx";
    private final String NOM_FICHIER_PDF = "testDocuments.pdf";

    private final Long documentID =55L;


    @Test
    public void testAvecFichierWordAppelCloudMersive() throws IOException, CertificateException, CMSException, NoSuchProviderException {
        init();
        Mockito.when(cloudMersiveService.convertDocxToPDF(any(byte[].class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        Notaire notaire = NotaireServiceTest.initNotaire();
        Client client = ClientServiceTest.initClient();
        MockMultipartFile file = new MockMultipartFile("testDocuments", NOM_FICHIER_WORD, "multipart/form-data", TestDocumentUtils.initWordDocument());
        FichierDocument fichier = fichierDocumentService.createDocument(1L, 1L, file);
        Mockito.verify(cloudMersiveService, times(1)).convertDocxToPDF(any());
        Mockito.verify(encryptionService, times(1)).encryptData(any(byte[].class));
        assertEquals(notaire.getNom(), fichier.getNotaire().getNom());
        assertEquals(notaire.getEmail(), fichier.getNotaire().getEmail());
        assertEquals(notaire.getPrenom(), fichier.getNotaire().getPrenom());
        assertEquals(client.getNom(), fichier.getClient().get(0).getNom());
        assertEquals(client.getEmail(), fichier.getClient().get(0).getEmail());
        assertEquals(client.getPrenom(), fichier.getClient().get(0).getPrenom());

    }

    @Test
    public void testGetDocumentNotFoundLanceException() {
        when(fichierDocumentRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(FichierDocumentNotFoundException.class, () -> {
            fichierDocumentService.getDocument(Long.valueOf(1));
        });
    }

    @Test
    public void testGetDocument() {
        LocalDateTime localDateTime = LocalDateTime.now();
        FichierDocument fichierDocument = initFichierDocument(localDateTime);
        when(fichierDocumentRepository.findById(anyLong())).thenReturn(Optional.of(fichierDocument));
        FichierDocument document =fichierDocumentService.getDocument(documentID);
        assertEquals(documentID,document.getId());
        assertEquals(localDateTime,document.getLocalDateTime());
    }

    @Test
    public void testAvecFichierPdfAppelCloudMersive() throws IOException, CertificateException, CMSException, NoSuchProviderException {
        init();
        Notaire notaire = NotaireServiceTest.initNotaire();
        Client client = ClientServiceTest.initClient();
        MockMultipartFile file = new MockMultipartFile("testDocuments", NOM_FICHIER_PDF, "multipart/form-data", TestDocumentUtils.initPDFDocument());
        FichierDocument fichier = fichierDocumentService.createDocument(1L, 1L, file);
        Mockito.verify(cloudMersiveService, times(0)).convertDocxToPDF(any());
        Mockito.verify(encryptionService, times(1)).encryptData(any(byte[].class));
        assertEquals(notaire.getNom(), fichier.getNotaire().getNom());
        assertEquals(notaire.getEmail(), fichier.getNotaire().getEmail());
        assertEquals(notaire.getPrenom(), fichier.getNotaire().getPrenom());
        assertEquals(client.getNom(), fichier.getClient().get(0).getNom());
        assertEquals(client.getEmail(), fichier.getClient().get(0).getEmail());
        assertEquals(client.getPrenom(), fichier.getClient().get(0).getPrenom());

    }

    private void init() throws IOException {
        Mockito.when(notaireService.getNotaire(anyLong())).thenReturn(NotaireServiceTest.initNotaire());
        Mockito.when(clientService.findClient(anyLong())).thenReturn(ClientServiceTest.initClient());
        Mockito.when(fichierDocumentRepository.save(any(FichierDocument.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
    }

    private FichierDocument initFichierDocument(LocalDateTime localDateTime) {
        FichierDocument fichierDocument = new FichierDocument();
        fichierDocument.setLocalDateTime(localDateTime);
        fichierDocument.setId(documentID);
        return fichierDocument;
    }


}