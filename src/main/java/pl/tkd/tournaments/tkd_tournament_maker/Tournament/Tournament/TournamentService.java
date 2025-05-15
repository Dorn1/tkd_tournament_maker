package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Club.Club;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Club.ClubService;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Sex;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories.*;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories.LadderCategory.Fight;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories.LadderCategory.FightRepository;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories.LadderCategory.LadderCategory;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories.TableCategory.*;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterHandler;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.ICategoryFilter;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Mat.Mat;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Mat.MatRepository;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.ObjectNotFoundException;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.RematchNeededException;

import java.util.*;

@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final MatRepository matRepository;
    private final CategoryRepository categoryRepository;
    private final FightRepository fightRepository;
    private final ClubService clubService;
    private final TableDataRepository tableDataRepository;
    private final PlaceWrapperRepository placeWrapperRepository;



    @Autowired
    public TournamentService(TournamentRepository tournamentRepository,
                             MatRepository matRepository,
                             CategoryRepository categoryRepository,
                             ClubService clubService,
                             FightRepository fightRepository,
                             TableDataRepository tableDataRepository,
                             PlaceWrapperRepository placeWrapperRepository
                             ) {
        this.tournamentRepository = tournamentRepository;
        this.matRepository = matRepository;
        this.categoryRepository = categoryRepository;
        this.clubService = clubService;
        this.fightRepository = fightRepository;
        this.tableDataRepository = tableDataRepository;
        this.placeWrapperRepository = placeWrapperRepository;
    }


    public void addTournament(String name,
                              String location,
                              Long startDatenum,
                              Long endDatenum,
                              Long organizerId) throws ObjectNotFoundException {
        Date startDate = new Date(startDatenum);
        Date endDate = new Date(endDatenum);
        Club c = clubService.getClubById(organizerId);
        Tournament t = new Tournament(name, location, startDate, endDate, c);
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

    public void addCategory(Long matId, boolean ladderCategory) throws ObjectNotFoundException {
        Mat mat = getMat(matId);
        Category category = new TableCategory();
        if (ladderCategory)
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
        int maxtwopowered = 1;
        while (maxtwopowered < category.getCompetitors().size()) {
            maxtwopowered *= 2;
        }
        int layerFightCount = 1;
        category.setFirstPlaceFight(new Fight());
        category.getFights().add(category.getFirstPlaceFight());
        int competitorFightSum = 2;
        List<Fight> thisLayerQueque = new ArrayList<>();
        thisLayerQueque.add(category.getFirstPlaceFight());
        List<Fight> nextLayerQueque = new ArrayList<>();
        boolean thirdPlaceFightSet = false;
        while (competitorFightSum < category.getCompetitors().size()) {
            Fight generatingFight = thisLayerQueque.removeFirst();
            Fight beforeFight1 = new Fight();
            if (category.getCompetitors().size() - competitorFightSum <= thisLayerQueque.size() + 1) {
                beforeFight1.setNextFightObserver(generatingFight);
                generatingFight.getFightsBefore().add(beforeFight1);
                category.getFights().add(beforeFight1);
                competitorFightSum++;
            } else {
                Fight beforeFight2 = new Fight();
                beforeFight1.setNextFightObserver(generatingFight);
                beforeFight2.setNextFightObserver(generatingFight);
                generatingFight.getFightsBefore().add(beforeFight1);
                generatingFight.getFightsBefore().add(beforeFight2);
                category.getFights().add(beforeFight1);
                category.getFights().add(beforeFight2);
                nextLayerQueque.add(beforeFight1);
                nextLayerQueque.add(beforeFight2);
                if (thisLayerQueque.isEmpty()) {
                    if (category.getCompetitors().size() != maxtwopowered &&
                            layerFightCount * 4 >= maxtwopowered / 2)
                        thisLayerQueque = evenQueque(nextLayerQueque);
                    else
                        thisLayerQueque = nextLayerQueque;
                    nextLayerQueque = new ArrayList<>();
                    layerFightCount = thisLayerQueque.size();
                }
                competitorFightSum += 2;
            }
            if (!thirdPlaceFightSet && thisLayerQueque.size() == 2) {
                thirdPlaceFightSet = true;
                category.setThridPlaceFight(new Fight());
                thisLayerQueque.getFirst().setThirdPlaceFightObserver(category.getThridPlaceFight());
                thisLayerQueque.getLast().setThirdPlaceFightObserver(category.getThridPlaceFight());
                category.getThridPlaceFight().getFightsBefore().add(thisLayerQueque.getFirst());
                category.getThridPlaceFight().getFightsBefore().add(thisLayerQueque.getLast());
                category.getFights().add(category.getThridPlaceFight());
            }
        }
        Set<Competitor> competitorsCopy = new HashSet<>(category.getCompetitors());

        for (Fight fight : category.getFights()) {
            if (competitorsCopy.isEmpty()) break;
            if (fight.getFightsBefore().isEmpty()) {
                fight.addCompetitor(randomCompetitor(competitorsCopy));
                fight.addCompetitor(randomCompetitor(competitorsCopy));
            } else if (fight.getFightsBefore().size() == 1) {
                fight.addCompetitor(randomCompetitor(competitorsCopy));
            }
        }

        fightRepository.saveAll(category.getFights());
        categoryRepository.save(category);
    }

    public List<PlaceWrapper> getTableWinners(TableCategory category){
        List<TableData> winners = new ArrayList<>();
        for(TableData score : category.getScores()){
            if (winners.size() < category.getWantedPlaces()){
                winners.add(score);
            }
            else {
                for (TableData winscore : winners) {
                    if (score.getScore() > winscore.getScore()) {
                        winners.remove(winscore);
                        winners.add(score);
                        break;
                    }
                }
            }
        }

        for (TableData winner : winners) {
            List<TableData> copy = new ArrayList<>(winners);
            copy.remove(winner);
            for (TableData winner2 : copy) {
                if (Objects.equals(winner.getScore(), winner2.getScore())) {
                    List<TableData> rematchWinners = new ArrayList<>(2);
                    winners.add(winner);
                    winners.add(winner2);
                    throw new RematchNeededException("Rematch!",rematchWinners);
                }
            }
        }

        List<TableData> allCopy = new ArrayList<>(category.getScores());
        allCopy.removeAll(winners);
        TableData lastPlace = allCopy.getFirst();
        for (TableData winner : winners) {
            if (lastPlace.getScore() > winner.getScore()) {
                lastPlace = winner;
            }
        }
        List<TableData> rematchCompetitors = new LinkedList<>();
        for (TableData score : allCopy) {
            if (Objects.equals(score.getScore(), lastPlace.getScore())) {
                if (!rematchCompetitors.contains(lastPlace))
                    rematchCompetitors.add(lastPlace);
                rematchCompetitors.add(score);
            }
        }

        if (!rematchCompetitors.isEmpty()) {
            throw new RematchNeededException("Rematch for last win place!",rematchCompetitors);
        }

        List<PlaceWrapper> result = new ArrayList<>();
        winners = winners.stream().sorted(Comparator.comparing(TableData::getScore)).toList();
        for (int place = 0; place< winners.size(); place++){
            PlaceWrapper placeWrapper1 = new PlaceWrapper();
            placeWrapper1.setPlace(place+1);
            placeWrapper1.setCategory(category);
            placeWrapper1.setCompetitor(winners.get(place).getCompetitor());
            result.add(placeWrapper1);
            placeWrapperRepository.save(placeWrapper1);
        }
        return result;
    }

    public void generateTableCategory(TableCategory category) {
        for (Competitor competitor : category.getCompetitors()){
            TableData tableData = new TableData();
            tableData.setCompetitor(competitor);
            tableDataRepository.save(tableData);
            category.getScores().add(tableData);
        }
    }

    public Set<Competitor> filter_competitors(ICategoryFilter filter, Set<Competitor> allCompetitors) {
        return filter.filter(allCompetitors);
    }

    public ICategoryFilter getFilter(Long minAge, Long maxAge, Sex sex, Integer minBelt, Integer maxBelt, Double minWeight, Double maxWeight) {
        CategoryFilterHandler handler = new CategoryFilterHandler();
        if (minAge != null)
            handler.addminAge(minAge);
        if (maxAge != null)
            handler.addmaxAge(maxAge);
        if (sex != null)
            handler.setSex(sex);
        if (minBelt != null)
            handler.addminBelt(minBelt);
        if (maxBelt != null)
            handler.addmaxBelt(maxBelt);
        if (minWeight != null)
            handler.addminWeight(minWeight);
        if (maxWeight != null)
            handler.addmaxWeight(maxWeight);
        return handler.build();
    }

    public List<Fight> evenQueque(List<Fight> thisLayerQueque) throws IllegalAccessException {
        if (thisLayerQueque == null) {
            throw new IllegalAccessException("null Fight Queque provided");
        }
        if (thisLayerQueque.size() <= 2) {
            return thisLayerQueque;
        }
        List<Fight> byTwo1 = new ArrayList<>();
        List<Fight> byTwo2 = new ArrayList<>();
        for (int i = 0; i < thisLayerQueque.size(); i++) {
            if (i % 2 == 0) {
                byTwo1.add(thisLayerQueque.get(i));
            } else {
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
            i++;
        }
        return chosen;
    }
}
