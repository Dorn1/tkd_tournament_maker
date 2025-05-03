package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class LadderCategory extends Category {

    private Competitor randomCompetitor(Set<Competitor> competitorSet){
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
