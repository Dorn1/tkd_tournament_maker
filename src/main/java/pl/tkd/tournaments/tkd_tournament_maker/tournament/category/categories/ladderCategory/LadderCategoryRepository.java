package pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.ladderCategory;

import org.springframework.stereotype.Repository;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LadderCategoryRepository extends CategoryRepository<LadderCategory> {
    Optional<List<LadderCategory>> findByMatIdIsNullAndTournamentId(Long tournamentId);
}
