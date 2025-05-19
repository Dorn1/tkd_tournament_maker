package pl.tkd.tournaments.tkd_tournament_maker.Club.Club;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Referee.Referee;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Tournament.Tournament;

import java.io.Serializable;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany
    Set<Tournament> tournaments;
    @OneToMany
    Set<Competitor> Competitors;
    @OneToMany
    Set<Referee> Referees;

}
