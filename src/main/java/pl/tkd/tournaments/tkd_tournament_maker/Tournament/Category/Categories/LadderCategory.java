package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Fight;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.ObjectNotFoundException;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class LadderCategory extends Category {

    @OneToMany
    Set<Competitor> competitors;

    @OneToOne
    private Fight firstPlaceFight;

    @OneToOne
    private Fight thridPlaceFight;

    public void generateCategory() throws IllegalAccessException {
        if (firstPlaceFight != null) {
            throw new IllegalAccessException("Category already has generated ladder");
        }
        boolean even = false;
        firstPlaceFight = new Fight();
        int competitorFightSum = 2;
        List<Fight> thisLayerQueque = new ArrayList<>();
        thisLayerQueque.add(firstPlaceFight);
        List<Fight> nextLayerQueque = new ArrayList<>();
        nextLayerQueque.add(firstPlaceFight);

        while (competitorFightSum < competitors.size()) {
            Fight generatingFight = null;
            if (!even) {
                generatingFight = thisLayerQueque.removeFirst();
                even = true;
            } else {
                generatingFight = thisLayerQueque.removeLast();
                even = false;
            }
            if (competitorFightSum == competitors.size()-1) {
                Fight beforeFight1 = new Fight();
                generatingFight.getFightsBefore().add(beforeFight1);
                competitorFightSum++;
            }
            Fight beforeFight1 = new Fight();
            Fight beforeFight2 = new Fight();
            generatingFight.getFightsBefore().add(beforeFight1);
            generatingFight.getFightsBefore().add(beforeFight2);
            nextLayerQueque.add(beforeFight1);
            nextLayerQueque.add(beforeFight2);
            if(thisLayerQueque.isEmpty()){
                thisLayerQueque = nextLayerQueque;
                nextLayerQueque = new ArrayList<>();
            }
            competitorFightSum +=2;
        }
    }

    private Competitor randomCompetitor(Set<Competitor> competitorSet) {
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
