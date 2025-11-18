package pl.tkd.tournaments.tkd_tournament_maker.tournament;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.tkd.tournaments.tkd_tournament_maker.club.club.ClubDTO;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.RefereeDTO;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.Category;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.CategoryDTO;
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
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping(value = "/getTableWinners")
    public ResponseEntity<String> getTableWinners(@RequestParam Long categoryId) {
        try {
            TableCategory tableCategory = tournamentService.getTableCategoryById(categoryId);
            try {
                List<PlaceWrapper> winners = tournamentService.getTableWinners(tableCategory);
                return ResponseEntity.ok(winners.toString());
            } catch (RematchNeededException e) {
                tournamentService.setTableRematch(tableCategory, e.getCompetitors());
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

    @PatchMapping(value = "/addClubToTournament")
    public ResponseEntity<String> addClubToTournament(@RequestParam Long clubId,
                                                      @RequestParam Long tournamentId) {
        try {
            tournamentService.addClubToTournament(clubId, tournamentId);
            logger.info("Club added to Tournament");
            return ResponseEntity.ok("Club added to Tournament");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PatchMapping(value = "/removeClubFromTournament")
    public ResponseEntity<String> removeClubFromTournament(@RequestParam Long clubId,
                                                           @RequestParam Long tournamentId) {
        try {
            tournamentService.removeClubFromTournament(clubId, tournamentId);
            logger.info("Club removed from Tournament");
            return ResponseEntity.ok("Club removed from Tournament");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping(value = "/newCategory")
    public ResponseEntity<String> newCategory(
            @RequestParam Long tournamentId,
            @RequestParam String categoryType,
            @RequestParam String categoryName,
            @RequestBody Map<String, String> filterParams) {
        try {
            tournamentService.addCategory(categoryName, tournamentId, categoryType, filterParams);
            logger.info("New Category created");
            return ResponseEntity.ok("Category created");
        } catch (ObjectNotFoundException e) {
            logger.warn("attempt to access a non-existent mat or category type");
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

    @PatchMapping(value = "/addRefereeToTournament")
    public ResponseEntity<String> addRefereeToTournament(@RequestParam Long refereeId,
                                                         @RequestParam Long tournamentId) {
        try {
            tournamentService.addRefereeToTournamnet(refereeId, tournamentId);
            return ResponseEntity.ok("Referee added to tournament");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping(value = "/removeRefereeFromTournament")
    public ResponseEntity<String> removeRefereeFromTournament(@RequestParam Long refereeId,
                                                              @RequestParam Long tournamentId) {
        try {
            tournamentService.removeRefereeFormTournamnet(refereeId, tournamentId);
            return ResponseEntity.ok("Referee added to tournament");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/addRefereeToMat")
    public ResponseEntity<String> addRefereeToMat(@RequestParam Long refereeId,
                                                  @RequestParam Long matId) {
        tournamentService.addRefereeToMat(refereeId, matId);
        return ResponseEntity.ok("Referee added to mat");
    }

    @PostMapping(value = "/removeRefereeFromMat")
    public ResponseEntity<String> removeRefereeRomMat(@RequestParam Long refereeId,
                                                      @RequestParam Long matId) {
        try {
            tournamentService.removeRefereeFromMat(refereeId, matId);
            return ResponseEntity.ok("Referee removed from mat");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping(value = "/addCompetitorToTournament")
    public ResponseEntity<String> addCompetitorToTournament(@RequestParam Long competitorId,
                                                            @RequestParam Long tournamentID) {
        try {
            tournamentService.addCompetitorToTournament(competitorId, tournamentID);
            return ResponseEntity.ok("competitor added to tournament");
        } catch (ObjectNotFoundException e) {
            logger.warn("attempt to access a non-existent tournament or competitor");
            return ResponseEntity.status(404).body(e.getMessage());
        }

    }

    @PostMapping(value = "/setWinner")
    public ResponseEntity<String> setWinner(@RequestParam Long fightId,
                                            @RequestParam Boolean wonFirst) {
        try {
            tournamentService.setFightWinner(wonFirst, fightId);
            return ResponseEntity.ok("");
        } catch (Exception e) {
            logger.warn("attempt to access a non-existent fight or competitor");
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping(value = "/getMat")
    public ResponseEntity<MatDTO> getMat(@RequestParam Long matId) {
        try {
            MatDTO dto = tournamentService.getMatDTO(matId);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping(value = "/removeMat")
    public ResponseEntity<String> removeMat(@RequestParam Long matId) {
        try {
            tournamentService.removeMat(matId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping(value = "/getTournamentLeaders")
    public ResponseEntity<List<RefereeDTO>> getTournamentLeaders(@RequestParam Long tournamentId) {
        try {
            List<RefereeDTO> dtos = tournamentService.getLeaders(tournamentId);
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping(value = "/getTournamentReferees")
    public ResponseEntity<List<RefereeDTO>> getTournamentReferees(@RequestParam Long tournamentId) {
        try {
            List<RefereeDTO> dtos = tournamentService.getReferees(tournamentId);
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping(value = "/getTournamentClubs")
    public ResponseEntity<List<ClubDTO>> getTournamentClubs(@RequestParam Long tournamentId) {
        try {
            List<ClubDTO> dtos = tournamentService.getTournamentClubs(tournamentId);
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping(value = "/getCategoryName")
    public ResponseEntity<String> getCategoryName(@RequestParam Long categoryId) {
        try {
            String name = tournamentService.getCategoryNameById(categoryId);
            return ResponseEntity.ok(name);
        } catch (Exception e) {
            logger.warn("attempt to access a non-existent category");
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping(value = "/getFreeCategories")
    public ResponseEntity<List<CategoryDTO>> getFreeCategories(@RequestParam Long tournamentId) {
        try {
            List<CategoryDTO> categories = tournamentService.getFreeCategories(tournamentId);
            return ResponseEntity.ok().body(categories);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping(value = "/setMatLeader")
    public ResponseEntity<String> setMatLeader(Long matId, Long leaderId) {
        try {
            tournamentService.setMatLeader(matId, leaderId);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PatchMapping(value = "/addCategoryToMat")
    public ResponseEntity<String> addCategoryToMat(Long categoryId, Long matId, String categoryType) {
        try {
            tournamentService.addCategoryToMat(categoryId, matId, categoryType);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping(value = "/removeCategoryFromMat")
    public ResponseEntity<String> removeCategoryFromMat(Long categoryId, Long matId, String categoryType) {
        try {
            tournamentService.removeCategoryFromMat(categoryId, matId, categoryType);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/getMatCategories")
    public ResponseEntity<List<CategoryDTO>> getMatCategories(@RequestParam Long matId){
        try {
            List<CategoryDTO> dtos = tournamentService.getMatCategories(matId);
            return ResponseEntity.ok(dtos);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }


}
