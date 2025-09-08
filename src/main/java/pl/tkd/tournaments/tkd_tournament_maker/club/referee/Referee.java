package pl.tkd.tournaments.tkd_tournament_maker.club.referee;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.tkd.tournaments.tkd_tournament_maker.club.club.Club;
import pl.tkd.tournaments.tkd_tournament_maker.club.user.User;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament.Tournament;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Referee extends User {

    @ManyToOne
    private Club club;
    private String firstName;
    private String lastName;
    @ManyToMany
    Set<Tournament> tournaments;

}
