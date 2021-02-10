package com.momo.notaireApplication.service.payment;

import com.momo.notaireApplication.model.db.Facture;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.service.NotaireService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.ApplePayDomain;
import com.stripe.model.PaymentIntent;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.ApplePayDomainCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class StripeService {
    @Value("${stripe.apiKey}")
    private String stripeAPIKey;

    @Value("${front-end.url}")
    private String frontEndUrl;

    private final String cardMethodType = "card";

    private final String currency = "cad";

    private NotaireService notaireService;

    public StripeService(NotaireService notaireService) {
        this.notaireService = notaireService;
    }

    //todo retourner les valueures de stripe par DTOS
    public String processPayment(Facture facture, Notaire notaire) throws StripeException {
        Stripe.apiKey = stripeAPIKey;
        PaymentIntentCreateParams params =
                buildPaymentParams(facture, notaire);

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        return paymentIntent.getClientSecret();

    }

    public String createStripeAccount(String notaireEmail) throws StripeException {
        Stripe.apiKey = stripeAPIKey;

        Notaire notaire = notaireService.findNotaireByEmail(notaireEmail);
        AccountCreateParams params = initAccountCreateParams(notaire);
        Account account = fetchStripeAccount(notaire, params);
        return createAccountLink(account);
    }

    private String createAccountLink(Account account) throws StripeException {
        AccountLinkCreateParams accountLinkparams =
                AccountLinkCreateParams.builder()
                        .setAccount(account.getId())
                        .setRefreshUrl(frontEndUrl)
                        .setReturnUrl(frontEndUrl)
                        .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                        .build();
        AccountLink accountLink = AccountLink.create(accountLinkparams);
        return accountLink.getUrl();
    }

    private AccountCreateParams initAccountCreateParams(Notaire notaire) {
        AccountCreateParams params =
                AccountCreateParams.builder()
                        .setType(AccountCreateParams.Type.EXPRESS)
                        .setEmail(notaire.getEmail())
                        .setBusinessType(AccountCreateParams.BusinessType.COMPANY)
                        .setCapabilities(
                                AccountCreateParams.Capabilities.builder()
                                        .setCardPayments(AccountCreateParams.Capabilities.CardPayments.builder().setRequested(true).build())
                                        .setTransfers(AccountCreateParams.Capabilities.Transfers.builder().setRequested(true).build())
                                        .build())
                        .build();
        return params;
    }

    private Account fetchStripeAccount(Notaire notaire, AccountCreateParams params) throws StripeException {
        Account account = null;
        if (Objects.isNull(notaire.getStripeAccountId())) {
            account = Account.create(params);
            notaire.setStripeAccountId(account.getId());
            notaireService.saveNotaire(notaire);
        } else {
            account = Account.retrieve(notaire.getStripeAccountId());
        }
        return account;
    }

    private PaymentIntentCreateParams buildPaymentParams(Facture facture, Notaire notaire) {
        PaymentIntentCreateParams.Builder builder =
                PaymentIntentCreateParams.builder()
                        .setAmount(Math.round(facture.getPrix().doubleValue() * 100))
                        .setCurrency(currency)
                        .setTransferData(
                                PaymentIntentCreateParams
                                        .TransferData.builder()
                                        .setDestination(notaire.getStripeAccountId())
                                        .build()
                        )
                        .addPaymentMethodType(cardMethodType);
        return builder.build();
    }
}