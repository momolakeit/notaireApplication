package com.momo.notaireApplication.service;

import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.FichierDocument;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.repository.DocumentRepository;
import com.momo.notaireApplication.testUtils.TestDocumentUtils;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class FichierDocumentServiceTest {
    @InjectMocks
    private FichierDocumentService fichierDocumentService;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private NotaireService notaireService;

    @Mock
    private ClientService clientService;
    @Mock
    private CloudMersiveService cloudMersiveService;

    private final String NOM_FICHIER_WORD = "testDocuments.docx";
    private final String NOM_FICHIER_PDF = "testDocuments.pdf";

    @BeforeEach
    private void init() {
        Mockito.when(notaireService.getNotaire(anyLong())).thenReturn(NotaireServiceTest.initNotaire());
        Mockito.when(clientService.findClient(anyLong())).thenReturn(ClientServiceTest.initClient());
        Mockito.when(documentRepository.save(any(FichierDocument.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
    }

    @Test
    public void testAvecFichierWordAppelCloudMersive() throws IOException {
        Notaire notaire = NotaireServiceTest.initNotaire();
        Client client = ClientServiceTest.initClient();
        MockMultipartFile file = new MockMultipartFile("testDocuments", NOM_FICHIER_WORD, "multipart/form-data", TestDocumentUtils.initWordDocument());
        FichierDocument fichier = fichierDocumentService.createDocument(1L, 1L, file);
        Mockito.verify(cloudMersiveService, times(1)).convertDocxToPDF(any());
        assertEquals(notaire.getNom(), fichier.getNotaire().getNom());
        assertEquals(notaire.getEmail(), fichier.getNotaire().getEmail());
        assertEquals(notaire.getPrenom(), fichier.getNotaire().getPrenom());
        assertEquals(client.getNom(), fichier.getClient().get(0).getNom());
        assertEquals(client.getEmail(), fichier.getClient().get(0).getEmail());
        assertEquals(client.getPrenom(), fichier.getClient().get(0).getPrenom());

    }
    @Test
    public void testAvecFichierPdfAppelCloudMersive() throws IOException {
        Notaire notaire = NotaireServiceTest.initNotaire();
        Client client = ClientServiceTest.initClient();
        MockMultipartFile file = new MockMultipartFile("testDocuments", NOM_FICHIER_PDF, "multipart/form-data", TestDocumentUtils.initPDFDocument());
        FichierDocument fichier = fichierDocumentService.createDocument(1L, 1L, file);
        Mockito.verify(cloudMersiveService, times(0)).convertDocxToPDF(any());
        assertEquals(notaire.getNom(), fichier.getNotaire().getNom());
        assertEquals(notaire.getEmail(), fichier.getNotaire().getEmail());
        assertEquals(notaire.getPrenom(), fichier.getNotaire().getPrenom());
        assertEquals(client.getNom(), fichier.getClient().get(0).getNom());
        assertEquals(client.getEmail(), fichier.getClient().get(0).getEmail());
        assertEquals(client.getPrenom(), fichier.getClient().get(0).getPrenom());

    }


}