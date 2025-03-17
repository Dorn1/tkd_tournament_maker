package pl.tkd.tournaments.tkd_tournament_maker.Club.Club;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Referee.Referee;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Tournament.Tournament;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Club {
    public Club(String name) {
        this.name = name;
    }

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
