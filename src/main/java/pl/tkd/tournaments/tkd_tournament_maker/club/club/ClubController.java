package pl.tkd.tournaments.tkd_tournament_maker.club.club;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.Referee;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.RefereeDTO;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.ObjectNotFoundException;

import java.util.List;


@RestController
@CrossOrigin("*")
public class ClubController {

    private final ClubService clubService;


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
        Club club = clubService.getClubByName(name);
        return ResponseEntity.ok(club.toString());

    }

    @GetMapping(value = "/getClubReferees")
    public ResponseEntity<List<RefereeDTO>> getClubReferees(@RequestParam String clubName) {
        try {
            return ResponseEntity.ok(clubService.getRefereesByClub(clubName));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }

    }

}
