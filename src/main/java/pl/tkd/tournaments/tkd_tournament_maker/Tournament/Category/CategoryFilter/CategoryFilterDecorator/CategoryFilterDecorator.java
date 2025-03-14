package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterDecorator;

import lombok.Getter;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.ICategoryFilter;

import java.util.Set;

public abstract class CategoryFilterDecorator implements ICategoryFilter {
    private final ICategoryFilter base_filter;
    @Getter
    private final Integer min;
    @Getter
    private final Integer max;
    public CategoryFilterDecorator(ICategoryFilter base_filter, Integer min, Integer max) {
        this.base_filter = base_filter;
        this.min = min;
        this.max = max;
    }
    @Override
    public Set<Competitor> filter(Set<Competitor> competitors) {
        return base_filter.filter(competitors);
    }

}
