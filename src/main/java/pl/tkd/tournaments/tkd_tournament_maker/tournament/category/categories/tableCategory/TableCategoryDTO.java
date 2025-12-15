package pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.tableCategory;

import lombok.Data;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.CompetitorTableDTO;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.mat.MatDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class TableCategoryDTO {
    private MatDTO mat;
    private Long id;
    private String name;
    private Set<TableDataDTO> scores;
    private List<CompetitorTableDTO> competitors;

    private Set<TableDataDTO> rematches;
    
    CompetitorTableDTO firstPlace;
    CompetitorTableDTO secondPlace;
    CompetitorTableDTO thirdPlace;
}
