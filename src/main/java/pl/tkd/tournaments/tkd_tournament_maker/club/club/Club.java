package pl.tkd.tournaments.tkd_tournament_maker.club.club;

import jakarta.persistence.*;
import lombok.*;
import pl.tkd.tournaments.tkd_tournament_maker.club.user.Role;
import pl.tkd.tournaments.tkd_tournament_maker.club.user.User;
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
            String userName,
            String password,
            Role role,
            LocalDate createdAt,
            LocalDate updatedAt,
            boolean admin) {
        super(userName, password, role, createdAt, updatedAt);
        this.admin = admin;
    }

    @ToString.Exclude
    private boolean admin;

}
