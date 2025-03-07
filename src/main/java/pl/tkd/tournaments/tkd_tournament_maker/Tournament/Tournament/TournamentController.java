package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Tournament;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TournamentController {
    @GetMapping(value = "/test")
    public String test() {

        return "test1";
    }
}
