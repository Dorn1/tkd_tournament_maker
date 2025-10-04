package pl.tkd.tournaments.tkd_tournament_maker.club.competitor;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitorRepository extends JpaRepository<Competitor, Long> {
    Competitor findByUsername(String userName);
}
