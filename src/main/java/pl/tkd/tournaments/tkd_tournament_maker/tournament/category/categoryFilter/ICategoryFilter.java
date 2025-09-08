package pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categoryFilter;

import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Competitor;

import java.util.Set;

public interface ICategoryFilter {
    Set<Competitor> filter(Set<Competitor> competitors);
}

