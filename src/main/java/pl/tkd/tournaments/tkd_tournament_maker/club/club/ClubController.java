package pl.tkd.tournaments.tkd_tournament_maker.club.club;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.Referee;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.ObjectNotFoundException;

import java.util.List;


@RestController
public class ClubController {

    private final ClubService clubService;
    private static final Logger logger = LoggerFactory.getLogger(ClubController.class);


    @Autowired
    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @GetMapping(value = "/getCompetitorById")
    public ResponseEntity<String> getCompetitorById(@RequestParam Long id) {
        try {
            Competitor competitor = clubService.getCompetitorById(id);
            return ResponseEntity.ok(competitor.toString());
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping(value = "/getClubByName")
    public ResponseEntity<String> getClubByName(@RequestParam String name) {
        List<Club> clubs = clubService.getClubByName(name);
        return ResponseEntity.ok(clubs.toString());

    }

    @GetMapping(value = "/getRefereeByName")
    public ResponseEntity<String> getClubByName(@RequestParam String firstName, @RequestParam String lastName) {
        List<Referee> refereeList = clubService.getRefereeByName(firstName, lastName);
        return ResponseEntity.ok(refereeList.toString());

    }

}
