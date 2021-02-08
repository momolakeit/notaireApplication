package com.momo.notaireApplication.service;

import com.itextpdf.kernel.pdf.*;
import com.momo.notaireApplication.utils.ApplicationFileUtils;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class ITextService {
    private String OWNER_PASS ="owner";
    public byte[] encripterPdf(byte[] array){
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream inputStream = new ByteArrayInputStream(array);
            PdfReader reader =new PdfReader(inputStream);
            PdfDocument pdfDoc = new PdfDocument(
                    reader,
                    new PdfWriter(outputStream, new WriterProperties().setStandardEncryption(
                            // null user password argument is equal to empty string,
                            // this means that no user password required
                            null,
                            OWNER_PASS.getBytes(),
                            EncryptionConstants.ALLOW_PRINTING,
                            EncryptionConstants.ENCRYPTION_AES_128 | EncryptionConstants.DO_NOT_ENCRYPT_METADATA))
            );
            pdfDoc.getWriter().write(array);
            pdfDoc.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
