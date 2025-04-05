package pl.tkd.tournaments.tkd_tournament_maker.Club.Referee;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefereeRepository extends JpaRepository<Referee, Long> {
    List<Referee> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstname, String lastname);
}
