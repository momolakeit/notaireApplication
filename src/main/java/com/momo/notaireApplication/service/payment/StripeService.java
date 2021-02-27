package com.momo.notaireApplication.service.payment;

import com.momo.notaireApplication.exception.validation.BadStripeRoleException;
import com.momo.notaireApplication.model.db.Facture;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.service.UserService;
import com.momo.notaireApplication.utils.ObjectUtils;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.PaymentIntent;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class StripeService {
    @Value("${stripe.apiKey}")
    private String stripeAPIKey;

    @Value("${front-end.url}")
    private String frontEndUrl;

    private final String cardMethodType = "card";

    private final String currency = "cad";

    private final String stripePaymentErreurLogMsg = "Erreur lors de l'appel de stripe pour process un payment";

    private final String stripeAccountErreurLogMsg = "Erreur lors de l'appel de stripe pour creer un compte";

    private UserService userService;


    private static final Logger LOGGER = LoggerFactory.getLogger(StripeService.class);

    public StripeService(UserService userService) {
        this.userService = userService;
    }

    public String processPayment(Facture facture) {
        Stripe.apiKey = stripeAPIKey;
        Notaire notaire = ObjectUtils.findNotaireInList(facture.getUsers());
        PaymentIntentCreateParams params =
                buildPaymentParams(facture, notaire);
        PaymentIntent paymentIntent = null;
        try {
            paymentIntent = PaymentIntent.create(params);

        } catch (StripeException e) {
            LOGGER.info(stripePaymentErreurLogMsg, e);
        }
        return paymentIntent.getClientSecret();

    }

    public String createStripeAccount(String notaireEmail) {
        Stripe.apiKey = stripeAPIKey;

        Notaire notaire;
        try {
            notaire = (Notaire) userService.foundUserByEmail(notaireEmail);
        } catch (ClassCastException e) {
            throw new BadStripeRoleException();
        }
        AccountCreateParams params = initAccountCreateParams(notaire);
        try {
            Account account = fetchStripeAccount(notaire, params);
            return createAccountLink(account);

        } catch (StripeException e) {
            LOGGER.info(stripeAccountErreurLogMsg, e);
        }
        return null;
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
                        .setEmail(notaire.getEmailAdress())
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
            userService.saveUser(notaire);
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
