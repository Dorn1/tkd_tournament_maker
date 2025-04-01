package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Club.Club;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Club.ClubService;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Category;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryRepository;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.LadderCategory;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.TableCategory;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Mat.Mat;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Mat.MatRepository;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.ObjectNotFoundException;

import java.util.Date;
import java.util.List;

@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final MatRepository matRepository;
    private final CategoryRepository categoryRepository;
    private final ClubService clubService;



    @Autowired
    public TournamentService(TournamentRepository tournamentRepository,
                             MatRepository matRepository,
                             CategoryRepository categoryRepository,
                             ClubService clubService) {
        this.tournamentRepository = tournamentRepository;
        this.matRepository = matRepository;
        this.categoryRepository = categoryRepository;
        this.clubService = clubService;
    }



    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
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
}
