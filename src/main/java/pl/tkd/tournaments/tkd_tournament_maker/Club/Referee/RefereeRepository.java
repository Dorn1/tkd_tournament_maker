package pl.tkd.tournaments.tkd_tournament_maker.Club.Referee;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;

public interface RefereeRepository extends JpaRepository<Referee, Long> {
}
