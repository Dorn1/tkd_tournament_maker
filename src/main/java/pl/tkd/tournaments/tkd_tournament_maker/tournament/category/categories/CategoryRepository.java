package pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CategoryRepository<T> extends JpaRepository<T, Long> {
}
