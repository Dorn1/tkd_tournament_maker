package pl.tkd.tournaments.tkd_tournament_maker.club.club;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    Club findByUsername(String userName);
}
