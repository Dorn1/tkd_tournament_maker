package pl.tkd.tournaments.tkd_tournament_maker.Club.Referee;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Club.Club;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Tournament.Tournament;

import java.util.Set;

@Getter
@Setter
@Entity
public class Referee {
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;
    @ManyToMany
    Set<Tournament> tournaments;

}
