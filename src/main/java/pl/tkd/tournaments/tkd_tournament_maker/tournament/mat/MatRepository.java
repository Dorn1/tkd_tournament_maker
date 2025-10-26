package pl.tkd.tournaments.tkd_tournament_maker.tournament.mat;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament.Tournament;

import java.util.List;

public interface MatRepository extends JpaRepository<Mat, Long> {
    List<Mat> findByTournament(Tournament tournament);
}
