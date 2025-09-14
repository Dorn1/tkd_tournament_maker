package pl.tkd.tournaments.tkd_tournament_maker.club.competitor;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.tkd.tournaments.tkd_tournament_maker.club.club.Club;
import pl.tkd.tournaments.tkd_tournament_maker.club.user.Role;
import pl.tkd.tournaments.tkd_tournament_maker.club.user.User;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament.Tournament;

import java.time.LocalDate;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
public class Competitor extends User {
    @Builder
    public Competitor(String email, String password, Role role, LocalDate createdAt, LocalDate updatedAt, Club club, Double weight, Integer belt, Sex sex, String firstName, String lastName, Long birthYear) {
        super(email, password, role, createdAt, updatedAt);
        this.club = club;
        Weight = weight;
        this.belt = belt;
        this.sex = sex;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
    }

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
