package pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.ladderCategory;

import lombok.Data;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.CompetitorTableDTO;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.mat.MatDTO;

import java.util.List;

@Data
public class LadderCategoryDTO {
    private  Long id;
    private List<CompetitorTableDTO> competitors;
    private MatDTO mat;
    private FightDTO firstPlaceFight;

    private FightDTO thridPlaceFight;
}
