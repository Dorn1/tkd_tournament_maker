package pl.tkd.tournaments.tkd_tournament_maker.tournament;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.tkd.tournaments.tkd_tournament_maker.club.club.Club;
import pl.tkd.tournaments.tkd_tournament_maker.club.club.ClubDTO;
import pl.tkd.tournaments.tkd_tournament_maker.club.club.ClubService;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.CompetitorRepository;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.CompetitorTableDTO;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Sex;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.Referee;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.RefereeClass;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.RefereeDTO;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.RefereeRepository;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.RematchNeededException;
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
import pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament.dto.TournamentStatisticsDTO;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament.dto.TournamentTableDTO;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.*;

@Slf4j
@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final MatRepository matRepository;
    private final FightRepository fightRepository;
    private final RefereeRepository refereeRepository;
    private final ClubService clubService;
    private final TableDataRepository tableDataRepository;
    private final TableCategoryRepository tableCategoryRepository;
    private final LadderCategoryRepository ladderCategoryRepository;
    private final CompetitorRepository competitorRepository;
    private static final Logger logger = LoggerFactory.getLogger(TournamentService.class);


    @Autowired
    public TournamentService(TournamentRepository tournamentRepository,
                             MatRepository matRepository, RefereeRepository refereeRepository,
                             ClubService clubService,
                             FightRepository fightRepository,
                             TableDataRepository tableDataRepository, TableCategoryRepository tableCategoryRepository, LadderCategoryRepository ladderCategoryRepository,
                             CompetitorRepository competitorRepository) {
        this.tournamentRepository = tournamentRepository;
        this.matRepository = matRepository;
        this.refereeRepository = refereeRepository;
        this.clubService = clubService;
        this.fightRepository = fightRepository;
        this.tableDataRepository = tableDataRepository;
        this.tableCategoryRepository = tableCategoryRepository;
        this.ladderCategoryRepository = ladderCategoryRepository;
        this.competitorRepository = competitorRepository;
    }


    public void addTournament(String name,
                              String location,
                              Long startDatenum,
                              Long endDatenum,
                              String organizer) throws ObjectNotFoundException {
        Club club = clubService.getClubByName(organizer);
        Date startDate = new Date(startDatenum);
        Date endDate = new Date(endDatenum + 24L * 60 * 60 * 1000);
        Tournament tournament = new Tournament(name, location, startDate, endDate, club);
        tournamentRepository.save(tournament);
    }


    public void addMat(Long tournamentId) throws ObjectNotFoundException {
        Tournament tournament = getTournament(tournamentId);
        Long max = 0L;
        for (Mat mat : tournament.getMats()) {
            if (mat.getNumber() > max)
                max = mat.getNumber();

        }
        Mat mat = new Mat();
        mat.setTournament(tournament);
        mat.setCategoryQueque(new ArrayList<>());
        mat.setReferees(new ArrayList<>());
        mat.setNumber(max + 1);
        tournament.getMats().add(mat);
        matRepository.save(mat);
        tournamentRepository.save(tournament);
    }

    public List<Competitor> filterCompetitors(List<Competitor> competitors, Map<String, String> filterData) {
        CategoryFilterHandler categoryFilterHandler = new CategoryFilterHandler();
        for (Map.Entry<String, String> entry : filterData.entrySet()) {
            switch (entry.getKey()) {
                case "max-weight":
                    categoryFilterHandler.addmaxWeight(Double.valueOf(entry.getValue()));
                    break;
                case "min-weight":
                    categoryFilterHandler.addminWeight(Double.valueOf(entry.getValue()));
                    break;
                case "max-age":
                    categoryFilterHandler.addmaxAge(Long.valueOf(entry.getValue()));
                    break;
                case "min-age":
                    categoryFilterHandler.addminAge(Long.valueOf(entry.getValue()));
                    break;
                case "max-degree":
                    categoryFilterHandler.addmaxBelt(Integer.valueOf(entry.getValue()));
                    break;
                case "min-degree":
                    categoryFilterHandler.addminBelt(Integer.valueOf(entry.getValue()));
                    break;
                case "sex":
                    categoryFilterHandler.setSex(Sex.valueOf(entry.getValue()));
            }
        }
        Set<Competitor> filteredCompetitors = categoryFilterHandler.build().filter(new HashSet<>(competitors));
        return new ArrayList<>(filteredCompetitors);
    }

    public void addCategory(String categoryName, Long tournamentId, String categoryType, Map<String, String> filterData) throws ObjectNotFoundException, IllegalAccessException {
        Tournament tournament = getTournament(tournamentId);
        Category category = new TableCategory();
        category.setGenerated(false);
        List<Competitor> competitors = filterCompetitors(tournament.getCompetitors().stream().toList(), filterData);
        switch (categoryType) {
            case "ladder":
                category = new LadderCategory();
                category.setName(categoryName);
                category.setClassified(new HashSet<>(competitors));
                category.setTournamentId(tournamentId);
                category = ladderCategoryRepository.save((LadderCategory) category);
                break;
            case "table":
                category.setTournamentId(tournamentId);
                category.setName(categoryName);
                category.setClassified(new HashSet<>(competitors));
                category = tableCategoryRepository.save((TableCategory) category);
                break;
            default:
                throw new ObjectNotFoundException("Incorrect Category Type");
        }
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
        if (!tournament.getCompetitors().contains(competitor)) {
            tournament.getCompetitors().add(competitor);
            tournamentRepository.save(tournament);
        }
    }

    public TournamentTableDTO getTournamentDTO(Tournament tournament) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        TournamentTableDTO dto = new TournamentTableDTO();
        dto.setId(tournament.getId());
        dto.setName(tournament.getName());
        dto.setDate(format.format(tournament.getStartDate()));
        dto.setEndDate(format.format(tournament.getEndDate()));
        dto.setLocation(tournament.getLocation());
        return dto;
    }

    public void generateLadderCategory(LadderCategory category) throws IllegalAccessException {
        if (category.getFirstPlaceFight() != null) {
            throw new IllegalAccessException("Category already has generated ladder");
        }
        if (category.getCompetitors().size() < 2) {
            throw new IllegalAccessException("category need to have at least 2 competitors");
        }
        int maxtwopowered = 1;
        while (maxtwopowered < category.getCompetitors().size()) {
            maxtwopowered *= 2;
        }
        int layerFightCount = 1;
        Fight firstPlaceFight = new Fight();
        firstPlaceFight = fightRepository.save(firstPlaceFight);
        firstPlaceFight.setCategoryId(category.getId());
        firstPlaceFight = fightRepository.save(firstPlaceFight);
        category.setFirstPlaceFight(firstPlaceFight);
        category.getFights().add(category.getFirstPlaceFight());
        int competitorFightSum = 2;
        List<Fight> thisLayerQueque = new ArrayList<>();
        thisLayerQueque.add(category.getFirstPlaceFight());
        List<Fight> nextLayerQueque = new ArrayList<>();
        while (competitorFightSum < category.getCompetitors().size()) {
            Fight generatingFight = thisLayerQueque.removeFirst();
            Fight beforeFight1 = new Fight();
            beforeFight1 = fightRepository.save(beforeFight1);
            beforeFight1.setCategoryId(category.getId());
            beforeFight1 = fightRepository.save(beforeFight1);
            if (category.getCompetitors().size() - competitorFightSum <= thisLayerQueque.size() + 1) {
                beforeFight1.setNextFightObserver(generatingFight.getId());
                generatingFight.getFightsBefore().add(beforeFight1.getId());
                category.getFights().add(beforeFight1);
                competitorFightSum++;
            } else {
                Fight beforeFight2 = new Fight();
                beforeFight2 = fightRepository.save(beforeFight2);
                beforeFight2.setCategoryId(category.getId());
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
        if (firstPlaceFight.getFightsBefore().size() == 2) {
            Fight thirdPlaceFight = new Fight();
            thirdPlaceFight = fightRepository.save(thirdPlaceFight);
            thirdPlaceFight.setCategoryId(category.getId());
            thirdPlaceFight = fightRepository.save(thirdPlaceFight);
            category.setThridPlaceFight(thirdPlaceFight);
            Fight fight1 = fightRepository.findById(firstPlaceFight.getFightsBefore().stream().toList().getFirst()).orElseThrow();
            fight1.setThirdPlaceFightObserver(category.getThridPlaceFight().getId());
            Fight fight2 = fightRepository.findById(firstPlaceFight.getFightsBefore().stream().toList().getLast()).orElseThrow();
            fight2.setThirdPlaceFightObserver(category.getThridPlaceFight().getId());
            category.getFights().add(category.getThridPlaceFight());
            fightRepository.save(fight1);
            fightRepository.save(fight2);
        }
        fightRepository.saveAll(category.getFights());
        category.setGenerated(true);
        ladderCategoryRepository.save(category);
    }

    public void generateTableCategory(TableCategory category) {
        for (Competitor competitor : category.getCompetitors()) {
            TableData tableData = new TableData();
            tableData.setChecked(false);
            tableData.setCompetitor(competitor);
            tableDataRepository.save(tableData);
            category.getScores().add(tableData);
        }
        category.setGenerated(true);
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Competitor competitor = competitorRepository.findById(competitorID).orElseThrow();

        if (!Objects.equals(competitor.getClub().getUsername(), currentPrincipalName)) {
            logger.info("Attempt to remove competitor from another club");
            throw new IllegalAccessException("Attempt to remove competitor from another club");
        }

        if (ladderCategoryRepository.findById(categoryId).isPresent()) {
            LadderCategory category = ladderCategoryRepository.findById(categoryId).get();
            category.getRemoved().add(competitor);
            ladderCategoryRepository.save(category);
        } else
            throw new ObjectNotFoundException("Category not found");
    }

    public LadderCategoryDTO getLadderCategoryById(Long categoryId) throws ObjectNotFoundException, IllegalAccessException {
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
            Mat mat = matRepository.findById(category.getMatId()).orElseThrow();

            dto.setFirstPlaceFight(createFightDTO(category.getFirstPlaceFight(), true));
            try {
                dto.setThridPlaceFight(createFightDTO(category.getThridPlaceFight(), false));
            } catch (Exception ignored) {
            }
            Mat categoryMat = matRepository.findById(category.getMatId()).orElseThrow();

            TournamentTableDTO tournament = createTournamentTableDTO(categoryMat.getTournament());
            MatDTO matDTO = createMatDTO(categoryMat, tournament);
            dto.setMat(matDTO);
            try {
                dto.setFirstPlace(createCompetitorTableDTO(category.getFirstPlace()));
            } catch (Exception ignored) {
            }
            try {
                dto.setSecondPlace(createCompetitorTableDTO(category.getSecondPlace()));
            } catch (Exception ignored) {
            }
            try {
                dto.setThirdPlace(createCompetitorTableDTO(category.getThirdPlace()));
            } catch (Exception ignored) {
            }


            return dto;
        }
        throw new RuntimeException("requested Ladder Category not found");
    }

    private static TournamentTableDTO createTournamentTableDTO(Tournament categoryMat) {
        TournamentTableDTO tournament = new TournamentTableDTO();
        tournament.setId(categoryMat.getId());
        tournament.setName(categoryMat.getName());
        tournament.setLocation(categoryMat.getLocation());
        tournament.setDate(categoryMat.getStartDate().toString());
        return tournament;
    }

    private MatDTO createMatDTO(Mat categoryMat, TournamentTableDTO tournament) {
        MatDTO matDTO = new MatDTO();
        matDTO.setId(categoryMat.getId());
        matDTO.setNumber(categoryMat.getNumber());
        matDTO.setTournament(tournament);
        if (categoryMat.getMatLeader() != null)
            matDTO.setMatLeader(createRefereeDTO(categoryMat.getMatLeader()));
        matDTO.setReferees(new ArrayList<>());
        for (Referee referee : categoryMat.getReferees()) {
            matDTO.getReferees().add(createRefereeDTO(referee));
        }
        matDTO.setCategoryQueque(categoryMat.getCategoryQueque());
        return matDTO;
    }

    private List<FightDTO> addOneWithChildren(Fight fight) throws ObjectNotFoundException, IllegalAccessException {
        List<FightDTO> fights = new ArrayList<>();
        if (!fight.getFightsBefore().isEmpty()) {
            for (Long beforeFight : fight.getFightsBefore()) {
                Fight fight1 = fightRepository.findById(beforeFight).orElseThrow();
                fights.add(createFightDTO(fight1, false));
            }
            for (Long beforeFight : fight.getFightsBefore()) {
                fights.addAll(addOneWithChildren(fightRepository.findById(beforeFight).orElseThrow()));
            }
        }
        return fights;
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

    private FightDTO createFightDTO(Fight fight, boolean recursively) throws ObjectNotFoundException, IllegalAccessException {
        FightDTO dto = new FightDTO();
        Long fightId = fight.getId();
        Long categoryId = fight.getCategoryId();
        Category category = null;
        category = getCategory(categoryId);
        try {
            if (category.getRemoved().contains(fight.getCompetitor1()) && fight.getWinner() == null) {
                setFightWinner(false, fightId);
            }
        } catch (Exception ignored) {
        }
        try {
            if (category.getRemoved().contains(fight.getCompetitor2()) && fight.getWinner() == null) {
                setFightWinner(true, fightId);
            }
        } catch (Exception ignored) {
        }

        if (!fight.getFightsBefore().isEmpty() && recursively) {
            List<FightDTO> fightsBefore = new ArrayList<>();
            for (Long beforeFight : fight.getFightsBefore()) {
                fightsBefore.add(createFightDTO(fightRepository.findById(beforeFight).orElseThrow(), recursively));
            }
            dto.setFightsBefore(fightsBefore);
        }

        dto.setId(fight.getId());
        dto.setMainFightReferee(createRefereeDTO(refereeRepository.findById(fight.getMainFightReferee()).orElseThrow()));


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
        dto.setWeight(competitor.getWeight());
        Long year = (long) Year.now().getValue();
        dto.setAge((int) (year-competitor.getBirthYear()));
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
        addCompetitor(fight.getWinner(), nextFightObserver);
        if (fight.getThirdPlaceFightObserver() != null) {
            Fight thirdPlaceFightObserver = fightRepository.findById(fight.getThirdPlaceFightObserver()).orElseThrow();
            if (fight.getWinner() == fight.getCompetitor1()) {
                addCompetitor(fight.getCompetitor2(), thirdPlaceFightObserver);
            } else {
                addCompetitor(fight.getCompetitor1(), thirdPlaceFightObserver);
            }

            fightRepository.save(thirdPlaceFightObserver);
        }
    }

    public void setFightWinner(boolean wonFirst, Long fightId) throws ObjectNotFoundException, IllegalAccessException {
        Fight fight = fightRepository.findById(fightId).orElseThrow(() -> new ObjectNotFoundException("Fight not found"));


        if (fight.getCompetitor1() != null && fight.getCompetitor2() != null) {
            fight.setWinner(wonFirst ? fight.getCompetitor1() : fight.getCompetitor2());
        } else if (fight.getCompetitor1() != null) {
            fight.setWinner(fight.getCompetitor1());
        } else if (fight.getCompetitor2() != null) {
            fight.setWinner(fight.getCompetitor2());
        } else {
            throw new ObjectNotFoundException("neither competitor1 nor competitor2 is set");
        }
        if (fight.getNextFightObserver() != null) {
            updateObservers(fight);
        }
        fightRepository.save(fight);
        LadderCategory ladderCategory = ladderCategoryRepository.findById(fight.getCategoryId()).orElseThrow();
        if (fight.getId().equals(ladderCategory.getFirstPlaceFight().getId())){
            ladderCategory.setFirstPlace(fight.getWinner());
            ladderCategory = ladderCategoryRepository.save(ladderCategory);
            if (fight.getWinner().getId().equals(fight.getCompetitor1().getId())){
                ladderCategory.setSecondPlace(fight.getCompetitor2());
                ladderCategory = ladderCategoryRepository.save(ladderCategory);
            }
            else {
                ladderCategory.setSecondPlace(fight.getCompetitor1());
                ladderCategory = ladderCategoryRepository.save(ladderCategory);
            }
            ladderCategory.setThirdPlace(ladderCategory.getThridPlaceFight().getWinner());
            ladderCategory = ladderCategoryRepository.save(ladderCategory);
        }
    }

    public void addCompetitor(Competitor competitor, Fight fight) throws IllegalAccessException {
        if (fight.getCompetitor2() == null) {
            fight.setCompetitor2(competitor);
        } else if (fight.getCompetitor1() == null) {
            fight.setCompetitor1(competitor);
        } else {
            throw new IllegalAccessException("added too many competitors to fight");
        }
        fightRepository.save(fight);
    }

    public FightDTO getFightDTOById(Long fightId) throws ObjectNotFoundException, IllegalAccessException {
        Fight fight = fightRepository.findById(fightId).orElseThrow();
        return createFightDTO(fight, true);
    }

    private void saveCategory(Category category) throws ObjectNotFoundException {
        if (category instanceof TableCategory) {
            tableCategoryRepository.save((TableCategory) category);
        } else if (category instanceof LadderCategory) {
            ladderCategoryRepository.save((LadderCategory) category);
        } else {
            throw new ObjectNotFoundException("Category of fight not found");
        }
    }

    private Category getCategory(Long categoryId) throws ObjectNotFoundException {
        Category category;
        try {
            category = ladderCategoryRepository.findById(categoryId).orElseThrow();
        } catch (Exception e) {
            try {
                category = tableCategoryRepository.findById(categoryId).orElseThrow();
            } catch (Exception ex) {
                throw new ObjectNotFoundException("Category of fight not found");
            }
        }
        return category;
    }

    public List<MatDTO> getMatsByTournamentId(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        List<Mat> mats = matRepository.findByTournament(tournament);
        List<MatDTO> matDTOS = new ArrayList<>();
        for (Mat mat : mats) {
            TournamentTableDTO tournamentDTO = createTournamentTableDTO(tournament);
            MatDTO dto = createMatDTO(mat, tournamentDTO);
            matDTOS.add(dto);
        }
        return matDTOS;
    }

    public String getCategoryNameById(Long categoryId) {
        if (tableCategoryRepository.findById(categoryId).isPresent()) {
            TableCategory category = tableCategoryRepository.findById(categoryId).get();
            if (category.getName() != null && !category.getName().isEmpty()) {
                return tableCategoryRepository.findById(categoryId).get().getName();
            } else {
                return "";
            }
        } else if (ladderCategoryRepository.findById(categoryId).isPresent()) {
            LadderCategory category = ladderCategoryRepository.findById(categoryId).get();
            if (category.getName() != null && !category.getName().isEmpty()) {
                return ladderCategoryRepository.findById(categoryId).get().getName();
            } else {
                return "";
            }
        } else {
            throw new RuntimeException("Category not found");
        }
    }

    public MatDTO getMatDTO(Long matId) {
        Mat mat = matRepository.findById(matId).orElseThrow();
        return createMatDTO(mat, createTournamentTableDTO(mat.getTournament()));
    }

    public List<RefereeDTO> getLeaders(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        List<RefereeDTO> refereeDtos = new LinkedList<>();
        for (Referee referee : tournament.getReferees()) {
            if (referee.getRefereeClass().equals(RefereeClass.MAT_LEADER))
                refereeDtos.add(createRefereeDTO(referee));
        }
        return refereeDtos;
    }

    public void setMatLeader(Long matId, Long leaderId) {
        Referee leader = refereeRepository.findById(leaderId).orElseThrow();
        Mat mat = matRepository.findById(matId).orElseThrow();
        mat.setMatLeader(leader);
        for (Long categoryId : mat.getCategoryQueque()) {
            try {
                LadderCategory ladderCategory = ladderCategoryRepository.findById(categoryId).orElseThrow();
                ensureMainReferee(ladderCategory.getFirstPlaceFight(), leaderId);
                ensureMainReferee(ladderCategory.getThridPlaceFight(), leaderId);
            } catch (Exception e) {
                try {
                    TableCategory tableCategory = tableCategoryRepository.findById(categoryId).orElseThrow();
                } catch (Exception ex) {
                }
            }
        }

        matRepository.save(mat);
    }

    private void ensureMainReferee(Fight fight, Long refereeId) {
        try {
            Long referee = fight.getMainFightReferee();
            if (referee == null)
                throw new Exception();
        } catch (Exception e) {
            fight.setMainFightReferee(refereeId);
            fightRepository.save(fight);
        }
        for (Long fight1 : fight.getFightsBefore()) {
            Fight f = fightRepository.findById(fight1).orElseThrow();
            ensureMainReferee(f, refereeId);
        }

    }

    public void removeRefereeFromMat(Long refereeId, Long matId) {
        Referee referee = refereeRepository.findById(refereeId).orElseThrow();
        Mat mat = matRepository.findById(matId).orElseThrow();
        mat.getReferees().remove(referee);
        matRepository.save(mat);
    }

    public List<RefereeDTO> getReferees(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        List<RefereeDTO> refereeDtos = new LinkedList<>();
        for (Referee referee : tournament.getReferees()) {
            refereeDtos.add(createRefereeDTO(referee));
        }
        return refereeDtos;
    }

    public void removeMat(Long matId) {
        Mat mat = matRepository.findById(matId).orElseThrow();
        Tournament tournament = mat.getTournament();
        tournament.getMats().remove(mat);
        tournamentRepository.save(tournament);
    }

    public List<ClubDTO> getTournamentClubs(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        List<ClubDTO> dtos = new LinkedList<>();
        for (Club club : tournament.getClubs()) {
            ClubDTO dto = createClubDTO(club);
            dtos.add(dto);
        }
        return dtos;
    }

    private ClubDTO createClubDTO(Club club) {
        ClubDTO dto = new ClubDTO();
        dto.setId(club.getId());
        dto.setUsername(club.getUsername());
        dto.setAdmin(club.isAdmin());
        return dto;
    }

    public void addClubToTournament(Long clubId, Long tournamentId) throws ObjectNotFoundException {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        Club club = clubService.getClubById(clubId);
        if (!tournament.getClubs().contains(club)) {
            tournament.getClubs().add(club);
            tournamentRepository.save(tournament);
        }
    }

    public void removeClubFromTournament(Long clubId, Long tournamentId) throws ObjectNotFoundException {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        Club club = clubService.getClubById(clubId);
        if (tournament.getClubs().contains(club)) {
            tournament.getClubs().remove(club);
            tournamentRepository.save(tournament);
        }
    }

    public void addRefereeToTournament(Long refereeId, Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        Referee referee = refereeRepository.findById(refereeId).orElseThrow();
        if (!tournament.getReferees().contains(referee)) {
            tournament.getReferees().add(referee);
            tournamentRepository.save(tournament);
        }
    }

    public void removeRefereeFormTournament(Long refereeId, Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        Referee referee = refereeRepository.findById(refereeId).orElseThrow();
        if (tournament.getReferees().contains(referee)) {
            tournament.getReferees().remove(referee);
            tournamentRepository.save(tournament);
        }
    }

    public List<CategoryDTO> getFreeCategories(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        List<CategoryDTO> categories = new LinkedList<>();
        List<LadderCategory> ladderCategories = ladderCategoryRepository.findByMatIdIsNullAndTournamentId(tournamentId).orElseThrow();
        List<TableCategory> tableCategories = tableCategoryRepository.findByMatIdIsNullAndTournamentId(tournamentId).orElseThrow();
        for (LadderCategory category : ladderCategories) {
            if (category.isGenerated())
                categories.add(createCategoryDTO(category, "ladder"));
        }
        for (TableCategory category : tableCategories) {
            if (category.isGenerated())
                categories.add(createCategoryDTO(category, "table"));
        }
        return categories;
    }

    private CategoryDTO createCategoryDTO(Category category, String categoryType) {
        CategoryDTO dto = new CategoryDTO();
        dto.setType(categoryType);
        dto.setName(category.getName());
        dto.setId(category.getId());
        try {
            dto.setFirstPlace(createCompetitorTableDTO(category.getFirstPlace()));
            dto.setSecondPlace(createCompetitorTableDTO(category.getSecondPlace()));
            dto.setThirdPlace(createCompetitorTableDTO(category.getThirdPlace()));

        } catch (Exception ignored) {
        }
        return dto;
    }

    public void addCategoryToMat(Long categoryId, Long matId, String categoryType) throws IllegalAccessException {
        Mat mat = matRepository.findById(matId).orElseThrow();
        if (!mat.getCategoryQueque().contains(categoryId)) {
            switch (categoryType) {
                case "ladder":
                    LadderCategory ladderCategory = ladderCategoryRepository.findById(categoryId).orElseThrow();

                    ladderCategory.setMatId(matId);
                    ladderCategoryRepository.save(ladderCategory);
                    mat.getCategoryQueque().add(categoryId);
                    matRepository.save(mat);
                    ensureMainReferee(ladderCategory.getFirstPlaceFight(), mat.getMatLeader().getId());
                    ensureMainReferee(ladderCategory.getThridPlaceFight(), mat.getMatLeader().getId());
                    break;
                case "table":
                    TableCategory tableCategory = tableCategoryRepository.findById(categoryId).orElseThrow();
                    tableCategory.setMatId(matId);
                    tableCategoryRepository.save(tableCategory);
                    mat.getCategoryQueque().add(categoryId);
                    matRepository.save(mat);
                    break;
                default:
                    throw new IllegalAccessException("Illegal category type");
            }
        }
    }

    public void removeCategoryFromMat(Long categoryId, Long matId, String categoryType) throws IllegalAccessException {
        Mat mat = matRepository.findById(matId).orElseThrow();
        if (mat.getCategoryQueque().contains(categoryId)) {
            switch (categoryType) {
                case "ladder":
                    LadderCategory ladderCategory = ladderCategoryRepository.findById(categoryId).orElseThrow();
                    ladderCategory.setMatId(null);
                    ladderCategoryRepository.save(ladderCategory);
                    mat.getCategoryQueque().remove(categoryId);
                    matRepository.save(mat);
                    break;
                case "table":
                    TableCategory tableCategory = tableCategoryRepository.findById(categoryId).orElseThrow();
                    tableCategory.setMatId(null);
                    tableCategoryRepository.save(tableCategory);
                    mat.getCategoryQueque().remove(categoryId);
                    matRepository.save(mat);
                    break;
                default:
                    throw new IllegalAccessException("Illegal category type");
            }
        }
    }

    public List<CategoryDTO> getMatCategories(Long matId) {
        Mat mat = matRepository.findById(matId).orElseThrow();
        List<CategoryDTO> dtos = new LinkedList<>();
        for (Long categoryId : mat.getCategoryQueque()) {
            try {
                LadderCategory category = ladderCategoryRepository.findById(categoryId).orElseThrow();
                dtos.add(createCategoryDTO(category, "ladder"));
            } catch (Exception e) {
                try {
                    TableCategory category = tableCategoryRepository.findById(categoryId).orElseThrow();
                    dtos.add(createCategoryDTO(category, "table"));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return dtos;
    }

    public void removeCompetitorFormTournament(Long competitorId, Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        Competitor competitor = competitorRepository.findById(competitorId).orElseThrow();
        if (tournament.getCompetitors().contains(competitor)) {
            tournament.getCompetitors().remove(competitor);
            tournamentRepository.save(tournament);
        }
    }

    public List<CompetitorTableDTO> getTournamentCompetitors(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        List<CompetitorTableDTO> dtos = new LinkedList<>();
        for (Competitor competitor : tournament.getCompetitors()) {
            CompetitorTableDTO dto = createCompetitorTableDTO(competitor);
            dtos.add(dto);
        }
        return dtos;
    }

    public CategoryDTO getOngoingRefereeCategory(String refereeName) throws IllegalAccessException {
        Referee referee = refereeRepository.findByUsername(refereeName);
        List<Tournament> tournaments = tournamentRepository.findTournamentsByRefereeInReferees(referee);
        Date today = new Date();
        for (Tournament tournament : tournaments) {
            if (tournament.getStartDate().before(today) && tournament.getEndDate().after(today)) {
                for (Mat mat : tournament.getMats()) {
                    try {
                        if (mat.getReferees().contains(referee) || mat.getMatLeader().equals(referee)) {
                            Long categoryId = mat.getCategoryQueque().getFirst();
                            Category category = null;
                            String categoryType = null;
                            try {
                                category = ladderCategoryRepository.findById(categoryId).orElseThrow();

                                categoryType = "ladder";
                            } catch (Exception e) {
                                try {
                                    category = tableCategoryRepository.findById(categoryId).orElseThrow();
                                    categoryType = "table";
                                } catch (Exception ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                            return createCategoryDTO(category, categoryType);
                        }
                    } catch (Exception e) {

                    }
                }

            }
        }
        throw new IllegalAccessException("no onging tournaments");
    }

    public void finishCompetition(Long categoryId) throws ObjectNotFoundException, IllegalAccessException {
        Category category = getCategory(categoryId);
        Mat mat = matRepository.findById(category.getMatId()).orElseThrow();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        if (!Objects.equals(mat.getMatLeader().getUsername(), currentPrincipalName)) {
            logger.info("Attempt to remove competitor from another club");
            throw new IllegalAccessException("Attempt to remove competitor from another club");
        }
        if (category.getId().equals(mat.getCategoryQueque().getFirst())) {
            mat.getCategoryQueque().remove(category.getId());
            mat.getFinishedCategories().add(category.getId());
            matRepository.save(mat);
        }

        if (category instanceof LadderCategory) {
            LadderCategory ladderCategory = (LadderCategory) category;
            ladderCategory.setFirstPlace(ladderCategory.getFirstPlaceFight().getWinner());
            ladderCategory.setSecondPlace(getLoser(ladderCategory.getFirstPlaceFight()));
            ladderCategory.setThirdPlace(ladderCategory.getThridPlaceFight().getWinner());
            ladderCategoryRepository.save(ladderCategory);
        } else if (category instanceof TableCategory) {
            TableCategory tableCategory = (TableCategory) category;
            tableCategoryRepository.save(tableCategory);
        }
    }

    public CategoryDTO getOngoingCompetitorCategory(String competitorName) throws IllegalAccessException {
        Competitor competitor = competitorRepository.findByUsername(competitorName);
        List<Tournament> tournaments = tournamentRepository.findTournamentsByCompetitorInCompetitors(competitor);
        Date today = new Date();
        for (Tournament tournament : tournaments) {
            if (tournament.getStartDate().before(today) && tournament.getEndDate().after(today)) {
                for (Mat mat : tournament.getMats()) {
                    try {
                        Long categoryId = mat.getCategoryQueque().getFirst();
                        Category category = null;
                        String categoryType = null;
                        try {
                            category = ladderCategoryRepository.findById(categoryId).orElseThrow();

                            categoryType = "ladder";
                        } catch (Exception e) {
                            try {
                                category = tableCategoryRepository.findById(categoryId).orElseThrow();
                                categoryType = "table";
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        if (category.getCompetitors().contains(competitor)) {
                            return createCategoryDTO(category, categoryType);
                        }
                    } catch (Exception e) {

                    }
                }

            }
        }
        throw new IllegalAccessException("no onging tournaments");
    }

    public void acceptCompetitorCategory(Long competitorId, Long categoryId) throws ObjectNotFoundException, IllegalAccessException {
        Category category = getCategory(categoryId);
        Competitor competitor = competitorRepository.findById(competitorId).orElseThrow();

        if (category.getRemoved().contains(competitor)) {
            throw new IllegalAccessException("Competitor already removed from category");
        }
        if (!category.getClassified().contains(competitor)) {
            throw new IllegalAccessException("Competitor is not classified to this category");
        }
        category.getCompetitors().add(competitor);
        category.getClassified().remove(competitor);
        saveCategory(category);
        checkAndGenerateIfPossible(category);
    }

    public void declineCompetitorCategory(Long competitorId, Long categoryId) throws ObjectNotFoundException, IllegalAccessException {
        Category category = getCategory(categoryId);
        Competitor competitor = competitorRepository.findById(competitorId).orElseThrow();

        if (category.getRemoved().contains(competitor)) {
            throw new IllegalAccessException("Competitor already removed from category");
        }
        if (!category.getClassified().contains(competitor)) {
            throw new IllegalAccessException("Competitor is not classified to this category");
        }
        category.getClassified().remove(competitor);
        saveCategory(category);
        checkAndGenerateIfPossible(category);
    }

    private void checkAndGenerateIfPossible(Category category) {
        if (category.getClassified().isEmpty()) {
            try {
                if (category instanceof LadderCategory) {
                    generateLadderCategory((LadderCategory) category);
                } else if (category instanceof TableCategory) {
                    generateTableCategory((TableCategory) category);
                }
                category.setGenerated(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Map<Long, List<CategoryDTO>> getCompetitorsForComission(Long tournamentId) {
        List<LadderCategory> ladderCategories = ladderCategoryRepository.findByMatIdIsNullAndTournamentId(tournamentId).orElseThrow();
        List<TableCategory> tableCategories = tableCategoryRepository.findByMatIdIsNullAndTournamentId(tournamentId).orElseThrow();
        Map<Long, List<CategoryDTO>> comisionData = new HashMap<>();
        for (LadderCategory category : ladderCategories) {
            for (Competitor competitor : category.getClassified()) {
                if (!comisionData.containsKey(competitor.getId())) {
                    comisionData.put(competitor.getId(), new LinkedList<>());
                }
                comisionData.get(competitor.getId()).add(createCategoryDTO(category, "ladder"));
            }
        }
        for (TableCategory category : tableCategories) {
            for (Competitor competitor : category.getClassified()) {
                if (!comisionData.containsKey(competitor.getId())) {
                    comisionData.put(competitor.getId(), new LinkedList<>());
                }
                comisionData.get(competitor.getId()).add(createCategoryDTO(category, "table"));
            }
        }
        return comisionData;
    }

    public List<TournamentTableDTO> getFutureTournaments() {
        Date today = new Date();
        List<Tournament> tournaments = tournamentRepository.findByStartDateAfter(today);
        List<TournamentTableDTO> dtos = new LinkedList<>();
        for (Tournament tournament : tournaments) {
            TournamentTableDTO dto = createTournamentTableDTO(tournament);
            dtos.add(dto);
        }
        return dtos;
    }

    public List<TournamentTableDTO> getPastTournaments() {
        Date today = new Date();
        List<Tournament> tournaments = tournamentRepository.findByStartDateBefore(today);
        List<TournamentTableDTO> dtos = new LinkedList<>();
        for (Tournament tournament : tournaments) {
            TournamentTableDTO dto = createTournamentTableDTO(tournament);
            dtos.add(dto);
        }
        return dtos;
    }

    public TableCategoryDTO getTableCategoryByIdDTO(Long categoryId) {
        if (tableCategoryRepository.findById(categoryId).isPresent()) {
            TableCategory category = tableCategoryRepository.findById(categoryId).get();
            TableCategoryDTO dto = new TableCategoryDTO();
            dto.setId(category.getId());
            dto.setName(category.getName());
            try {
                dto.setScores(new HashSet<>());
                for (TableData score : category.getScores()) {
                    dto.getScores().add(createTableDataDTO(score));
                }

            } catch (Exception ignored) {
            }
            try {
                dto.setRematches(new HashSet<>());
                for (TableData rematch : category.getRematches()) {
                    dto.getRematches().add(createTableDataDTO(rematch));
                }
            } catch (Exception ignored) {
            }
            try {
                dto.setFirstPlace(createCompetitorTableDTO(category.getFirstPlace()));
            } catch (Exception ignored) {
            }
            try {
                dto.setSecondPlace(createCompetitorTableDTO(category.getSecondPlace()));
            } catch (Exception ignored) {
            }
            try {
                dto.setThirdPlace(createCompetitorTableDTO(category.getThirdPlace()));
            } catch (Exception ignored) {
            }
            Mat categoryMat = matRepository.findById(category.getMatId()).orElseThrow();

            TournamentTableDTO tournament = createTournamentTableDTO(categoryMat.getTournament());
            MatDTO matDTO = createMatDTO(categoryMat, tournament);
            dto.setMat(matDTO);
            List<Competitor> competitors = category.getCompetitors().stream().toList();
            dto.setCompetitors(new ArrayList<>());
            for (Competitor competitor : competitors) {
                dto.getCompetitors().add(createCompetitorTableDTO(competitor));
            }
            return dto;
        } else {
            throw new RuntimeException("Category not found");
        }
    }


    private TableDataDTO createTableDataDTO(TableData score) {
        TableDataDTO dto = new TableDataDTO();
        dto.setId(score.getId());
        dto.setCompetitor(createCompetitorTableDTO(score.getCompetitor()));
        dto.setScore(score.getScore());
        return dto;
    }

    public void setTableCategoryScore(Long scoreId, Long scoreValue) {
        TableData score = tableDataRepository.findById(scoreId).orElseThrow();
        score.setScore(scoreValue);
        tableDataRepository.save(score);
        TableCategory category = tableCategoryRepository.findByScoresContaining(score);
        if (category == null) {
            category = tableCategoryRepository.findByRematchesContaining(score);
        }
        TableData top1 = null;
        TableData top2 = null;
        TableData top3 = null;
        Set<TableData> allScores = new HashSet<>();
        allScores.addAll(category.getScores());
        allScores.addAll(category.getRematches());
        for (TableData tableData : allScores) {
            if (!tableData.getChecked()) {
                if (tableData.getScore() == null) {
                    return;
                } else if (category.getFirstPlace() == null && (top1 == null || tableData.getScore() > top1.getScore())) {
                    top3 = top2;
                    top2 = top1;
                    top1 = tableData;
                } else if (category.getSecondPlace() == null && (top2 == null || tableData.getScore() > top2.getScore())) {
                    top3 = top2;
                    top2 = tableData;
                } else if (category.getThirdPlace() == null && (top3 == null || tableData.getScore() > top3.getScore())) {
                    top3 = tableData;

                }
            }
        }
        boolean top1Set = false;
        boolean top2Set = false;
        boolean top3Set = false;
        if (top1 == null) {
            top1Set = true;
        }
        if (top2 == null) {
            top2Set = true;
        }
        if (top3 == null) {
            top3Set = true;
        }


        Boolean rematchNeeded = false;
        List<TableData> rematchCopies = new ArrayList<>();

        if (!top1Set && Objects.equals(top1.getScore(), top2.getScore()) && Objects.equals(top2.getScore(), top3.getScore())) {
            TableData c1 = new TableData();
            c1.setCompetitor(top1.getCompetitor());
            c1.setChecked(false);
            c1 = tableDataRepository.save(c1);
            rematchCopies.add(c1);
            TableData c2 = new TableData();
            c2.setChecked(false);
            c2.setCompetitor(top2.getCompetitor());
            c2 = tableDataRepository.save(c2);
            rematchCopies.add(c2);
            TableData c3 = new TableData();
            c3.setCompetitor(top3.getCompetitor());
            c3.setChecked(false);
            c3 = tableDataRepository.save(c3);
            rematchCopies.add(c3);
            rematchNeeded = true;
        } else {
            if (!top1Set && Objects.equals(top1.getScore(), top2.getScore())) {
                TableData c1 = new TableData();
                c1.setCompetitor(top1.getCompetitor());
                c1.setChecked(false);
                c1 = tableDataRepository.save(c1);
                rematchCopies.add(c1);
                TableData c2 = new TableData();
                c2.setCompetitor(top2.getCompetitor());
                c2.setChecked(false);
                c2 = tableDataRepository.save(c2);
                rematchCopies.add(c2);
                rematchNeeded = true;
            } else if (!top1Set) {
                category.setFirstPlace(top1.getCompetitor());
                category = tableCategoryRepository.save(category);
            }

            if (!top2Set && Objects.equals(top2.getScore(), top3.getScore())) {
                TableData c2 = new TableData();
                c2.setCompetitor(top2.getCompetitor());
                c2.setChecked(false);
                c2 = tableDataRepository.save(c2);
                rematchCopies.add(c2);
                TableData c3 = new TableData();
                c3.setChecked(false);
                c3.setCompetitor(top3.getCompetitor());
                c3 = tableDataRepository.save(c3);
                rematchCopies.add(c3);
                rematchNeeded = true;
            } else if (!rematchNeeded && !top2Set) {
                category.setSecondPlace(top2.getCompetitor());
                category = tableCategoryRepository.save(category);
            }
        }

        if (!top3Set) {
            Boolean thirdPlaceRematch = false;
            for (TableData scoreData : category.getScores()) {
                scoreData.setChecked(true);
                if (top1 != null && !scoreData.getCompetitor().equals(top1.getCompetitor()) && !scoreData.getCompetitor().equals(top2.getCompetitor()) && !scoreData.getCompetitor().equals(top3.getCompetitor()) && Objects.equals(scoreData.getScore(), top3.getScore())) {
                    thirdPlaceRematch = true;
                    TableData copy = new TableData();
                    copy.setCompetitor(scoreData.getCompetitor());
                    copy = tableDataRepository.save(copy);
                    rematchCopies.add(copy);
                    rematchNeeded = true;
                }
            }
            if (!thirdPlaceRematch && category.getSecondPlace() != null) {
                category.setThirdPlace(top3.getCompetitor());
                category = tableCategoryRepository.save(category);
            }
        }

        if (!rematchCopies.isEmpty()) {
            category.getRematches().addAll(rematchCopies);
            tableCategoryRepository.save(category);
        }

        if (rematchNeeded) {
            throw new RematchNeededException("Rematch needed due to tie scores");
        }
        tableCategoryRepository.save(category);
    }

    public void removeCompetitorFromTableCategory(Long competitorId, Long categoryId) {
        TableCategory category = tableCategoryRepository.findById(categoryId).orElseThrow();
        Competitor competitor = competitorRepository.findById(competitorId).orElseThrow();
        for (TableData data : category.getScores()) {
            if (data.getCompetitor().equals(competitor)) {
                data.setScore(0L);
                tableDataRepository.save(data);
                break;
            }
        }
    }

    public List<CategoryDTO> getTournamentCategories(Long tournamentId) {
        List<LadderCategory> ladderCategories = ladderCategoryRepository.findByTournamentId(tournamentId);
        List<TableCategory> tableCategories = tableCategoryRepository.findByTournamentId(tournamentId);
        List<CategoryDTO> dtos = new LinkedList<>();
        for (LadderCategory category : ladderCategories) {
            dtos.add(createCategoryDTO(category, "ladder"));
        }
        for (TableCategory category : tableCategories) {
            dtos.add(createCategoryDTO(category, "table"));
        }
        return dtos;
    }

    public TournamentStatisticsDTO getTournamentStatisticsDTO(Long tournamentId) throws ObjectNotFoundException {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        TournamentStatisticsDTO dto = new TournamentStatisticsDTO();
        dto.setName(tournament.getName());
        dto.setId(tournamentId);
        dto.setDate(tournament.getStartDate().toString());
        dto.setEndDate(tournament.getEndDate().toString());
        dto.setLocation(tournament.getLocation());
        dto.setCategories(new ArrayList<>());
        for (Mat mat : tournament.getMats()) {
            for (Long categoryId : mat.getFinishedCategories()) {
                Category category = getCategory(categoryId);
                if (category instanceof LadderCategory) {
                    dto.getCategories().add(createCategoryDTO(category, "ladder"));
                } else if (category instanceof TableCategory) {
                    dto.getCategories().add(createCategoryDTO(category, "table"));
                }
            }
        }
        dto.setClubs(new LinkedList<>());
        for (Club club : tournament.getClubs()){
            dto.getClubs().add(createClubDTO(club));
        }
        dto.setCompetitors(new ArrayList<>());
        for (Competitor competitor : tournament.getCompetitors()) {
            dto.getCompetitors().add(createCompetitorTableDTO(competitor));
        }
        dto.getClubs().add(createClubDTO(tournament.getOrganizerClub()));
        return dto;
    }
}
