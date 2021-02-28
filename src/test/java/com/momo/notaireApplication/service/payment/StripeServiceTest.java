package com.momo.notaireApplication.service.payment;

import com.momo.notaireApplication.exception.validation.BadStripeRoleException;
import com.momo.notaireApplication.model.db.Facture;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.service.UserService;
import com.momo.notaireApplication.testUtils.ObjectTestUtils;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.PaymentIntent;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StripeServiceTest {

    @InjectMocks
    private StripeService stripeService;

    @Mock
    private UserService userService;

    @Captor
    private ArgumentCaptor<PaymentIntentCreateParams> paymentIntentCreateParamsArgumentCaptor;

    @Captor
    private ArgumentCaptor<AccountCreateParams> accountCreateParamsArgumentCaptor;


    private final String CLIENT_SECRET = "superSecret";

    private final String ACCOUNT_ID = "123456";

    @BeforeEach
    public void initValues() {
        ReflectionTestUtils.setField(stripeService, "stripeAPIKey", "superSecureKey");
        ReflectionTestUtils.setField(stripeService, "frontEndUrl", "http://www.bestWebsite.com");
    }
    

    @Test
    public void testProcessPayment() throws StripeException {
        Notaire notaire = ObjectTestUtils.initNotaire();
        Facture facture = new Facture();
        facture.setUsers(new ArrayList<>(Arrays.asList(notaire)));
        facture.setPrix(BigDecimal.valueOf(50));
        PaymentIntent paymentIntent = new PaymentIntent();
        paymentIntent.setClientSecret(CLIENT_SECRET);

        MockedStatic<PaymentIntent> paymentIntentMockedStatic = mockStatic(PaymentIntent.class);
        paymentIntentMockedStatic.when(() -> PaymentIntent.create(any(PaymentIntentCreateParams.class))).thenReturn(paymentIntent);

        String clientSecretReturnValue = stripeService.processPayment(facture);
        paymentIntentMockedStatic.verify(() -> PaymentIntent.create(paymentIntentCreateParamsArgumentCaptor.capture()));
        PaymentIntentCreateParams paymentIntentCreateParams = paymentIntentCreateParamsArgumentCaptor.getValue();

        assertEquals(Math.round(facture.getPrix().doubleValue() * 100), paymentIntentCreateParams.getAmount());
        assertEquals("cad", paymentIntentCreateParams.getCurrency());
        assertEquals(notaire.getStripeAccountId(), paymentIntentCreateParams.getTransferData().getDestination());
        assertEquals(CLIENT_SECRET, clientSecretReturnValue);

        paymentIntentMockedStatic.closeOnDemand();

    }

    @Test
    public void testCreateStripeAccountStripeAccountDejaPresent() throws StripeException {
        Notaire notaire = ObjectTestUtils.initNotaire();
        AccountLink accountLink = new AccountLink();
        accountLink.setUrl("http://www.bestWebsite.com");
        Account account = new Account();
        account.setId(ACCOUNT_ID);

        MockedStatic<Account> accountMockedStatic = mockStatic(Account.class);
        MockedStatic<AccountLink> accountLinkMockedStatic = mockStatic(AccountLink.class);

        when(userService.foundUserByEmail(anyString())).thenReturn(notaire);
        accountLinkMockedStatic.when(() -> AccountLink.create(any(AccountLinkCreateParams.class))).thenReturn(accountLink);
        accountMockedStatic.when(() -> Account.retrieve(anyString())).thenReturn(account);

        assertEquals("http://www.bestWebsite.com", stripeService.createStripeAccount("notaire@mail.com"));

        accountMockedStatic.closeOnDemand();
        accountLinkMockedStatic.closeOnDemand();

    }

    @Test
    public void testCreateStripeAccountStripeAccountPasPresent() throws StripeException {
        Notaire notaire = ObjectTestUtils.initNotaire();
        notaire.setStripeAccountId(null);
        AccountLink accountLink = new AccountLink();
        accountLink.setUrl("http://www.bestWebsite.com");
        Account account = new Account();
        account.setId(ACCOUNT_ID);

        MockedStatic<Account> accountMockedStatic = mockStatic(Account.class);
        MockedStatic<AccountLink> accountLinkMockedStatic = mockStatic(AccountLink.class);

        when(userService.foundUserByEmail(anyString())).thenReturn(notaire);
        accountLinkMockedStatic.when(() -> AccountLink.create(any(AccountLinkCreateParams.class))).thenReturn(accountLink);
        accountMockedStatic.when(() -> Account.create(any(AccountCreateParams.class))).thenReturn(account);

        String emailReturnValue = stripeService.createStripeAccount("notaire@mail.com");

        accountMockedStatic.verify(() -> Account.create(accountCreateParamsArgumentCaptor.capture()));
        AccountCreateParams accountCreateParams = accountCreateParamsArgumentCaptor.getValue();

        assertEquals(notaire.getEmailAdress(),accountCreateParams.getEmail());
        assertEquals(AccountCreateParams.Type.EXPRESS,accountCreateParams.getType());
        assertEquals(AccountCreateParams.BusinessType.COMPANY,accountCreateParams.getBusinessType());
        assertEquals("http://www.bestWebsite.com", emailReturnValue);

        accountMockedStatic.closeOnDemand();
        accountLinkMockedStatic.closeOnDemand();

    }

    @Test
    public void testCreateStripeAccountPourClientLanceException() throws StripeException {


        when(userService.foundUserByEmail(anyString())).thenReturn(ObjectTestUtils.initClient());

        Assertions.assertThrows(BadStripeRoleException.class, () -> {
            stripeService.createStripeAccount("notaire@mail.com");
        });

    }
}