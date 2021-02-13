package com.momo.notaireApplication.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class EncryptionUtils {
    private static String BEGIN_PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----";
    private static String END_PRIVATE_KEY = "-----END RSA PRIVATE KEY-----";
    private static final String FILE_PATH = "src\\main\\resources\\certificate\\certificate.cer";

    public static X509Certificate initCertificate() throws CertificateException, NoSuchProviderException, FileNotFoundException {
        CertificateFactory certFactory = CertificateFactory
                .getInstance("X.509", "BC");

        return (X509Certificate) certFactory
                .generateCertificate(new FileInputStream(FILE_PATH));
    }
    public static void registerBouncyCastleProvider(){
        Security.addProvider(new BouncyCastleProvider());
    }

    public static PrivateKey initPrivateKey(String privateKeyStringValue) throws NoSuchAlgorithmException, InvalidKeySpecException {
        privateKeyStringValue = privateKeyStringValue.replace(BEGIN_PRIVATE_KEY, "");
        privateKeyStringValue = privateKeyStringValue.replace(END_PRIVATE_KEY, "");
        privateKeyStringValue = privateKeyStringValue.replace("\n", "").replace("\r", "");

        byte[] decodedByteArray = Base64.getDecoder().decode(privateKeyStringValue);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedByteArray);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }
}
