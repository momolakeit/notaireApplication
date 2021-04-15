package com.momo.notaireApplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.FichierDocument;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.RendezVous;
import com.momo.notaireApplication.model.request.CreateFichierDocumentRequestDTO;
import com.momo.notaireApplication.model.request.SignDocumentDTO;
import com.momo.notaireApplication.repositories.*;
import com.momo.notaireApplication.service.encryption.EncryptionService;
import com.momo.notaireApplication.testUtils.TestDocumentUtils;
import org.bouncycastle.cms.CMSException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@Transactional
class FichierDocumentControllerTest {

    @Autowired
    private FichierDocumentController fichierDocumentController;

    @Autowired
    private FichierDocumentRepository fichierDocumentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RendezVousRepository rendezVousRepository;

    @Autowired
    private EncryptionService encryptionService;


    private FichierDocument fichierDocument;

    private Client client;

    private Notaire notaire;

    private RendezVous rendezVous;

    @BeforeEach
    public void init() throws IOException, CertificateException, CMSException, NoSuchProviderException {
        fichierDocument = new FichierDocument();
        fichierDocument.setDescription("la mala");
        fichierDocument.setData(encryptionService.encryptData(TestDocumentUtils.initPDFDocument()));
        fichierDocument = fichierDocumentRepository.save(fichierDocument);
        client = userRepository.save(new Client());
        notaire = userRepository.save(new Notaire());
        rendezVous = rendezVousRepository.save(new RendezVous());
    }

    @Test
    public void testGetFichierDocument() throws Exception {
        MockMvc mvc = initMockMvc();
        mvc.perform(MockMvcRequestBuilders.get("/fichierDocument/{documentId}", fichierDocument.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetFichierDocumentNotFoundBadRequest() throws Exception {
        MockMvc mvc = initMockMvc();
        mvc.perform(MockMvcRequestBuilders.get("/fichierDocument/{documentId}", 0)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testCreateFichierDocument() throws Exception {
        MockMvc mvc = initMockMvc();
        CreateFichierDocumentRequestDTO createFichierDocumentRequestDTO = new CreateFichierDocumentRequestDTO();
        createFichierDocumentRequestDTO.setClientId(client.getId());
        createFichierDocumentRequestDTO.setNotaireId(notaire.getId());
        createFichierDocumentRequestDTO.setRendezVousId(rendezVous.getId());
        createFichierDocumentRequestDTO.setDescription("hi mate");
        mvc.perform(MockMvcRequestBuilders.post("/fichierDocument")
                .content(new ObjectMapper().writeValueAsString(createFichierDocumentRequestDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testSignDocument() throws Exception {
        MockMvc mvc = initMockMvc();
        SignDocumentDTO signDocumentDTO = new SignDocumentDTO();
        signDocumentDTO.setDocumentId(fichierDocument.getId());
        signDocumentDTO.setLocation("macarena");
        mvc.perform(MockMvcRequestBuilders.post("/fichierDocument/sign")
                .content(new ObjectMapper().writeValueAsString(signDocumentDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testUploadFichierDocumentData() throws Exception {
        MockMvc mvc = initMockMvc();
        MockMultipartFile file = new MockMultipartFile("file", "mysuperfile.pdf", "multipart/form-data", TestDocumentUtils.initPDFDocument());
        mvc.perform(MockMvcRequestBuilders.multipart("/fichierDocument/upload/{documentId}", fichierDocument.getId())
                .file(file)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }


    private MockMvc initMockMvc() {
        return MockMvcBuilders.standaloneSetup(fichierDocumentController).build();
    }

}