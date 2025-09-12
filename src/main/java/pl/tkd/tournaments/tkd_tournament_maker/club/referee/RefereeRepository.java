package pl.tkd.tournaments.tkd_tournament_maker.club.referee;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tkd.tournaments.tkd_tournament_maker.club.user.UserRepository;

import java.util.List;

public interface RefereeRepository extends JpaRepository<Referee, Long> {
    List<Referee> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstname, String lastname);
}
