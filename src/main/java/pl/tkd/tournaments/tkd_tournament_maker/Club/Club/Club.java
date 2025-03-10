package pl.tkd.tournaments.tkd_tournament_maker.Club.Club;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Referee.Referee;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Tournament.Tournament;

import java.util.Set;

@Entity
@Getter
@Setter
public class Club {
    @Id
    private Long id;
    @ManyToMany
    Set<Tournament> tournaments;
    @OneToMany
    Set<Competitor> Competitors;
    @OneToMany
    Set<Referee> Referees;

}
