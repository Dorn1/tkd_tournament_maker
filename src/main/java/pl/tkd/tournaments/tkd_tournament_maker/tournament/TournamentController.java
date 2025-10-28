package pl.tkd.tournaments.tkd_tournament_maker.tournament;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.ladderCategory.FightDTO;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.ladderCategory.LadderCategoryDTO;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.tableCategory.*;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.ObjectNotFoundException;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.RematchNeededException;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.mat.MatDTO;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament.dto.CreateTournamentRequest;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/tournament")
public class TournamentController {

    private final TournamentService tournamentService;
    private static final Logger logger = LoggerFactory.getLogger(TournamentController.class);


    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @PostMapping(value = "/newTournament")
    public ResponseEntity<String> newTournament(@RequestBody CreateTournamentRequest request) {
        try {
            tournamentService.addTournament(request.getName(),
                    request.getLocation(),
                    request.getStartDate(),
                    request.getEndDate(),
                    request.getOrganizerName());
            logger.info("New tournament {} created", request.getName());
            return ResponseEntity.ok("Tournament created");
        } catch (ObjectNotFoundException e) {
            logger.warn("attempt to access a non-existent club");
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping(value = "/getTournamentMats")
    public ResponseEntity<List<MatDTO>> getTournamentMats(@RequestParam Long tournamentId) {
        try {
            List<MatDTO> mats = tournamentService.getMatsByTournamentId(tournamentId);
            return ResponseEntity.ok(mats);
        } catch ( Exception e) {
            return ResponseEntity.status(404).body(null);
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
    public ResponseEntity<String> newMat(@RequestParam Long tournamentId, @RequestParam Long mat_Leader_Id) {
        try {
            tournamentService.addMat(tournamentId, mat_Leader_Id);
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
                                              @RequestParam boolean ladderCategory,
                                              @RequestBody Map<String, String> filterParams) {
        try {
            tournamentService.addCategory(matId, ladderCategory, filterParams);
            logger.info("New Category created");
            return ResponseEntity.ok("Category created");
        } catch (ObjectNotFoundException e) {
            logger.warn("attempt to access a non-existent mat");
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping(value = "/getLadderCategory")
    public ResponseEntity<LadderCategoryDTO> getLadderCategory(@RequestParam Long categoryId) {
        LadderCategoryDTO ladderCategoryDTO = tournamentService.getLadderCategoryById(categoryId);
        return ResponseEntity.ok(ladderCategoryDTO);
    }

    @GetMapping(value = "/getFight")
    public ResponseEntity<FightDTO> getFight(@RequestParam Long fightId) {
        FightDTO fightDTO = tournamentService.getFightDTOById(fightId);
        return ResponseEntity.ok(fightDTO);
    }

    @PostMapping(value = "/addRefereeToMat")
    public ResponseEntity<String> addRefereeToMat(@RequestParam Long refereeId,
                                                  @RequestParam Long matId) {
        tournamentService.addRefereeToMat(refereeId, matId);
        return ResponseEntity.ok("Referee added to mat");
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

    @PostMapping(value = "/setWinner")
    public ResponseEntity<String> setWinner(@RequestParam Long fightId,
                                            @RequestParam Boolean wonFirst) {
        try{
            tournamentService.setFightWinner(wonFirst, fightId);
            return ResponseEntity.ok("");
        } catch (Exception e) {
            logger.warn("attempt to access a non-existent fight or competitor");
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
    @GetMapping(value = "/getCategoryName")
    public ResponseEntity<String> getCategoryName(@RequestParam Long categoryId) {
        try{
            String name = tournamentService.getCategoryNameById(categoryId);
            return ResponseEntity.ok(name);
        } catch (Exception e) {
            logger.warn("attempt to access a non-existent category");
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
