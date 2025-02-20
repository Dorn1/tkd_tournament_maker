package pl.tkd.tournaments.tkd_tournament_maker.tournament.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Mat {
    @Id
    private Long id;
    private Long onTournamentNumber;
    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;
}
