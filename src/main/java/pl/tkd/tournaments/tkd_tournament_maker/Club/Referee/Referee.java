package pl.tkd.tournaments.tkd_tournament_maker.Club.Referee;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Club.Club;

@Getter
@Setter
@Entity
public class Referee {
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

}
