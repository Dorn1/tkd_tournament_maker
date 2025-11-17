package pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.tkd.tournaments.tkd_tournament_maker.club.club.Club;
import java.util.List;
@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    @Query("SELECT t FROM Tournament t WHERE t.organizerClub = :club")
    List<Tournament> findByClubAsOrganizer(Club club);
    @Query("SELECT t FROM Tournament t WHERE :club MEMBER OF t.clubs")
    List<Tournament> findByClubAsMember(Club club);
}
