package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.ObjectNotFoundException;

@RestController
public class TournamentController {

    private final TournamentService tournamentService;
    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }
    @PostMapping(value = "/newTournament")
    public ResponseEntity<String> newTournament(@RequestParam String name,
                                        @RequestParam String location,
                                        @RequestParam Long startDate,
                                        @RequestParam Long endDate,
                                        @RequestParam Long organizerId) {
        try{
        tournamentService.addTournament(name,location,startDate,endDate,organizerId);
        return ResponseEntity.ok("Tournament created");
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
