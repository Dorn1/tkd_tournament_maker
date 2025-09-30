package pl.tkd.tournaments.tkd_tournament_maker.club.referee;

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
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Referee extends User {
    @Builder
    public Referee(String userName, String password, Role role, LocalDate createdAt, LocalDate updatedAt, Club club, String firstName, String lastName, RefereeClass refereeClass){
        super(userName, password, role, createdAt, updatedAt);
        this.refereeClass = refereeClass;
        this.club = club;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @ToString.Exclude
    @ManyToOne
    private Club club;

    @ToString.Exclude
    @ManyToMany
    Set<Tournament> tournaments;

    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefereeClass refereeClass;


}
