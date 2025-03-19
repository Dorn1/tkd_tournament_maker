package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Club.Club;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Club.ClubService;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Mat.Mat;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Mat.MatRepository;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.ObjectNotFoundException;

import java.util.Date;
import java.util.List;

@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final MatRepository matRepository;
    private final ClubService clubService;



    @Autowired
    public TournamentService(TournamentRepository tournamentRepository,
                             MatRepository matRepository,
                             ClubService clubService) {
        this.tournamentRepository = tournamentRepository;
        this.matRepository = matRepository;
        this.clubService = clubService;
    }



    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    public void addTournament(String name,
                              String location,
                              Long startDatenum,
                              Long endDatenum,
                              Long organizerId) throws ObjectNotFoundException {
        Date startDate = new Date(startDatenum);
        Date endDate = new Date(endDatenum);
        Club c = clubService.getClub(organizerId);
        Tournament t = new Tournament(name,location, startDate, endDate, c);
        tournamentRepository.save(t);
    }

    public void addMat(Long tournamentId) throws ObjectNotFoundException {
        if(tournamentRepository.findById(tournamentId).isEmpty())
            throw new ObjectNotFoundException("Tournament doesn't exist");
        Tournament tournament = tournamentRepository.findById(tournamentId).get();
        Mat mat = new Mat();
        mat.setTournament(tournament);
        tournament.getMats().add(mat);
        matRepository.save(mat);
        tournamentRepository.save(tournament);
    }
}
