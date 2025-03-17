package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterDecorator;


import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.ICategoryFilter;


public class AgeCategory extends CategoryFilterDecorator {
    public AgeCategory(ICategoryFilter filter, Integer min, Integer max) {
        super(filter,min,max);
    }
}
