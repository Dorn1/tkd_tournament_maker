package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterDecorator;

import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.ICategoryFilter;

import java.util.Set;

public abstract class CategoryFilterDecorator implements ICategoryFilter {
    private final ICategoryFilter base_filter;
    public CategoryFilterDecorator(ICategoryFilter base_filter){
        this.base_filter = base_filter;
    }
    @Override
    public Set<Competitor> filter(Set<Competitor> competitors) {
        return base_filter.filter(competitors);
    }

}
