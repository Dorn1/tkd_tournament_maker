package pl.tkd.tournaments.tkd_tournament_maker;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Belt;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Sex;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.BaseCategoryFilter;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.ICategoryFilter;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
class TkdTournamentMakerApplicationTests {

    @Test
    void contextLoads() {
    }
    @Test
    public void BaseCategoryFilterTest() {
        //ICategoryFilter filter = new BaseCategoryFilter();
        //Assert.assertEquals(competitors,filter.filter(competitors));

    }

}
