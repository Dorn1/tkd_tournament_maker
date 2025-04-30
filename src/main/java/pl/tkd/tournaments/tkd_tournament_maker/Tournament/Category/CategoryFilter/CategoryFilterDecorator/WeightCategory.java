package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterDecorator;

import lombok.Getter;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.ICategoryFilter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class WeightCategory extends CategoryFilterDecorator {
    Double min;
    Double max;
    public WeightCategory(ICategoryFilter filter, Double min, Double max) {
        super(filter);
        this.min = min;
        this.max = max;
    }

    @Override
    public Set<Competitor> filter(Set<Competitor> competitors) {
        return super.filter(competitors)
                .stream().
                filter(competitor -> competitor.getWeight() <= max)
                .filter(competitor -> competitor.getWeight() >= min)
                .collect(Collectors.toSet());
    }
}