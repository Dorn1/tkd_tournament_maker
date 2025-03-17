package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterDecorator;

import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.ICategoryFilter;


public class BeltCategory extends CategoryFilterDecorator {
    public BeltCategory(ICategoryFilter filter, Integer min, Integer max) {
        super(filter,min,max);
    }
}
