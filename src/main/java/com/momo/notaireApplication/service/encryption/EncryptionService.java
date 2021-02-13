package com.momo.notaireApplication.service.encryption;

import com.momo.notaireApplication.utils.EncryptionUtils;
import lombok.SneakyThrows;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientInfoGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OutputEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Collection;

@Service
public class EncryptionService {

    @Value("${encryption.privateKey}")
    private String privateKeyStringValue;
    private PrivateKey privateKey;
    private X509Certificate encryptionCertificate;

    public EncryptionService() {
        EncryptionUtils.registerBouncyCastleProvider();
    }

    public byte[] encryptData(byte[] data)
            throws CertificateException, CMSException, IOException, NoSuchProviderException {
        encryptionCertificate = EncryptionUtils.initCertificate();
        byte[] encryptedData = null;
        if (null != data && null != encryptionCertificate) {
            CMSEnvelopedDataGenerator cmsEnvelopedDataGenerator
                    = new CMSEnvelopedDataGenerator();

            JceKeyTransRecipientInfoGenerator jceKey
                    = new JceKeyTransRecipientInfoGenerator(encryptionCertificate);
            cmsEnvelopedDataGenerator.addRecipientInfoGenerator(jceKey);
            CMSTypedData msg = new CMSProcessableByteArray(data);
            OutputEncryptor encryptor
                    = new JceCMSContentEncryptorBuilder(CMSAlgorithm.AES128_CBC)
                    .setProvider("BC").build();
            CMSEnvelopedData cmsEnvelopedData = cmsEnvelopedDataGenerator
                    .generate(msg, encryptor);
            encryptedData = cmsEnvelopedData.getEncoded();
        }
        return encryptedData;
    }

    public byte[] decryptData(
            byte[] encryptedData)
            throws CMSException, InvalidKeySpecException, NoSuchAlgorithmException {

        privateKey = EncryptionUtils.initPrivateKey(privateKeyStringValue);
        byte[] decryptedData = null;
        if (null != encryptedData && null != privateKey) {
            CMSEnvelopedData envelopedData = new CMSEnvelopedData(encryptedData);

            Collection<RecipientInformation> recipients
                    = envelopedData.getRecipientInfos().getRecipients();
            KeyTransRecipientInformation recipientInfo
                    = (KeyTransRecipientInformation) recipients.iterator().next();
            JceKeyTransRecipient recipient
                    = new JceKeyTransEnvelopedRecipient(privateKey);

            return recipientInfo.getContent(recipient);
        }
        return decryptedData;
    }




}
