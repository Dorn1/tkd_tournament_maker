package pl.tkd.tournaments.tkd_tournament_maker.UnitTests.serviceTests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Club.Club;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Sex;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories.Category;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories.CategoryRepository;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories.LadderCategory;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.FightRepository;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Tournament.Tournament;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Tournament.TournamentService;

import java.time.Year;
import java.util.HashSet;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TournamentServiceTests {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private FightRepository fightRepository;
    @InjectMocks
    private TournamentService tournamentService;

    private Set<Competitor> competitors;
    private LadderCategory testCategory;

    @BeforeEach
    public void setup() {
        Club club = new Club();
        club.setName("TkdFreaks");
        club.setId(1L);
        club.setCompetitors(new HashSet<>());
        Long thisYear = (long) Year.now().getValue();


        competitors = new HashSet<>();
        for (int i = 0; i<34;i++){
            Competitor competitor1;
            competitor1 = new Competitor();
            competitor1.setSex(Sex.Male);
            competitor1.setBirthYear(thisYear - 15);
            competitor1.setBelt(5);
            competitor1.setId(1L);
            competitor1.setWeight(65.5);
            competitor1.setClub(club);
            competitor1.setFirstName("John");
            competitor1.setLastName("Smith");
            competitors.add(competitor1);
        }
        testCategory = new LadderCategory();
        testCategory.setCompetitors(competitors);
    }

    @Test
    public void ladderCategoryGeneratingTest() {
        try {
            tournamentService.generateLadderCategory(testCategory);
        } catch (IllegalAccessException e) {
            Assertions.fail();
        }
        System.out.println(testCategory.getFights());
    }
}
