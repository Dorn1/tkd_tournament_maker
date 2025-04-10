package pl.tkd.tournaments.tkd_tournament_maker.Club.Club;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    List<Club> findByNameContainingIgnoreCase(String name);
}
