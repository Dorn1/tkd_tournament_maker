package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Club.Club;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Club.ClubService;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.ObjectNotFoundException;

import java.util.Date;
import java.util.List;

@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final ClubService clubService;

    @Autowired
    public TournamentService(TournamentRepository tournamentRepository, ClubService clubService) {
        this.tournamentRepository = tournamentRepository;
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
        System.out.println(tournamentRepository.findAll());
    }
}
