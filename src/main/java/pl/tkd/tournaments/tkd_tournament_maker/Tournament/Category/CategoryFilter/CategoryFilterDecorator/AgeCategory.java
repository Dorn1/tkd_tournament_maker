package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterDecorator;


import lombok.Getter;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.ICategoryFilter;

import java.util.Set;
import java.util.stream.Collectors;


@Getter
public class AgeCategory extends CategoryFilterDecorator {
    Integer min;
    Integer max;
    public AgeCategory(ICategoryFilter filter, Integer min, Integer max) {
        this.min = min;
        this.max = max;
        super(filter);
    }

    @Override
    public Set<Competitor> filter(Set<Competitor> competitors) {
        return super.filter(competitors)
                .stream().
                filter(competitor -> competitor.getAge() >= min)
                .filter(competitor -> competitor.getAge() <= max)
                .collect(Collectors.toSet());
    }
}
