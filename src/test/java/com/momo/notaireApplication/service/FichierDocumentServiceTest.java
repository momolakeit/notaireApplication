package com.momo.notaireApplication.service;

import com.momo.notaireApplication.repository.DocumentRepository;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    private final String FILE_PATH = "\\src\\test\\resources\\";
    private final String NOM_FICHIER_WORD = "testDocuments.docx";
    private final String NOM_FICHIER_PDF = "testDocuments.pdf";

    @BeforeEach
    private void init() {
        Mockito.when(notaireService.getNotaire(anyLong())).thenReturn(NotaireServiceTest.initNotaire());
        Mockito.when(clientService.findClient(anyLong())).thenReturn(ClientServiceTest.initClient());
    }

    @Test
    public void testAvecFichierWordAppelCloudMersive() throws IOException {
        MockMultipartFile file = new MockMultipartFile("testDocuments", NOM_FICHIER_WORD, "multipart/form-data",initDocumentByteArray());
        fichierDocumentService.createDocument(1L,1L,file);
        Mockito.verify(cloudMersiveService, times(1)).convertDocxToPDF(any());
    }

    private byte[] initDocumentByteArray() throws IOException {
        Path currentRelativePath = Paths.get("");
        String absolutePath = currentRelativePath.toAbsolutePath().toString();
        String pathDansProjet = FILE_PATH + NOM_FICHIER_WORD;
        File file = new File(absolutePath + pathDansProjet);
        return FileUtils.readFileToByteArray(file);
    }
}