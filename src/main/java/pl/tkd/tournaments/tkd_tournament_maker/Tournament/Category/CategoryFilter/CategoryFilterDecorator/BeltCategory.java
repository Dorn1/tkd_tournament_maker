package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterDecorator;

import lombok.Getter;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.ICategoryFilter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class BeltCategory extends CategoryFilterDecorator {
    Integer minbelt;
    Integer maxbelt;
    public BeltCategory(ICategoryFilter filter, Integer minbelt, Integer maxbelt) {
        this.minbelt = minbelt;
        this.maxbelt = maxbelt;
        super(filter);
    }

    @Override
    public Set<Competitor> filter(Set<Competitor> competitors) {
        return super.filter(competitors)
                .stream().
                filter(competitor -> competitor.getBelt().compareTo(maxbelt) >= 0)
                .filter(competitor -> competitor.getBelt().compareTo(minbelt) <= 0)
                .collect(Collectors.toSet());
    }
}
