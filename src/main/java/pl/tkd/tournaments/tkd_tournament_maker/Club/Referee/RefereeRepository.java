package pl.tkd.tournaments.tkd_tournament_maker.Club.Referee;

import pl.tkd.tournaments.tkd_tournament_maker.Club.UserRepository;

import java.util.List;

public interface RefereeRepository extends UserRepository<Referee> {
    List<Referee> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstname, String lastname);
}
