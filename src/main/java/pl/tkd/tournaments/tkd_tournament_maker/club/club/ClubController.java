package pl.tkd.tournaments.tkd_tournament_maker.club.club;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping(value = "/getClubReferees")
    public ResponseEntity<List<RefereeDTO>> getClubReferees(@RequestParam String clubName) {
        try {
            return ResponseEntity.ok(clubService.getRefereesByClub(clubName));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }

    }

    @GetMapping(value = "/getReferee")
    public ResponseEntity<RefereeDTO> getReferee(@RequestParam Long refereeId) {
        try {
            return ResponseEntity.ok(clubService.getRefereeDTOById(refereeId));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
