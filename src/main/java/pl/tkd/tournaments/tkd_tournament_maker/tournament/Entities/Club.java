package pl.tkd.tournaments.tkd_tournament_maker.tournament.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Club {
    @Id
    private Long id;

}
