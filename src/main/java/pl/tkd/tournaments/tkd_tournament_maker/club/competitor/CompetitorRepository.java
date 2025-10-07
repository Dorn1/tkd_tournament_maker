package pl.tkd.tournaments.tkd_tournament_maker.club.competitor;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tkd.tournaments.tkd_tournament_maker.club.club.Club;

import java.util.List;

public interface CompetitorRepository extends JpaRepository<Competitor, Long> {
    Competitor findByUsername(String userName);

    List<Competitor> findByClub(Club club);
}
