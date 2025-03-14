package pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Club.Club;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Tournament.Tournament;

import java.util.Set;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class Competitor {
    @Id
    private Long id;
    @ManyToOne
    private Club club;
    @ManyToMany
    Set<Tournament> tournaments;
    private Long age;
    private Double Weight;
    private Belt belt;
    private Sex sex;
    private String firstName;
    private String lastName;
}
