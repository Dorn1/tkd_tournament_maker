package pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories;

import lombok.Data;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.CompetitorTableDTO;

@Data
public class CategoryDTO {
    private String name;
    private Long id;
    private String type;
    CompetitorTableDTO firstPlace;
    CompetitorTableDTO secondPlace;
    CompetitorTableDTO thirdPlace;

}
