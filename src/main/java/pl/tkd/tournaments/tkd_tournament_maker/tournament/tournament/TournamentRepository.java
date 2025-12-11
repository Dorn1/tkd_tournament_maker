package pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.tkd.tournaments.tkd_tournament_maker.club.club.Club;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.Referee;

import java.util.Date;
import java.util.List;
@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    @Query("SELECT t FROM Tournament t WHERE t.organizerClub = :club")
    List<Tournament> findByClubAsOrganizer(Club club);
    @Query("SELECT t FROM Tournament t WHERE :club MEMBER OF t.clubs")
    List<Tournament> findByClubAsMember(Club club);

    @Query("SELECT t FROM Tournament t WHERE :referee MEMBER OF t.referees")
    List<Tournament> findTournamentsByRefereeInReferees(Referee referee);

    @Query("SELECT t FROM Tournament t WHERE :competitor MEMBER OF t.competitors")
    List<Tournament> findTournamentsByCompetitorInCompetitors(Competitor competitor);

    List<Tournament> findByStartDateAfter(Date today);

    List<Tournament> findByStartDateBefore(Date today);
}
