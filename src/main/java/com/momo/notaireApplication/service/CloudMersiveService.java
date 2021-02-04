package com.momo.notaireApplication.service;

import com.cloudmersive.client.ConvertDocumentApi;
import com.cloudmersive.client.EditDocumentApi;
import com.cloudmersive.client.invoker.ApiClient;
import com.cloudmersive.client.invoker.ApiException;
import com.cloudmersive.client.invoker.Configuration;
import com.cloudmersive.client.invoker.auth.ApiKeyAuth;
import com.cloudmersive.client.model.ReplaceStringRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class CloudMersiveService {
    //todo tester manuellement pas de test unitaire pck on a un nb de call
    @Autowired
    private static final Logger LOGGER = LoggerFactory.getLogger(CloudMersiveService.class);

    @Value("${pdf.cloudMersive.apiKey}")
    private String cloudMersiveapiKey;

    public byte[] convertDocxToPDF(byte[] byteArray)throws IOException {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        ApiKeyAuth Apikey = (ApiKeyAuth) defaultClient.getAuthentication("Apikey");
        Apikey.setApiKey(cloudMersiveapiKey);

        ConvertDocumentApi apiInstance = new ConvertDocumentApi();
        File tempFile = initTempFile(byteArray);
        try {
            return apiInstance.convertDocumentAutodetectToPdf(tempFile);
        } catch (ApiException e) {
            LOGGER.info("Exception when calling ConvertDocumentApi#convertDocumentAutodetectToPdf");
            e.printStackTrace();
        }
        return null;
    }

    private File initTempFile(byte[] byteArray) throws IOException {
        File tempFile = File.createTempFile("", "", null);
        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.write(byteArray);
        return tempFile;
    }
}
