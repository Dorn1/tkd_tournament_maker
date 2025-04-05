package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterDecorator;

import lombok.Getter;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Belt;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.ICategoryFilter;

@Getter
public class BeltCategory extends CategoryFilterDecorator {
    Belt minbelt;
    Belt maxbelt;
    public BeltCategory(ICategoryFilter filter, Belt minbelt, Belt maxbelt) {
        this.minbelt = minbelt;
        this.maxbelt = maxbelt;
        super(filter);
    }
}
