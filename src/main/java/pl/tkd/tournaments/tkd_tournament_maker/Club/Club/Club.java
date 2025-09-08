package pl.tkd.tournaments.tkd_tournament_maker.Club.Club;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Referee.Referee;
import pl.tkd.tournaments.tkd_tournament_maker.Club.User;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Tournament.Tournament;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
public class Club extends User {

    private String name;
    @ManyToMany
    Set<Tournament> tournaments;
    @OneToMany
    Set<Competitor> Competitors;
    @OneToMany
    Set<Referee> Referees;

}
