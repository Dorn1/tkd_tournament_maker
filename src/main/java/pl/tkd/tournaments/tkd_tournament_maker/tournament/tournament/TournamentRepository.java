package pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.tkd.tournaments.tkd_tournament_maker.club.club.Club;
import java.util.List;
@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    List<Tournament> findByOrganizer_Club(Club club);
}
