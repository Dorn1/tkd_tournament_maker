package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterDecorator;

import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;

import java.util.HashSet;
import java.util.Set;

public class BeltCategory implements ICategoryFilterDecorator{
    @Override
    public Set<Competitor> filter(Set<Competitor> competitors) {

        return Set.of();
    }
}
