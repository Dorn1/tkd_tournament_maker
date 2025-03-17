package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;

    @Autowired
    public TournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }
    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }
    public void addTournament() {
//        Date startDate = new Date(2025,3,25);
//        Date endDate = new Date(2025,3,26);
//        Club c = new Club();
//        Tournament t = new Tournament("Opole OpenCup","Opole", startDate, endDate, c);
//        tournamentRepository.save(t);
//        System.out.println(tournamentRepository.findAll());
    }
}
