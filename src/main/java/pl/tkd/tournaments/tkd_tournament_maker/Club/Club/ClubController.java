package pl.tkd.tournaments.tkd_tournament_maker.Club.Club;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping(value = "/getClubByName")
    public ResponseEntity<String> getClubByName(@RequestParam String name) {
        List<Club> club = clubService.getClubByName(name);
        return ResponseEntity.ok(List.of(club).toString());

    }

    @PostMapping(value = "/newClub")
    public ResponseEntity<String> newClub(@RequestParam String name) {
        clubService.addClub(name);
        logger.info("New club {} created", name);
        return ResponseEntity.ok("Club added");
    }

    @PostMapping(value = "/newCompetitor")
    public ResponseEntity<String> newCompetitor(@RequestParam String firstName,
                                                @RequestParam String lastName,
                                                @RequestParam boolean male,
                                                @RequestParam Long birthDate,
                                                @RequestParam Long clubId) {
        try {
            clubService.addCompetitorToClub(firstName,
                    lastName,
                    male,
                    birthDate,
                    clubId);
            logger.info("New competitor {} {} added", firstName, lastName);
            return ResponseEntity.ok("Competitor added");
        } catch (ObjectNotFoundException e) {
            logger.warn("attempt to access a non-existent club");
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping(value = "/newReferee")
    public ResponseEntity<String> newReferee(@RequestParam String firstName,
                                             @RequestParam String lastName,
                                             @RequestParam Long clubId) {
        try {
            clubService.addRefereeToClub(firstName, lastName, clubId);
            logger.info("New referee {} {} added", firstName, lastName);
        } catch (ObjectNotFoundException e) {
            logger.warn("attempt to access a non-existent club");
            return ResponseEntity.status(404).body(e.getMessage());
        }
        return ResponseEntity.ok("Competitor added");
    }
}
