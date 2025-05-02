package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category;

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
    private List<List<Fight>> fights;

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

    public void randomize_category(){
        Set<Competitor> competitorSet = super.getCompetitors();
        fights = new ArrayList<>();
        ArrayList<Fight> initial = new ArrayList<>();
        if (super.getCompetitors().size() %2 == 1){
            Fight fight = new Fight();
            fight.setCompetitor1(randomCompetitor(competitorSet));
            initial.add(fight);
        }
        for (int i = 0; i<super.getCompetitors().size()/2; i++){
            Fight fight = new Fight();
            fight.setCompetitor1(randomCompetitor(competitorSet));
            fight.setCompetitor2(randomCompetitor(competitorSet));
            initial.add(fight);
        }
        fights.add(initial);
    }

}
