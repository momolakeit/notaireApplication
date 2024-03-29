package com.momo.notaireApplication.controller;

import com.momo.notaireApplication.model.db.RendezVous;
import com.momo.notaireApplication.model.dto.RendezVousDTO;
import com.momo.notaireApplication.model.request.CreateRendezVousRequestDTO;
import com.momo.notaireApplication.service.RendezVousService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rendezVous")
public class RendezVousController extends BaseController {

    private RendezVousService rendezVousService;

    public RendezVousController(RendezVousService rendezVousService) {
        this.rendezVousService = rendezVousService;
    }

    @PostMapping
    public RendezVousDTO createRendezVous(@RequestBody CreateRendezVousRequestDTO createRendezVousRequestDTO) {
        RendezVous rendezVous = rendezVousService.createRendezVous(
                createRendezVousRequestDTO.getClientId(),
                createRendezVousRequestDTO.getNotaireId(),
                createRendezVousRequestDTO.getDate(),
                createRendezVousRequestDTO.getDureeEnMinute()
        );
        return rendezVousService.toDTO(rendezVous);
    }

    @GetMapping("/getRendezVous/{id}")
    public RendezVousDTO findRendezVous(@PathVariable final long id) {
        RendezVous rendezVous = rendezVousService.getRendezVous(id);
        return rendezVousService.toDTO(rendezVous);
    }

    @GetMapping("/getAllRendezVousForUser/{userId}")
    public List<RendezVousDTO> findAllRendezVous(@PathVariable final long userId) {
        return rendezVousService.fetchAllRendezVousForUser(userId)
                .stream()
                .map(rendezVous -> rendezVousService.toDTO(rendezVous))
                .collect(Collectors.toList());
    }
}
