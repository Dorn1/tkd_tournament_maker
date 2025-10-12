package pl.tkd.tournaments.tkd_tournament_maker.club.referee;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
public class RefereeDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private RefereeClass refereeClass;
    private String userName;
    private  Long clubId;
    private Set<Long> tournamentIds;
}
