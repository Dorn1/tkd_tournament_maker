package pl.tkd.tournaments.tkd_tournament_maker.UnitTests.CategoryTests;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Club.Club;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.CompetitorRepository;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Sex;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories.CategoryRepository;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.FightRepository;

import java.time.Year;
import java.util.HashSet;
import java.util.Set;

public class CategorytTests {
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    FightRepository fightRepository;
    @Mock
    CompetitorRepository competitorRepository;
    private Competitor competitor1;
    private Competitor competitor2;
    private Competitor competitor3;
    private Set<Competitor> competitors;

    @BeforeEach
    public void setup() {
        Club club = new Club();
        club.setName("TkdFreaks");
        club.setId(1L);
        club.setCompetitors(new HashSet<>());
        Long thisYear = (long) Year.now().getValue();

        competitor1 = new Competitor();
        competitor1.setSex(Sex.Male);
        competitor1.setBirthYear(thisYear - 15) ;
        competitor1.setBelt(5);
        competitor1.setId(1L);
        competitor1.setWeight(65.5);
        competitor1.setClub(club);
        competitor1.setFirstName("John");
        competitor1.setLastName("Smith");

        competitor2 = new Competitor();
        competitor2.setSex(Sex.Male);
        competitor2.setBirthYear(thisYear - 5);
        competitor2.setBelt(1);
        competitor2.setId(1L);
        competitor2.setWeight(10.0);
        competitor2.setClub(club);
        competitor2.setFirstName("John");
        competitor2.setLastName("Little");

        competitor3 = new Competitor();
        competitor3.setSex(Sex.Female);
        competitor3.setBirthYear(thisYear - 30);
        competitor3.setBelt(8);
        competitor3.setId(1L);
        competitor3.setWeight(60.0);
        competitor3.setClub(club);
        competitor3.setFirstName("Mary");
        competitor3.setLastName("Sue");

        competitors = new HashSet<>();
        competitors.add(competitor1);
        competitors.add(competitor2);
        competitors.add(competitor3);
    }
}
