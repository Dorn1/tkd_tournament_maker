package pl.tkd.tournaments.tkd_tournament_maker.tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.tkd.tournaments.tkd_tournament_maker.club.club.Club;
import pl.tkd.tournaments.tkd_tournament_maker.club.club.ClubService;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.CompetitorRepository;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.CompetitorTableDTO;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Sex;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.Referee;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.RefereeDTO;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.RefereeRepository;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.*;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.ladderCategory.*;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.tableCategory.*;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categoryFilter.CategoryFilterHandler;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categoryFilter.ICategoryFilter;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.mat.Mat;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.mat.MatDTO;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.mat.MatRepository;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament.Tournament;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament.TournamentRepository;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.ObjectNotFoundException;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.RematchNeededException;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament.dto.TournamentTableDTO;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final MatRepository matRepository;
    private final FightRepository fightRepository;
    private final RefereeRepository refereeRepository;
    private final ClubService clubService;
    private final TableDataRepository tableDataRepository;
    private final PlaceWrapperRepository placeWrapperRepository;
    private final TableCategoryRepository tableCategoryRepository;
    private final LadderCategoryRepository ladderCategoryRepository;
    private final RematchRepository rematchRepository;


    @Autowired
    public TournamentService(TournamentRepository tournamentRepository,
                             MatRepository matRepository, RefereeRepository refereeRepository,
                             ClubService clubService,
                             FightRepository fightRepository,
                             TableDataRepository tableDataRepository,
                             PlaceWrapperRepository placeWrapperRepository, TableCategoryRepository tableCategoryRepository, LadderCategoryRepository ladderCategoryRepository,
                             RematchRepository rematchRepository) {
        this.tournamentRepository = tournamentRepository;
        this.matRepository = matRepository;
        this.refereeRepository = refereeRepository;
        this.clubService = clubService;
        this.fightRepository = fightRepository;
        this.tableDataRepository = tableDataRepository;
        this.placeWrapperRepository = placeWrapperRepository;
        this.tableCategoryRepository = tableCategoryRepository;
        this.ladderCategoryRepository = ladderCategoryRepository;
        this.rematchRepository = rematchRepository;
    }


    public void addTournament(String name,
                              String location,
                              Long startDatenum,
                              Long endDatenum,
                              String organizer) throws ObjectNotFoundException {
        Club club = clubService.getClubByName(organizer);
        Date startDate = new Date(startDatenum);
        Date endDate = new Date(endDatenum);
        Tournament tournament = new Tournament(name, location, startDate, endDate, club);
        tournamentRepository.save(tournament);
    }


    public void addMat(Long tournamentId, Long mat_LeaderId) throws ObjectNotFoundException {
        Tournament tournament = getTournament(tournamentId);
        Mat mat = new Mat();
        Referee matLeader = refereeRepository.findById(mat_LeaderId).orElseThrow(() -> new ObjectNotFoundException("Referee not found"));
        mat.setTournament(tournament);
        mat.setMatLeader(matLeader);
        mat.setCategoryQueque(new ArrayList<>());
        mat.setReferees(new ArrayList<>());
        tournament.getMats().add(mat);
        matRepository.save(mat);
        tournamentRepository.save(tournament);
    }

    public List<Competitor> filterCompetitors(List<Competitor> competitors, Map<String, String> filterData) {
        return competitors;

    }

    public void addCategory(Long matId, boolean ladderCategory, Map<String, String> filterData) throws ObjectNotFoundException, IllegalAccessException {
        Mat mat = getMat(matId);
        Category category = new TableCategory();
        List<Competitor> competitors = filterCompetitors(new ArrayList<>(mat.getTournament().getCompetitors()), filterData);
        if (ladderCategory) {
            category = new LadderCategory();
            category.setCompetitors(new HashSet<>(competitors));
            generateLadderCategory((LadderCategory) category);
            ladderCategoryRepository.save((LadderCategory) category);
        } else
            tableCategoryRepository.save((TableCategory) category);
        //Category Filtering logic needed here
        if (mat.getCategoryQueque().isEmpty())
            mat.setCategoryQueque(new ArrayList<>());

        mat.getCategoryQueque().add(category.getId());
        matRepository.save(mat);
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
    tournamentRepository.save(tournament);
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
            beforeFight1 = fightRepository.save(beforeFight1);
            if (category.getCompetitors().size() - competitorFightSum <= thisLayerQueque.size() + 1) {
                beforeFight1.setNextFightObserver(generatingFight.getId());
                generatingFight.getFightsBefore().add(beforeFight1.getId());
                category.getFights().add(beforeFight1);
                competitorFightSum++;
            } else {
                Fight beforeFight2 = new Fight();
                beforeFight2 = fightRepository.save(beforeFight2);
                beforeFight1.setNextFightObserver(generatingFight.getId());
                beforeFight2.setNextFightObserver(generatingFight.getId());
                generatingFight.getFightsBefore().add(beforeFight1.getId());
                generatingFight.getFightsBefore().add(beforeFight2.getId());
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
                thisLayerQueque.getFirst().setThirdPlaceFightObserver(category.getThridPlaceFight().getId());
                thisLayerQueque.getLast().setThirdPlaceFightObserver(category.getThridPlaceFight().getId());
                category.getThridPlaceFight().getFightsBefore().add(thisLayerQueque.getFirst().getId());
                category.getThridPlaceFight().getFightsBefore().add(thisLayerQueque.getLast().getId());
                category.getFights().add(category.getThridPlaceFight());
            }
        }
        Set<Competitor> competitorsCopy = new HashSet<>(category.getCompetitors());

        for (Fight fight : category.getFights()) {
            if (competitorsCopy.isEmpty()) break;
            if (fight.getFightsBefore().isEmpty()) {
                addCompetitor(randomCompetitor(competitorsCopy), fight);
                addCompetitor(randomCompetitor(competitorsCopy), fight);
            } else if (fight.getFightsBefore().size() == 1) {
                addCompetitor(randomCompetitor(competitorsCopy), fight);
            }
        }

        fightRepository.saveAll(category.getFights());
        ladderCategoryRepository.save(category);
    }

    public List<PlaceWrapper> getTableWinners(TableCategory category) {
        List<TableData> winners = new ArrayList<>();
        for (TableData score : category.getScores()) {
            if (winners.size() < category.getWantedPlaces()) {
                winners.add(score);
            } else {
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
                    throw new RematchNeededException("Rematch!", rematchWinners);
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
            throw new RematchNeededException("Rematch needed!", rematchCompetitors);
        }

        List<PlaceWrapper> result = new ArrayList<>();
        winners = winners.stream().sorted(Comparator.comparing(TableData::getScore)).toList();
        for (int place = 0; place < winners.size(); place++) {
            PlaceWrapper placeWrapper1 = new PlaceWrapper();
            placeWrapper1.setPlace(place + 1);
            placeWrapper1.setCategory(category);
            placeWrapper1.setCompetitor(winners.get(place).getCompetitor());
            result.add(placeWrapper1);
            placeWrapperRepository.save(placeWrapper1);
        }
        return result;
    }

    public void setTableRematch(TableCategory tableCategory, List<TableData> competitors) {
        Rematch newRematch = new Rematch();
        Set<TableData> tables = new HashSet<>(competitors);
        newRematch.setTables(tables);
        tableCategory.getRematches().add(newRematch);
        rematchRepository.save(newRematch);
        tableCategoryRepository.save(tableCategory);

    }

    public void generateTableCategory(TableCategory category) {
        for (Competitor competitor : category.getCompetitors()) {
            TableData tableData = new TableData();
            tableData.setCompetitor(competitor);
            tableDataRepository.save(tableData);
            category.getScores().add(tableData);
        }
        tableCategoryRepository.save(category);
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

    public TableCategory getTableCategoryById(Long id) throws ObjectNotFoundException {
        if (tableCategoryRepository.findById(id).isPresent()) {
            return tableCategoryRepository.findById(id).get();
        }
        throw new ObjectNotFoundException("requested Table Category not found");
    }

    public void removeCompetitorFromLadderCategory(Long competitorID, Long categoryId) throws ObjectNotFoundException, IllegalAccessException {
        if (ladderCategoryRepository.findById(categoryId).isPresent()) {
            LadderCategory category = ladderCategoryRepository.findById(categoryId).get();
            for (Fight fight : category.getFights()) {
                if (fight.getCompetitor1().getId().equals(competitorID)
                        && fight.getWinner() == null) {
                    setWinner(false, fight);
                    break;
                }
                if (fight.getCompetitor2().getId().equals(competitorID)
                        && fight.getWinner() == null) {
                    setWinner(true, fight);
                    break;
                }
            }
        } else
            throw new ObjectNotFoundException("Category not found");
    }

    public LadderCategoryDTO getLadderCategoryById(Long categoryId) {
        LadderCategory category = ladderCategoryRepository.findById(categoryId).get();

        if (ladderCategoryRepository.findById(categoryId).isPresent()) {
            LadderCategoryDTO dto = new LadderCategoryDTO();
            dto.setId(category.getId());
            dto.setCompetitors(new ArrayList<>());
            for (Competitor competitor : category.getCompetitors()) {
                CompetitorTableDTO competitorTableDTO = new CompetitorTableDTO();
                competitorTableDTO.setId(competitor.getId());
                competitorTableDTO.setFirstname(competitor.getFirstName());
                competitorTableDTO.setLastname(competitor.getLastName());
                competitorTableDTO.setBelt(competitor.getBelt());
                competitorTableDTO.setClubId(competitor.getClub().getId());
                dto.getCompetitors().add(competitorTableDTO);
            }

            dto.setFirstPlaceFight(createFightDTO(category.getFirstPlaceFight(), true));
            dto.setThridPlaceFight(createFightDTO(category.getThridPlaceFight(), false));
            Mat categoryMat = matRepository.findById(category.getMatId()).orElseThrow();
            MatDTO matDTO = new MatDTO();
            matDTO.setId(categoryMat.getId());
            TournamentTableDTO tournament = new TournamentTableDTO();
            tournament.setId(categoryMat.getTournament().getId());
            tournament.setName(categoryMat.getTournament().getName());
            tournament.setLocation(categoryMat.getTournament().getLocation());
            tournament.setDate(categoryMat.getTournament().getStartDate().toString());
            matDTO.setTournament(tournament);
            if (categoryMat.getMatLeader() != null)
                matDTO.setMatLeader(createRefereeDTO(categoryMat.getMatLeader()));
            matDTO.setReferees(new ArrayList<>());
            for (Referee referee : categoryMat.getReferees()) {
                matDTO.getReferees().add(createRefereeDTO(referee));
            }
            dto.setMat(matDTO);
            return dto;
        }
        throw new RuntimeException("requested Ladder Category not found");
    }

    private RefereeDTO createRefereeDTO(Referee referee) {
        RefereeDTO dto = new RefereeDTO();
        dto.setId(referee.getId());
        dto.setFirstname(referee.getFirstName());
        dto.setLastname(referee.getLastName());
        dto.setClubId(referee.getClub().getId());
        dto.setRefereeClass(referee.getRefereeClass());
        return dto;
    }

    private FightDTO createFightDTO(Fight fight, boolean recursively) {
        FightDTO dto = new FightDTO();
        if (fight.getFightsBefore() != null &&!fight.getFightsBefore().isEmpty() && recursively) {
            List<FightDTO> fightsBefore = new ArrayList<>();
            for (Long beforeFight : fight.getFightsBefore()) {
                fightsBefore.add(createFightDTO(fightRepository.findById(beforeFight).orElseThrow(), recursively));
            }
            dto.setFightsBefore(fightsBefore);
        }

        dto.setId(fight.getId());
        dto.setMainFightReferee(createRefereeDTO(refereeRepository.findById(fight.getMainFightReferee()).orElseThrow()));

        List<RefereeDTO> fightReferees = new ArrayList<>();
        for (Long referee : fight.getFightReferees()) {
            Referee ref = refereeRepository.findById(referee).orElseThrow();
            fightReferees.add(createRefereeDTO(ref));
        }
        dto.setFightReferees(fightReferees);

        List<RefereeDTO> tableReferees = new ArrayList<>();
        for (Long referee : fight.getTableReferees()) {
            Referee ref = refereeRepository.findById(referee).orElseThrow();
            tableReferees.add(createRefereeDTO(ref));
        }
        dto.setTableReferees(tableReferees);
        if (fight.getCompetitor1() != null)
            dto.setCompetitor1(createCompetitorTableDTO(fight.getCompetitor1()));
        else
            dto.setCompetitor1(null);
        if (fight.getCompetitor2() != null)
            dto.setCompetitor2(createCompetitorTableDTO(fight.getCompetitor2()));
        else
            dto.setCompetitor2(null);
        if (fight.getWinner() != null)
            dto.setWinner(createCompetitorTableDTO(fight.getWinner()));
        else
            dto.setWinner(null);
        return dto;
    }

    CompetitorTableDTO createCompetitorTableDTO(Competitor competitor) {
        CompetitorTableDTO dto = new CompetitorTableDTO();
        dto.setId(competitor.getId());
        dto.setFirstname(competitor.getFirstName());
        dto.setLastname(competitor.getLastName());
        dto.setBelt(competitor.getBelt());
        dto.setClubId(competitor.getClub().getId());
        return dto;
    }

    public void addRefereeToMat(Long refereeId, Long matId) {
        try {
            Mat mat = getMat(matId);
            Referee referee = refereeRepository.findById(refereeId).orElseThrow(() -> new ObjectNotFoundException("Referee not found"));
            if (mat.getReferees() == null) {
                mat.setReferees(new ArrayList<>());
            }
            if (!mat.getReferees().contains(referee)) {
                mat.getReferees().add(referee);
                matRepository.save(mat);
            }
        } catch (ObjectNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Competitor getLoser(Fight fight) {
        if (fight.getWinner().equals(fight.getCompetitor1())) {
            return fight.getCompetitor2();
        }
        return fight.getCompetitor1();
    }

    public void updateObservers(Fight fight) throws IllegalAccessException {
        Fight nextFightObserver = fightRepository.findById(fight.getNextFightObserver()).orElseThrow();
        addCompetitor(fight.getWinner(), fight);
        Fight thirdPlaceFightObserver = fightRepository.findById(fight.getThirdPlaceFightObserver()).orElseThrow();
        if (thirdPlaceFightObserver.getCompetitor1() == null) {
            thirdPlaceFightObserver.setCompetitor1(fight.getCompetitor1());
        } else if (thirdPlaceFightObserver.getCompetitor2() == null) {
            thirdPlaceFightObserver.setCompetitor2(fight.getCompetitor2());
        }
    }

    public void setWinner(boolean wonFirst, Fight fight) throws ObjectNotFoundException, IllegalAccessException {
        if (fight.getCompetitor1() != null && fight.getCompetitor2() != null) {
            fight.setWinner(wonFirst ? fight.getCompetitor1() : fight.getCompetitor2());
        } else if (fight.getCompetitor1() != null) {
            fight.setWinner(fight.getCompetitor1());
        } else if (fight.getCompetitor2() != null) {
            fight.setWinner(fight.getCompetitor2());
        } else {
            throw new ObjectNotFoundException("neither competitor1 nor competitor2 is set");
        }
        updateObservers(fight);
    }

    public void addCompetitor(Competitor competitor, Fight fight) throws IllegalAccessException {
        if (fight.getCompetitor1() == null) {
            fight.setCompetitor1(competitor);
        } else if (fight.getCompetitor2() == null) {
            fight.setCompetitor2(competitor);
        } else {
            throw new IllegalAccessException("added too many competitors to fight");
        }
    }
}
