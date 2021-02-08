package com.momo.notaireApplication.testUtils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestDocumentUtils {
    private static final String FILE_PATH = "\\src\\test\\resources\\";
    private static final String NOM_FICHIER_WORD = "testDocuments.docx";
    private static final String NOM_FICHIER_PDF = "testDocuments.pdf";

    public static byte[] initWordDocument() throws IOException {
        return initDocumentByteArray(NOM_FICHIER_WORD);
    }
    public static byte[] initPDFDocument() throws IOException {
        return initDocumentByteArray(NOM_FICHIER_PDF);
    }
    private static byte[] initDocumentByteArray(String filename) throws IOException {
        Path currentRelativePath = Paths.get("");
        String absolutePath = currentRelativePath.toAbsolutePath().toString();
        String pathDansProjet = FILE_PATH + filename;
        File file = new File(absolutePath + pathDansProjet);
        return FileUtils.readFileToByteArray(file);
    }
}
