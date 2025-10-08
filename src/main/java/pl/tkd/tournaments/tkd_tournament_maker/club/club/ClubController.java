package pl.tkd.tournaments.tkd_tournament_maker.club.club;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.CompetitorTableDTO;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.RefereeDTO;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.ObjectNotFoundException;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament.dto.TournamentTableDTO;

import java.util.List;


@Slf4j
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

    @GetMapping(value = "/getClubCompetitors")
    public ResponseEntity<List<CompetitorTableDTO>> getClubCompetitors(@RequestParam String clubName) {
        try {
            return ResponseEntity.ok(clubService.getCompetitorsByClub(clubName));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    @GetMapping(value = "/getClubTournaments")
    public ResponseEntity<List<TournamentTableDTO>> getClubTournaments(@RequestParam String clubName) {
        try {
            return ResponseEntity.ok(clubService.getTournamentsByClub(clubName));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping(value = "/getCompetitor")
    public ResponseEntity<CompetitorTableDTO> getCompetitor(@RequestParam Long competitorId) {
        try {
            return ResponseEntity.ok(clubService.getCompetitorDTOById(competitorId));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/getClubs")
    public ResponseEntity<List<ClubDTO>> getClubs() {
        try {
            return ResponseEntity.ok(clubService.getClubs());
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

}
