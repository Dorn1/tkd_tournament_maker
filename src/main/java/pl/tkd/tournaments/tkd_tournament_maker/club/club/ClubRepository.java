package pl.tkd.tournaments.tkd_tournament_maker.club.club;

import org.springframework.stereotype.Repository;
import pl.tkd.tournaments.tkd_tournament_maker.club.user.UserRepository;

import java.util.List;

@Repository
public interface ClubRepository extends UserRepository<Club> {
    List<Club> findByNameContainingIgnoreCase(String name);
}
