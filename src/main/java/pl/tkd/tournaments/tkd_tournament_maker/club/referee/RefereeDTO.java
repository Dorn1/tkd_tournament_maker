package pl.tkd.tournaments.tkd_tournament_maker.club.referee;

import lombok.Data;
import pl.tkd.tournaments.tkd_tournament_maker.club.user.Role;

import java.util.Set;

@Data
public class RefereeDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private RefereeClass refereeClass;
    private Set<Long> tournamentIds;
}
