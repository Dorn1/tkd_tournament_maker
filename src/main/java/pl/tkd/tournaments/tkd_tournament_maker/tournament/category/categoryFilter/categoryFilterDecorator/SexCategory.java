package pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categoryFilter.categoryFilterDecorator;

import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Sex;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categoryFilter.ICategoryFilter;

import java.util.Set;
import java.util.stream.Collectors;

public class SexCategory extends CategoryFilterDecorator {
    Sex sex;
    public SexCategory(ICategoryFilter filter, Sex sex) {
        super(filter);
        this.sex = sex;
    }

    @Override
    public Set<Competitor> filter(Set<Competitor> competitors) {
        return super.filter(competitors)
                .stream().
                filter(competitor -> competitor.getSex().equals(sex))
                .collect(Collectors.toSet());
    }
}
