package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CategoryRepository<T> extends JpaRepository<T, Long> {
}
