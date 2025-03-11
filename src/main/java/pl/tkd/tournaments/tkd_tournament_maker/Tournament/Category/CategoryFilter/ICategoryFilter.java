package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter;

import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;

import java.util.Set;

public interface ICategoryFilter {
    public Set<Competitor> filter(Set<Competitor> competitors);
}

