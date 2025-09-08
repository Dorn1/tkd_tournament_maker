package pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor;

import jakarta.persistence.*;
import lombok.*;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Club.Club;
import pl.tkd.tournaments.tkd_tournament_maker.Club.User;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Tournament.Tournament;

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
