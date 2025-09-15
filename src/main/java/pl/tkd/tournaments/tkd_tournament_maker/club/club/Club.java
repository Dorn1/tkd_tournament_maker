package pl.tkd.tournaments.tkd_tournament_maker.club.club;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.Referee;
import pl.tkd.tournaments.tkd_tournament_maker.club.user.Role;
import pl.tkd.tournaments.tkd_tournament_maker.club.user.User;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament.Tournament;

import java.time.LocalDate;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Club extends User {
    @Builder
    public Club(
            String email,
            String password,
            Role role,
            LocalDate createdAt,
            LocalDate updatedAt,
            boolean admin,
            String name) {
        super(email, password, role, createdAt, updatedAt);
        this.admin = admin;
        this.name = name;
    }

    private String name;
    @ToString.Exclude
    @ManyToMany
    Set<Tournament> tournaments;
    @ToString.Exclude
    @OneToMany
    Set<Competitor> Competitors;
    @ToString.Exclude
    @OneToMany
    Set<Referee> Referees;
    @ToString.Exclude
    private boolean admin;

}
