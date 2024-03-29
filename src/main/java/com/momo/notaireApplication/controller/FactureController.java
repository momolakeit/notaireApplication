package com.momo.notaireApplication.controller;

import com.momo.notaireApplication.model.db.Facture;
import com.momo.notaireApplication.model.dto.FactureDTO;
import com.momo.notaireApplication.model.request.CreateFactureRequestDTO;
import com.momo.notaireApplication.service.FactureService;
import com.momo.notaireApplication.service.payment.StripeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/facture")
public class FactureController extends BaseController {

    private FactureService factureService;  
    private StripeService stripeService;

    public FactureController(FactureService factureService, StripeService stripeService) {
        this.factureService = factureService;
        this.stripeService = stripeService;
    }

    @PostMapping
    public FactureDTO createFacture(@RequestBody CreateFactureRequestDTO createFactureRequestDTO) {
        Facture facture = this.factureService.createFacture(
                createFactureRequestDTO.getNotaireId(),
                createFactureRequestDTO.getClientId(),
                createFactureRequestDTO.getPrix());
        String paymentClientSecret = this.stripeService.processPayment(facture);
        FactureDTO factureDTO = this.factureService.getFactureDTO(facture.getId());
        factureDTO.setPaymentClientSecret(paymentClientSecret);
        return factureDTO;
    }
}
