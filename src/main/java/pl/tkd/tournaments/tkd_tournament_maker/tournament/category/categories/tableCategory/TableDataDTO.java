package pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.tableCategory;

import lombok.Data;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.CompetitorTableDTO;

@Data
public class TableDataDTO {
    private Long id;

    private CompetitorTableDTO competitor;

    private Long score;
}
