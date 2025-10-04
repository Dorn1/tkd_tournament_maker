package pl.tkd.tournaments.tkd_tournament_maker.club.referee;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tkd.tournaments.tkd_tournament_maker.club.club.Club;

import java.util.List;

public interface RefereeRepository extends JpaRepository<Referee, Long> {

    Referee findByUsername(String userName);

    List<Referee> findByClub(Club club);
}
