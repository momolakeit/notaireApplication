package com.momo.notaireApplication.model.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch= FetchType.EAGER)
    private List<User> users;

    @OneToMany(cascade = {CascadeType.REMOVE})
    private List<Messages> messages;

    @OneToOne
    private RendezVous rendezVous;

}
