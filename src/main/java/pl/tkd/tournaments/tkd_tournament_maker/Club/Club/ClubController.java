package pl.tkd.tournaments.tkd_tournament_maker.Club.Club;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Sex;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.ObjectNotFoundException;

@RestController
public class ClubController {

    private final ClubService clubService;
    @Autowired
    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }
    @PostMapping(value = "/newClub")
    public ResponseEntity<String> newClub(@RequestParam String name) {
        clubService.addClub(name);
        return ResponseEntity.ok("Club added");
    }
    @PostMapping(value = "/newCompetitor")
    public ResponseEntity<String> newCompetitor(@RequestParam String firstName,
                              @RequestParam String lastName,
                              @RequestParam boolean male,
                              @RequestParam Long birthDate,
                              @RequestParam Long clubId) {
        Sex competitorSex = Sex.Female;
        if (male){
            competitorSex = Sex.Male;
        }
        try {
            clubService.addCompetitorToClub(firstName,
                                            lastName,
                                            competitorSex,
                                            birthDate,
                                            clubId);
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        return ResponseEntity.ok("Competitor added");
    }
}
