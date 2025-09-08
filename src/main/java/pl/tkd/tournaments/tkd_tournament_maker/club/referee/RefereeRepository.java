package pl.tkd.tournaments.tkd_tournament_maker.club.referee;

import pl.tkd.tournaments.tkd_tournament_maker.club.user.UserRepository;

import java.util.List;

public interface RefereeRepository extends UserRepository<Referee> {
    List<Referee> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstname, String lastname);
}
