package pl.tkd.tournaments.tkd_tournament_maker.UnitTests.CategoryFilterTests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Club.Club;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Sex;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterHandler;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.ICategoryFilter;

import java.time.Year;
import java.util.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryFilterTests {

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
        Long thisYear = (long)Year.now().getValue();

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

    @Test
    public void baseCategoryTest(){
        CategoryFilterHandler handler = new CategoryFilterHandler();
        ICategoryFilter categoryFilter = handler.build();
        Assertions.assertEquals(3,categoryFilter.filter(competitors).size());
    }

    @Test
    public void ageCategoryTest(){
        CategoryFilterHandler handler = new CategoryFilterHandler();
        handler.addmaxAge(16L);
        handler.addminAge(10L);
        ICategoryFilter categoryFilter = handler.build();
        Assertions.assertEquals(1,categoryFilter.filter(competitors).size());
        Assertions.assertTrue(categoryFilter.filter(competitors).contains(competitor1));

        handler = new CategoryFilterHandler();
        handler.addmaxAge(16L);
        handler.addminAge(5L);
        categoryFilter = handler.build();
        Assertions.assertEquals(2,categoryFilter.filter(competitors).size());
        Assertions.assertTrue(categoryFilter.filter(competitors).contains(competitor1)
                && categoryFilter.filter(competitors).contains(competitor2));

        handler = new CategoryFilterHandler();
        handler.addmaxAge(30L);
        handler.addminAge(10L);
        categoryFilter = handler.build();
        Assertions.assertEquals(2,categoryFilter.filter(competitors).size());
        Assertions.assertTrue(categoryFilter.filter(competitors).contains(competitor1)
                && categoryFilter.filter(competitors).contains(competitor3));

    }

    @Test
    public void beltCategoryTest(){
        CategoryFilterHandler handler = new CategoryFilterHandler();
        handler.addmaxBelt(7);
        handler.addminBelt(2);
        ICategoryFilter categoryFilter = handler.build();
        Assertions.assertEquals(1,categoryFilter.filter(competitors).size());
        Assertions.assertTrue(categoryFilter.filter(competitors).contains(competitor1));

    }

    @Test
    public void sexCategoryTestFemale(){
        CategoryFilterHandler handler = new CategoryFilterHandler();
        handler.setSex(Sex.Female);
        ICategoryFilter categoryFilter = handler.build();
        Assertions.assertEquals(1,categoryFilter.filter(competitors).size());
        Assertions.assertTrue(categoryFilter.filter(competitors).contains(competitor3));

        handler = new CategoryFilterHandler();
        handler.setSex(Sex.Male);
        categoryFilter = handler.build();
        Assertions.assertEquals(2,categoryFilter.filter(competitors).size());
        Assertions.assertTrue(categoryFilter.filter(competitors).contains(competitor1) && categoryFilter.filter(competitors).contains(competitor2));

    }

    @Test
    public void weightCategoryTest(){
        CategoryFilterHandler handler = new CategoryFilterHandler();
        handler.addmaxWeight(60);
        handler.addminWeight(50);
        ICategoryFilter categoryFilter = handler.build();
        Assertions.assertEquals(1,categoryFilter.filter(competitors).size());
        Assertions.assertTrue(categoryFilter.filter(competitors).contains(competitor3));

    }

}
