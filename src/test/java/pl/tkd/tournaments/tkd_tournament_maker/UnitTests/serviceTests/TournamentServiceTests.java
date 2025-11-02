//package pl.tkd.tournaments.tkd_tournament_maker.UnitTests.serviceTests;
//
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import pl.tkd.tournaments.tkd_tournament_maker.club.club.Club;
//import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Competitor;
//import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Sex;
//import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.ladderCategory.LadderCategory;
//import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.ladderCategory.Fight;
//import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.ladderCategory.FightRepository;
//import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.ladderCategory.LadderCategoryRepository;
//import pl.tkd.tournaments.tkd_tournament_maker.tournament.TournamentService;
//
//import java.time.Year;
//import java.util.*;
//
//@ExtendWith(MockitoExtension.class)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class TournamentServiceTests {
//    @Mock
//    private LadderCategoryRepository categoryRepository;
//    @Mock
//    private FightRepository fightRepository;
//    @InjectMocks
//    private TournamentService tournamentService;
//
//    private LadderCategory testCategory1;
//
//    @BeforeEach
//    public void setup() {
//        Club club = new Club();
//        club.setUsername("TkdFreaks");
//        club.setId(1L);
//        Long thisYear = (long) Year.now().getValue();
//
//
//        Random random = new Random();
//        Set<Competitor> competitors1 = new HashSet<>();
//        for (int i = 0; i< random.nextInt(1000)+2; i++){
//            Competitor competitor1;
//            competitor1 = new Competitor();
//            competitor1.setSex(Sex.Male);
//            competitor1.setBirthYear(thisYear - 15);
//            competitor1.setBelt(5);
//            competitor1.setId((long) i);
//            competitor1.setWeight(65.5);
//            competitor1.setClub(club);
//            competitor1.setFirstName("John");
//            competitor1.setLastName("Smith");
//            competitors1.add(competitor1);
//        }
//        testCategory1 = new LadderCategory();
//        testCategory1.setCompetitors(competitors1);
//
//    }
//
//    @Test
//    public void ladderCategoryGeneratingTest() {
//        try {
//            tournamentService.generateLadderCategory(testCategory1);
//        } catch (IllegalAccessException e) {
//            Assertions.fail();
//        }
//        List<Long> firstLayer = testCategory1.getFirstPlaceFight().getFightsBefore().stream().toList();
//        Assertions.assertEquals(2,firstLayer.size());
//       // Assertions.assertTrue(Math.abs(countAscendants(firstLayer.getFirst())-countAscendants(firstLayer.get(1))) <= 1);
//    }
//
////    private int countAscendants(Fight startFight) {
////        int counter = 0;
////        Stack<Fight> waitingFights = new Stack<>();
////        waitingFights.push(startFight);
////        while (!waitingFights.isEmpty()) {
////            Fight currentFight = waitingFights.pop();
////            counter++;
////            for (Fight fightBefore : currentFight.getFightsBefore()) {
////                waitingFights.push(fightBefore);
////            }
////        }
////        return counter;
////    }
//}
