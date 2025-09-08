package pl.tkd.tournaments.tkd_tournament_maker.club.competitor;

import jakarta.persistence.*;
import lombok.*;
import pl.tkd.tournaments.tkd_tournament_maker.club.club.Club;
import pl.tkd.tournaments.tkd_tournament_maker.club.user.User;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament.Tournament;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
public class Competitor extends User {

    @ManyToOne
    private Club club;
    @ManyToMany
    Set<Tournament> tournaments;
    private Double Weight;
    private Integer belt;
    private Sex sex;
    private String firstName;
    private String lastName;
    private Long birthYear;
}
