package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories.TableCategory;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories.CategoryRepository;

@Repository
public interface TableCategoryRepository extends CategoryRepository<TableCategory> {
}
