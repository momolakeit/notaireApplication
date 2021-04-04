package com.momo.notaireApplication.model.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RendezVous {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateDebut;

    private LocalDateTime dateFin;

    @OneToOne
    private Conversation conversation;

    @ManyToMany
    private List<User> users;

}
