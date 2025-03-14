package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterDecorator;

import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.ICategoryFilter;

import java.util.HashSet;
import java.util.Set;

public class BeltCategory extends CategoryFilterDecorator {
    public BeltCategory(ICategoryFilter filter, Integer min, Integer max) {
        super(filter,min,max);
    }
}
