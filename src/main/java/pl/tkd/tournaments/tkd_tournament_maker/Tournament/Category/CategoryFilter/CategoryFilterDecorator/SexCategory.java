package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterDecorator;

import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Sex;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.ICategoryFilter;

public class SexCategory extends CategoryFilterDecorator {
    boolean male;
    public SexCategory(ICategoryFilter filter, Sex sex) {
        male = !sex.equals(Sex.Female);
        super(filter);
    }
}
