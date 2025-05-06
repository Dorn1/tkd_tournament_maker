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
    Set<Competitor> competitors;

    @OneToMany
    private Set<Fight> fights;

    @OneToOne
    private Fight firstPlaceFight;

    @OneToOne
    private Fight thridPlaceFight;

    public void generateCategory() throws IllegalAccessException {
        if (firstPlaceFight != null) {
            throw new IllegalAccessException("Category already has generated ladder");
        }
        firstPlaceFight = new Fight();
        fights.add(firstPlaceFight);
        int competitorFightSum = 2;
        List<Fight> thisLayerQueque = new ArrayList<>();
        thisLayerQueque.add(firstPlaceFight);
        List<Fight> nextLayerQueque = new ArrayList<>();
        boolean thirdPlaceFightSet = false;
        List<Fight> lastLayerCopy = thisLayerQueque;
        while (competitorFightSum < competitors.size()) {
            if (!thirdPlaceFightSet && thisLayerQueque.size() ==2) {
                thirdPlaceFightSet = true;
                thridPlaceFight = new Fight();
                thridPlaceFight.getFightsBefore().add(thisLayerQueque.getFirst());
                thridPlaceFight.getFightsBefore().add(thisLayerQueque.getLast());
                fights.add(thridPlaceFight);
            }
            Fight generatingFight = thisLayerQueque.removeFirst();
            if (competitorFightSum == competitors.size()-1) {
                Fight beforeFight1 = new Fight();
                generatingFight.getFightsBefore().add(beforeFight1);
                break;
            }
            Fight beforeFight1 = new Fight();
            Fight beforeFight2 = new Fight();
            generatingFight.getFightsBefore().add(beforeFight1);
            generatingFight.getFightsBefore().add(beforeFight2);
            fights.add(beforeFight1);
            fights.add(beforeFight2);
            nextLayerQueque.add(beforeFight1);
            nextLayerQueque.add(beforeFight2);
            if(thisLayerQueque.isEmpty()){
                thisLayerQueque = evenQueque(nextLayerQueque);
                lastLayerCopy = thisLayerQueque;
                nextLayerQueque = new ArrayList<>();
            }
            competitorFightSum +=2;
        }
        Set<Competitor> competitorsCopy = new HashSet<>();
        for (Fight fight : lastLayerCopy) {
            if (competitorsCopy.size() >1){
                fight.addCompetitor(randomCompetitor(competitorsCopy));
            }
            fight.addCompetitor(randomCompetitor(competitorsCopy));
        }
    }

    public List<Fight> evenQueque(List<Fight> thisLayerQueque) throws IllegalAccessException {
        if (thisLayerQueque == null) {throw new IllegalAccessException("null Fight Queque provided");}
        if (thisLayerQueque.size() <=4){
            List<Fight> quartet = new ArrayList<>();
            quartet.add(thisLayerQueque.getFirst());
            if (thisLayerQueque.size() >=3) {
                quartet.add(thisLayerQueque.get(2));
            }
            if (thisLayerQueque.size() >=2) {
                quartet.add(thisLayerQueque.get(1));
            }
            if (thisLayerQueque.size() ==4) {
                quartet.add(thisLayerQueque.get(3));
            }
            return quartet;
        }
        List<Fight> byTwo1 = new ArrayList<>();
        List<Fight> byTwo2 = new ArrayList<>();
        for (int i = 0; i < thisLayerQueque.size(); i++) {
            if (i%2 == 0) {
                byTwo1.add(thisLayerQueque.get(i));
            }else{
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
