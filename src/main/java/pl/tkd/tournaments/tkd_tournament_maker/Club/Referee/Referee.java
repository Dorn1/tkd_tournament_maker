package pl.tkd.tournaments.tkd_tournament_maker.Club.Referee;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Club.Club;
import pl.tkd.tournaments.tkd_tournament_maker.Club.User;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Tournament.Tournament;

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
