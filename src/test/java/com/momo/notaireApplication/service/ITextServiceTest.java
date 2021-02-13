package com.momo.notaireApplication.service;

import com.momo.notaireApplication.service.encryption.EncryptionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ITextServiceTest {
    @InjectMocks
    private ITextService iTextService;

    @BeforeEach
    public void init() throws CertificateException, FileNotFoundException, NoSuchProviderException, InvalidKeySpecException, NoSuchAlgorithmException {
        ReflectionTestUtils.setField(iTextService, "privateKeyStringValue", "-----BEGIN RSA PRIVATE KEY-----MIIEpQIBAAKCAQEAv0puO1pqOTqJE3btnWAdfGqaeQIpvquB0WxUMRvbjg+xHqUOWITaSa8tup1dFyrTUF44tNzj8WrmW1KFfyQI5LZBDft/kZNSjfLDsdMitRzzKI2Puyiy1hYZ83lryAgj/yWZ44VKBjQNyPUIJapJBp7N4mp/ms6wTyZgNtl/xebx1taWdtKD7w3vpPTPfEvx9JS0Fsn7Nlp9fMlNxh/Zy+Fcs0p1ULlYHnJDRQkRc7JRkKjoCRpYrp4p1FsnFpLojD+8Ujq76dFi/skCI1gEGpMavD0w3S8WyjGYWHpGlX/UBt0RDt50ljetaVZs2DyUAE4+/o1F7X1opvoB+lqoWwIDAQABAoIBAQCKwr87ptavNNsCl56+Ll61Rc9QQFVvzpGZDO/1v7OAVbmx9SF27Mg78ytE16oTiClJf0+7QxDGqzj2veHA3K7m2Z5POkTLoQadmaCDehkPMDTjEe1VYqx3yAiDKW0Z6W3TQ79h3+pWnaPyRmzmhaP7fW7z8zUlYxFaGQhqQVQChcXvq/pvK+ry9ZBpfv5ssC9zMpNkJ/F4G6HXA3K1Noew4Y1F7LKb5GBiIUTq/ayaqjhNJo0X6cYUE3WqqGLPe+OSNNi/boQsM5u19qXauEv0Eq0vo05dA7+yjrrtDs2BzTL3yanZSG6tUtvDvTlzdRT/24vSlhhDwyBP3L3L0I3hAoGBAPTOOzjJjmcVpd9V/rdrga6LwkXdNvu20hlAo0WXiSwxGiMf9pZfZrT7Y+isoVO9o5g5jns/rH3GQSl2S9HWAsRUkWCqTVv+2jbH/O0mENEetC8oGuLbvTrPq9e4aGqm8lC33UOMaVkREhSRtauL9gjUaYXcTnf/SNw5VKT360SPAoGBAMgJvPpnmAUpW/u4tAKtpmugpGLb1jcBf2um5IUx9rKP2dLa78tWm7LY804Z0txGNTiZu1ugvyKrDYgqB8PAeR7dRsdq+URKCw3rsmhXQnexlmkfFZXh0CKNTYaTp09GI/FDg+Aws6i/MDBRQfPDDb2n1IxtYyBq8f2EePB9Pf11AoGAKxpAnSAH3aiXGvqWLY9VNvlOjGSFsM4gseeKl881jL1cf8C59wH3IHh+v9HdWdQzALNpndO5OC/2+yjj8p8+v3iwXBfAJSl7HFgb9VlVbqx8Ry1Fhpnv+HHmLMZARD7+vS2SXLlVNyRlTY/WL80l1aoopjQC8OfW2UeS2rT4DIsCgYEAtlNZ8gp2H05BTsBdIovQQg0biBfDukMdNqvZYiXWK46+BVgUdIuuQmdH7InRDYDYhYxpnux3CkxKey+EAEHwo9cf6idwp3Ote7S22i+en6Y35/yzGffGU5fjb6QpPTI7FG5rRu5c8pPAgmmt6VaBsbnFG3Gghi2z/1S9PsWIuPkCgYEA6bnUh2eD3acO1NXHB9xrQtj5wdf8BXRh+SnYYwEE3rwpKeBBVH2WCo8kNzQy5ZolZasUW6TmNIm1M+ho3mNI3/kA5Gk9Vbke6DbGX9p4y/BUbwzn/mKUViMIzP2RY1kZ1Vpal8CxrV/JpmAFG6uH8jseLCLsp16eT92kZ/wwrp4=-----END RSA PRIVATE KEY-----");
       // when(encryptionService.initCertificate()).thenCallRealMethod();
       // when(encryptionService.initPrivateKey()).thenCallRealMethod();

    }

    @Test
    public void testSignature() throws GeneralSecurityException, IOException {
        iTextService.main();
    }

}