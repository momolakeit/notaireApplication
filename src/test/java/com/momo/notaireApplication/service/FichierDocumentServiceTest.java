package com.momo.notaireApplication.service;

import com.momo.notaireApplication.exception.FichierDocumentNotFoundException;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.FichierDocument;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.repositories.FichierDocumentRepository;
import com.momo.notaireApplication.service.encryption.EncryptionService;
import com.momo.notaireApplication.service.pdf.ITextService;
import com.momo.notaireApplication.testUtils.ObjectTestUtils;
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
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
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

    @Mock
    private ITextService iTextService;

    private final String NOM_FICHIER_WORD = "testDocuments.docx";
    private final String NOM_FICHIER_PDF = "testDocuments.pdf";

    private final Long documentID = 55L;


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
        
        Notaire notaireResult = ObjectTestUtils.findNotaireInList(fichier.getUsers());
        Client clientResult = ObjectTestUtils.findClientInList(fichier.getUsers());
        
        assertEquals(notaire.getNom(), notaireResult.getNom());
        assertEquals(notaire.getEmailAdress(), notaireResult.getEmailAdress());
        assertEquals(notaire.getPrenom(), notaireResult.getPrenom());
        assertEquals(client.getNom(), clientResult.getNom());
        assertEquals(client.getEmailAdress(), clientResult.getEmailAdress());
        assertEquals(client.getPrenom(), clientResult.getPrenom());

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
        FichierDocument document = fichierDocumentService.getDocument(documentID);
        assertEquals(documentID, document.getId());
        assertEquals(localDateTime, document.getLocalDateTime());
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
       
        Notaire notaireResult = ObjectTestUtils.findNotaireInList(fichier.getUsers());
        Client clientResult = ObjectTestUtils.findClientInList(fichier.getUsers());
       
        assertEquals(notaire.getNom(), notaireResult.getNom());
        assertEquals(notaire.getEmailAdress(), notaireResult.getEmailAdress());
        assertEquals(notaire.getPrenom(), notaireResult.getPrenom());
        assertEquals(client.getNom(), clientResult.getNom());
        assertEquals(client.getEmailAdress(), clientResult.getEmailAdress());
        assertEquals(client.getPrenom(), clientResult.getPrenom());

    }

    @Test
    public void testSignDocument() throws IOException, CertificateException, CMSException, NoSuchProviderException, InvalidKeySpecException, NoSuchAlgorithmException {
        //init();
        byte[] bytes =TestDocumentUtils.initPDFDocument();
        FichierDocument fichierDocument = initFichierDocumentPDF(LocalDateTime.now());
        when(fichierDocumentRepository.findById(anyLong())).thenReturn(Optional.of(fichierDocument));
        Mockito.when(fichierDocumentRepository.save(any(FichierDocument.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(iTextService.sign(any(byte[].class),anyString(),anyString())).thenReturn(bytes);
        Mockito.when(encryptionService.decryptData(any(byte[].class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        FichierDocument fichier = fichierDocumentService.signDocument(1L, "canada" );
        assertEquals(bytes,fichier.getData());

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
        fichierDocument.setDescription("cest pour me donner les dollars");
        return fichierDocument;
    }

    private FichierDocument initFichierDocumentPDF(LocalDateTime localDateTime) throws IOException {
        FichierDocument fichierDocument =initFichierDocument(localDateTime);
        fichierDocument.setData(TestDocumentUtils.initPDFDocument());
        return fichierDocument;
    }


}