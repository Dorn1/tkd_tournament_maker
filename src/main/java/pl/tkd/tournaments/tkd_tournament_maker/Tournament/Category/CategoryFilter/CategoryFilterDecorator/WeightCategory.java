package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterDecorator;

import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;

import java.util.Set;

public class WeightCategory implements ICategoryFilterDecorator{
    @Override
    public Set<Competitor> filter(Set<Competitor> competitors) {
        return Set.of();
    }
}
