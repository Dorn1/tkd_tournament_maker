package pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.tableCategory;

import org.springframework.stereotype.Repository;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableCategoryRepository extends CategoryRepository<TableCategory> {
    Optional<List<TableCategory>> findByMatIdIsNullAndTournamentId(Long tournamentId);
}
