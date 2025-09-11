package pl.tkd.tournaments.tkd_tournament_maker.club.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface UserRepository<T> extends JpaRepository<T, Long> {
}
