package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Fight;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class LadderCategory extends Category {
    @OneToMany
    Set<LadderLayer> lavers;

    @OneToMany
    Set<Competitor> competitors;

    public void initialLayer() throws IllegalAccessException {
        if (lavers != null) throw new IllegalAccessException("");
        lavers = new HashSet<>();
        LadderLayer layer = new LadderLayer();
        Competitor chosenCompetitor = null;
        if(competitors.size()%2!=0){
            chosenCompetitor = randomCompetitor(competitors);
        }
        for (int i = 0; i < competitors.size()/2; i++) {
            Fight fight = new Fight();
            fight.setCompetitor1(randomCompetitor(competitors));
            fight.setCompetitor2(randomCompetitor(competitors));
            layer.addFight(fight);
        }
        if (chosenCompetitor != null){
            Fight fight = new Fight();
            fight.setCompetitor1(chosenCompetitor);
            layer.addFight(fight);
        }
        lavers.add(layer);
    }


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
