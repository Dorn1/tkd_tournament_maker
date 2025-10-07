package pl.tkd.tournaments.tkd_tournament_maker.club.competitor;

import lombok.Data;

import java.util.Set;

@Data
public class CompetitorTableDTO {
    Long id;
    String firstname;
    String lastname;
    Integer belt;
    private String userName;
    private Set<Long> tournamentIds;
}
