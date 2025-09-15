package pl.tkd.tournaments.tkd_tournament_maker.tournament;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.tableCategory.*;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.ObjectNotFoundException;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.RematchNeededException;

import java.util.List;

@RestController
@RequestMapping("/tournament")
public class TournamentController {

    private final TournamentService tournamentService;
    private static final Logger logger = LoggerFactory.getLogger(TournamentController.class);


    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @PostMapping(value = "/newTournament")
    public ResponseEntity<String> newTournament(@RequestParam String name,
                                                @RequestParam String location,
                                                @RequestParam Long startDate,
                                                @RequestParam Long endDate,
                                                @RequestParam Long organizerId) {
        try {
            tournamentService.addTournament(name,
                    location,
                    startDate,
                    endDate,
                    organizerId);
            logger.info("New tournament {} created", name);
            return ResponseEntity.ok("Tournament created");
        } catch (ObjectNotFoundException e) {
            logger.warn("attempt to access a non-existent club");
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping(value = "/getTableWinners")
    public ResponseEntity<String> getTableWinners(@RequestParam Long categoryId) {
        try {
            TableCategory tableCategory = tournamentService.getTableCategoryById(categoryId);
            try{
            List<PlaceWrapper> winners = tournamentService.getTableWinners(tableCategory);
                return ResponseEntity.ok(winners.toString());
            }catch (RematchNeededException e){
                tournamentService.setTableRematch(tableCategory,e.getCompetitors());
                return ResponseEntity.status(399).body(e.getMessage());
            }
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping(value = "/newMat")
    public ResponseEntity<String> newMat(@RequestParam Long tournamentId) {
        try {
            tournamentService.addMat(tournamentId);
            logger.info("New mat created");
            return ResponseEntity.ok("Mat created");
        } catch (ObjectNotFoundException e) {
            logger.warn("attempt to access a non-existent tournament");
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping(value = "/newCategory")
    //need to change parameters here
    public ResponseEntity<String> newCategory(@RequestParam Long matId,
                                              boolean ladderCategory) {
        try {
            tournamentService.addCategory(matId, ladderCategory);
            logger.info("New Category created");
            return ResponseEntity.ok("Category created");
        } catch (ObjectNotFoundException e) {
            logger.warn("attempt to access a non-existent mat");
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping(value = "/addCompetitorToTournament")
    public ResponseEntity<String> addCompetitorToTournament(@RequestParam Long competitorId,
                                                            @RequestParam Long tournamentID) {
        try{
            tournamentService.addCompetitorToTournament(competitorId,tournamentID);
            return ResponseEntity.ok("competitor added to tournament");
        } catch (ObjectNotFoundException e) {
            logger.warn("attempt to access a non-existent tournament or competitor");
            return ResponseEntity.status(404).body(e.getMessage());
        }

    }
}
