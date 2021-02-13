package com.momo.notaireApplication.service.pdf;

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
import java.security.PrivateKey;
import java.security.cert.Certificate;

import com.momo.notaireApplication.model.db.FichierDocument;
import com.momo.notaireApplication.utils.ApplicationFileUtils;
import com.momo.notaireApplication.utils.EncryptionUtils;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ITextService {
    public static final String DEST = "./target/signatures/chapter02/";

    @Value("${encryption.privateKey}")
    private String privateKeyStringValue;

    public ITextService() {
        EncryptionUtils.registerBouncyCastleProvider();
    }

    public static final String RESULT_FILES = "hello_signed1.pdf";

    private void sign(byte[] byteArray, Certificate[] chain, PrivateKey pk, String digestAlgorithm,
                      String provider, PdfSigner.CryptoStandard signatureType, String reason, String location)
            throws GeneralSecurityException, IOException {

        PdfReader reader = new PdfReader(new FileInputStream(ApplicationFileUtils.initTempFile(byteArray)));
        PdfSigner signer = new PdfSigner(reader, new FileOutputStream(DEST+RESULT_FILES), new StampingProperties());

        // Create the signature appearance
        Rectangle rect = new Rectangle(36, 0, 200, 100);
        PdfSignatureAppearance appearance = signer.getSignatureAppearance();
        appearance
                .setContact("james")
                .setSignatureCreator("melo")
                .setContact("ball")
                .setReason(reason)
                .setLocation(location)
                .setReuseAppearance(false)
                .setPageRect(rect)
                .setPageNumber(1);

        signer.setFieldName("sig");

        IExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
        IExternalDigest digest = new BouncyCastleDigest();

        // Sign the document using the detached mode, CMS or CAdES equivalent.
        signer.signDetached(digest, pks, chain, null, null, null, 0, signatureType);
    }

    public byte[] sign(byte[] byteArray, String description,String location) throws GeneralSecurityException, IOException {
        createDirectory();

        BouncyCastleProvider provider = new BouncyCastleProvider();
        PrivateKey pk = EncryptionUtils.initPrivateKey(privateKeyStringValue);
        Certificate[] chain = {EncryptionUtils.initCertificate()};

        this.sign(  byteArray, chain, pk, DigestAlgorithms.SHA256, provider.getName(),
                PdfSigner.CryptoStandard.CMS, description, location);
        return FileUtils.readFileToByteArray(new File(DEST+RESULT_FILES));
    }

    private void createDirectory() {
        File file = new File(DEST);
        file.mkdirs();
    }

}
