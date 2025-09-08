package pl.tkd.tournaments.tkd_tournament_maker.Club.Club;

import org.springframework.stereotype.Repository;
import pl.tkd.tournaments.tkd_tournament_maker.Club.UserRepository;

import java.util.List;

@Repository
public interface ClubRepository extends UserRepository<Club> {
    List<Club> findByNameContainingIgnoreCase(String name);
}
