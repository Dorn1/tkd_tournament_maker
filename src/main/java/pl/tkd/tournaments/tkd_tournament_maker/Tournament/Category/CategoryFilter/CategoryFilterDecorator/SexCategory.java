package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterDecorator;

import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Sex;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.ICategoryFilter;

import java.util.Set;
import java.util.stream.Collectors;

public class SexCategory extends CategoryFilterDecorator {
    Sex sex;
    public SexCategory(ICategoryFilter filter, Sex sex) {
        this.sex = sex;
        super(filter);
    }

    @Override
    public Set<Competitor> filter(Set<Competitor> competitors) {
        return super.filter(competitors)
                .stream().
                filter(competitor -> competitor.getSex().equals(sex))
                .collect(Collectors.toSet());
    }
}
