package com.momo.notaireApplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momo.notaireApplication.mapping.ClientMapper;
import com.momo.notaireApplication.mapping.NotaireMapper;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.FichierDocument;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.request.CreateFactureRequestDTO;
import com.momo.notaireApplication.model.request.CreateFichierDocumentRequestDTO;
import com.momo.notaireApplication.repository.ClientRepository;
import com.momo.notaireApplication.repository.FichierDocumentRepository;
import com.momo.notaireApplication.repository.NotaireRepository;
import com.momo.notaireApplication.testUtils.TestDocumentUtils;
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

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class FichierDocumentControllerTest {

    @Autowired
    private FichierDocumentController fichierDocumentController;

    @Autowired
    private FichierDocumentRepository fichierDocumentRepository;

    @Autowired
    private NotaireRepository notaireRepository;

    @Autowired
    private ClientRepository clientRepository;

    private FichierDocument fichierDocument;

    private Client client;

    private Notaire notaire;

    @BeforeEach
    public void init() {
        fichierDocument = new FichierDocument();
        fichierDocument = fichierDocumentRepository.save(fichierDocument);
        client = clientRepository.save(new Client());
        notaire = notaireRepository.save(new Notaire());
    }

    @Test
    public void testFetchFacture() throws Exception {
        MockMvc mvc = initMockMvc();
        mvc.perform(MockMvcRequestBuilders.get("/fichierDocument/{documentId}", fichierDocument.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateFacture() throws Exception {
        MockMvc mvc = initMockMvc();
        MockMultipartFile file = new MockMultipartFile("file", "mysuperfile.pdf", "multipart/form-data", TestDocumentUtils.initPDFDocument());
        CreateFichierDocumentRequestDTO createFichierDocumentRequestDTO = new CreateFichierDocumentRequestDTO();
        createFichierDocumentRequestDTO.setClientDTO(ClientMapper.instance.toDTO(client));
        createFichierDocumentRequestDTO.setNotaireDTO(NotaireMapper.instance.toDTO(notaire));

        mvc.perform(MockMvcRequestBuilders.multipart("/fichierDocument", fichierDocument.getId())
                .file(file)
                .content(new ObjectMapper().writeValueAsString(createFichierDocumentRequestDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    private MockMvc initMockMvc() {
        return MockMvcBuilders.standaloneSetup(fichierDocumentController).build();
    }

}