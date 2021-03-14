package com.momo.notaireApplication.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.momo.notaireApplication.model.db.User;
import com.momo.notaireApplication.testUtils.ObjectTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtProviderTest {

    private JwtProvider jwtProvider;

    private final String SECRET = "supersecret";

    private final String ROLE_CLAIM = "role";

    @BeforeEach
    public void setEnvValues() {
        jwtProvider = new JwtProvider(50, SECRET);
    }

    @Test
    public void testGenerateToken() {
        User user = ObjectTestUtils.initNotaire();
        user.setId(55L);
        String generatedToken = jwtProvider.generate(user);
        DecodedJWT decodedJWT = jwtProvider.verify(generatedToken);
        assertEquals(user.getId().toString(), decodedJWT.getSubject());
        assertEquals("NOTAIRE",decodedJWT.getClaim(ROLE_CLAIM).asString());

    }

    @Test
    public void testGetRoleFromToken() {
        User user = ObjectTestUtils.initNotaire();
        user.setId(55L);
        String generatedToken = jwtProvider.generate(user);
        String role = jwtProvider.getUserRoleFromToken(generatedToken);
        assertEquals("NOTAIRE",role);

    }



}