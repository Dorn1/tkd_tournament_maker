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
public class Competitor {
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

}
