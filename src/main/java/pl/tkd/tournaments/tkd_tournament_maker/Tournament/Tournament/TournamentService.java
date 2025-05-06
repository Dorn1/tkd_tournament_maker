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

import java.util.*;

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

    public void generateLadderCategory(LadderCategory category) throws IllegalAccessException {
        if (category.getFirstPlaceFight() != null) {
            throw new IllegalAccessException("Category already has generated ladder");
        }
        category.setFirstPlaceFight(new Fight());
        category.getFights().add(category.getFirstPlaceFight());
        int competitorFightSum = 2;
        List<Fight> thisLayerQueque = new ArrayList<>();
        thisLayerQueque.add(category.getFirstPlaceFight());
        List<Fight> nextLayerQueque = new ArrayList<>();
        boolean thirdPlaceFightSet = false;
        while (competitorFightSum < category.getCompetitors().size()) {
            if (!thirdPlaceFightSet && thisLayerQueque.size() ==2) {
                thirdPlaceFightSet = true;
                category.setThridPlaceFight(new Fight());
                category.getThridPlaceFight().getFightsBefore().add(thisLayerQueque.getFirst());
                category.getThridPlaceFight().getFightsBefore().add(thisLayerQueque.getLast());
                category.getFights().add(category.getThridPlaceFight());
            }
            Fight generatingFight = thisLayerQueque.removeFirst();
            if (competitorFightSum == category.getCompetitors().size()-1) {
                Fight beforeFight1 = new Fight();
                generatingFight.getFightsBefore().add(beforeFight1);
                break;
            }
            Fight beforeFight1 = new Fight();
            Fight beforeFight2 = new Fight();
            generatingFight.getFightsBefore().add(beforeFight1);
            generatingFight.getFightsBefore().add(beforeFight2);
            category.getFights().add(beforeFight1);
            category.getFights().add(beforeFight2);
            nextLayerQueque.add(beforeFight1);
            nextLayerQueque.add(beforeFight2);
            if(thisLayerQueque.isEmpty()){
                thisLayerQueque = evenQueque(nextLayerQueque);
                nextLayerQueque = new ArrayList<>();
            }
            competitorFightSum +=2;
        }
        Set<Competitor> competitorsCopy = new HashSet<>(category.getCompetitors());
//        Stack<Fight> fightStack = new Stack<>();
//        fightStack.add(category.getFirstPlaceFight());
//
//        while (!competitorsCopy.isEmpty()){
//            Fight checkedFight = fightStack.pop();
//            if (checkedFight.getFightsBefore().isEmpty()){
//                checkedFight.addCompetitor(randomCompetitor(competitorsCopy));
//                checkedFight.addCompetitor(randomCompetitor(competitorsCopy));
//            } else if (checkedFight.getFightsBefore().size() == 1) {
//                checkedFight.addCompetitor(randomCompetitor(competitorsCopy));
//            }
//            if (!checkedFight.getFightsBefore().isEmpty()) {
//                fightStack.addAll(checkedFight.getFightsBefore());
//            }
//        }
        for (Fight fight : category.getFights().stream().sorted(Comparator.comparing(Fight::getId)).toList().reversed()) {
            if (competitorsCopy.isEmpty()) break;
            if (fight.getFightsBefore().isEmpty()){
                fight.addCompetitor(randomCompetitor(competitorsCopy));
                fight.addCompetitor(randomCompetitor(competitorsCopy));
            } else if (fight.getFightsBefore().size() == 1) {
                fight.addCompetitor(randomCompetitor(competitorsCopy));
            }
        }

        fightRepository.saveAll(category.getFights());
        categoryRepository.save(category);
    }

    public List<Fight> evenQueque(List<Fight> thisLayerQueque) throws IllegalAccessException {
        if (thisLayerQueque == null) {throw new IllegalAccessException("null Fight Queque provided");}
        if (thisLayerQueque.size() <=4){
            List<Fight> quartet = new ArrayList<>();
            quartet.add(thisLayerQueque.getFirst());
            if (thisLayerQueque.size() >=3) {
                quartet.add(thisLayerQueque.get(2));
            }
            if (thisLayerQueque.size() >=2) {
                quartet.add(thisLayerQueque.get(1));
            }
            if (thisLayerQueque.size() ==4) {
                quartet.add(thisLayerQueque.get(3));
            }
            return quartet;
        }
        List<Fight> byTwo1 = new ArrayList<>();
        List<Fight> byTwo2 = new ArrayList<>();
        for (int i = 0; i < thisLayerQueque.size(); i++) {
            if (i%2 == 0) {
                byTwo1.add(thisLayerQueque.get(i));
            }else{
                byTwo2.add(thisLayerQueque.get(i));
            }
        }
        byTwo1 = evenQueque(byTwo1);
        byTwo2 = evenQueque(byTwo2);
        List<Fight> evenQueque = new ArrayList<>();
        evenQueque.addAll(byTwo1);
        evenQueque.addAll(byTwo2);
        return evenQueque;

    }

    public Competitor randomCompetitor(Set<Competitor> competitorSet) {
        int random = new Random().nextInt(competitorSet.size());
        Competitor chosen = null;
        int i = 0;
        for (Competitor competitor : competitorSet) {
            if (i == random) {
                chosen = competitor;
                competitorSet.remove(competitor);
                break;
            }
        }
        return chosen;
    }
}
