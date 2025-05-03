package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Club.Club;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Club.ClubService;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.*;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories.Category;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories.CategoryRepository;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories.LadderCategory;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories.TableCategory;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Mat.Mat;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Mat.MatRepository;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.ObjectNotFoundException;

import java.util.Date;

@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final MatRepository matRepository;
    private final CategoryRepository categoryRepository;
    private final FightRepository fightRepository;
    private final ClubService clubService;



    @Autowired
    public TournamentService(TournamentRepository tournamentRepository,
                             MatRepository matRepository,
                             CategoryRepository categoryRepository,
                             ClubService clubService,
                             FightRepository fightRepository) {
        this.tournamentRepository = tournamentRepository;
        this.matRepository = matRepository;
        this.categoryRepository = categoryRepository;
        this.clubService = clubService;
        this.fightRepository = fightRepository;
    }



    public void addTournament(String name,
                              String location,
                              Long startDatenum,
                              Long endDatenum,
                              Long organizerId) throws ObjectNotFoundException {
        Date startDate = new Date(startDatenum);
        Date endDate = new Date(endDatenum);
        Club c = clubService.getClubById(organizerId);
        Tournament t = new Tournament(name,location, startDate, endDate, c);
        tournamentRepository.save(t);
    }


    public void addMat(Long tournamentId) throws ObjectNotFoundException {
        Tournament tournament = getTournament(tournamentId);
        Mat mat = new Mat();
        mat.setTournament(tournament);
        tournament.getMats().add(mat);
        matRepository.save(mat);
        tournamentRepository.save(tournament);
    }

    public void addCategory(Long matId, boolean ladderCategory) throws ObjectNotFoundException{
        Mat mat = getMat(matId);
        Category category = new TableCategory();
        if(ladderCategory)
            category = new LadderCategory();
        //Category Filtering logic needed here
        mat.getCategoryQueque().add(category);
        matRepository.save(mat);
        categoryRepository.save(category);
    }

    public Mat getMat(Long id) throws ObjectNotFoundException {
        if (matRepository.findById(id).isPresent())
            return matRepository.findById(id).get();
        throw new ObjectNotFoundException("Tournament doesn't exist");
    }

    public Tournament getTournament(Long id) throws ObjectNotFoundException {
        if (tournamentRepository.findById(id).isPresent())
            return tournamentRepository.findById(id).get();
        throw new ObjectNotFoundException("Tournament doesn't exist");
    }
    
    public void addCompetitorToTournament(Long CompetitorId, Long tournamentId) throws ObjectNotFoundException {
        Tournament tournament = getTournament(tournamentId);
        Competitor competitor = clubService.getCompetitorById(CompetitorId);
        tournament.getCompetitors().add(competitor);
    }
}
