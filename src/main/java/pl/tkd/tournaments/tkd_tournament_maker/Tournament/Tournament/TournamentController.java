package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Tournament;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TournamentController {
    @GetMapping(value = "/test")
    public List<String> test() {

        return List.of("test1");
    }
}
