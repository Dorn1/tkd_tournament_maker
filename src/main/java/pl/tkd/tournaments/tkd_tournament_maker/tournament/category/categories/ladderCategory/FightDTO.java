package pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.ladderCategory;

import lombok.Data;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.CompetitorTableDTO;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.RefereeDTO;

import java.util.List;

@Data
public class FightDTO {
    private Long id;
    private CompetitorTableDTO competitor1;
    private CompetitorTableDTO competitor2;
    private CompetitorTableDTO winner;

    private List<FightDTO> fightsBefore;

    private RefereeDTO mainFightReferee;
    private List<RefereeDTO> fightReferees;
    private List<RefereeDTO> tableReferees;

}
