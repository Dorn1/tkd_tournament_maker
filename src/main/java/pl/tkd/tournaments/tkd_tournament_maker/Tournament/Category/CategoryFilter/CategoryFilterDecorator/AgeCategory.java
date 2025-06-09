package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterDecorator;

import lombok.Getter;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.ICategoryFilter;

import java.util.Set;
import java.util.stream.Collectors;


@Getter
public class  AgeCategory extends CategoryFilterDecorator {
    Long min;
    Long max;

    public AgeCategory(ICategoryFilter filter, Long min, Long max) {
        super(filter);
        this.min = min;
        this.max = max;
    }

    @Override
    public Set<Competitor> filter(Set<Competitor> competitors) {
        Long year = (long) java.time.Year.now().getValue();
        return super.filter(competitors)
                .stream().
                filter(competitor -> year - competitor.getBirthYear() >= min)
                .filter(competitor -> year - competitor.getBirthYear() <= max)
                .collect(Collectors.toSet());
    }
}
