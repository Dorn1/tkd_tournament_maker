package pl.tkd.tournaments.tkd_tournament_maker.club.club;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.Referee;
import pl.tkd.tournaments.tkd_tournament_maker.club.user.User;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament.Tournament;

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

    private boolean admin;

}
