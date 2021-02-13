package com.momo.notaireApplication.service;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.DigestAlgorithms;
import com.itextpdf.signatures.IExternalDigest;
import com.itextpdf.signatures.IExternalSignature;
import com.itextpdf.signatures.PdfSignatureAppearance;
import com.itextpdf.signatures.PdfSigner;
import com.itextpdf.signatures.PrivateKeySignature;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.Arrays;

import com.momo.notaireApplication.service.encryption.EncryptionService;
import com.momo.notaireApplication.utils.EncryptionUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ITextService {
    public static final String DEST = "./target/signatures/chapter02/";

    public static final String KEYSTORE = "./src/test/resources/encryption/ks";
    public static final String SRC = "./src/test/resources/testDocuments.pdf";

    public static final char[] PASSWORD = "password".toCharArray();
    @Value("${encryption.privateKey}")
    private String privateKeyStringValue;

    public ITextService() {
        EncryptionUtils.registerBouncyCastleProvider();
    }

    public static final String[] RESULT_FILES = new String[] {
            "hello_signed1.pdf",
            "hello_signed2.pdf",
            "hello_signed3.pdf",
            "hello_signed4.pdf"
    };
    public void sign(String src, String dest, Certificate[] chain, PrivateKey pk, String digestAlgorithm,
                     String provider, PdfSigner.CryptoStandard signatureType, String reason, String location)
            throws GeneralSecurityException, IOException {
        PdfReader reader = new PdfReader(src);
        PdfSigner signer = new PdfSigner(reader, new FileOutputStream(dest), new StampingProperties());

        // Create the signature appearance
        Rectangle rect = new Rectangle(36, 648, 200, 100);
        PdfSignatureAppearance appearance = signer.getSignatureAppearance();
        appearance
                .setReason(reason)
                .setLocation(location)

                // Specify if the appearance before field is signed will be used
                // as a background for the signed field. The "false" value is the default value.
                .setReuseAppearance(false)
                .setPageRect(rect)
                .setPageNumber(1);
        signer.setFieldName("sig");

        IExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
        IExternalDigest digest = new BouncyCastleDigest();

        // Sign the document using the detached mode, CMS or CAdES equivalent.
        signer.signDetached(digest, pks, chain, null, null, null, 0, signatureType);
    }
    public void main() throws GeneralSecurityException, IOException {
        File file = new File(DEST);
        file.mkdirs();

        BouncyCastleProvider provider = new BouncyCastleProvider();
        PrivateKey pk = EncryptionUtils.initPrivateKey(privateKeyStringValue);
        Certificate[] chain ={EncryptionUtils.initCertificate()};


        this.sign(SRC, DEST + RESULT_FILES[0], chain, pk, DigestAlgorithms.SHA256, provider.getName(),
                PdfSigner.CryptoStandard.CMS, "Test 1", "Ghent");
    }

}
