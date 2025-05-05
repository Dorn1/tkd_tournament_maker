package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    Set<LadderLayer> layers;

    @OneToMany
    Set<Competitor> competitors;

    @OneToOne
    private Fight thridPlaceFight;

    public void createLadder() throws IllegalAccessException, ObjectNotFoundException {
        if (layers != null) throw new IllegalAccessException("");
        List<LadderLayer> layers1 = new ArrayList<>();
        int power;
        for (power = 0; Math.pow(2, power + 1) < competitors.size(); power++) {
            LadderLayer layer = new LadderLayer();
            Fight nextFight = null;
            for (int j = 0; j <= power; j++) {
                Fight thisFight = new Fight();
                if (!layers1.isEmpty()) {
                    Long lowest_index = Long.MAX_VALUE;
                    for (Fight fight : layers1.get(power - 1).getFights()) {
                        if (fight.getId() < lowest_index) {
                            lowest_index = fight.getId();
                        }
                    }
                    for (Fight fight : layers1.get(power - 1).getFights()) {
                        if (j % 2 == 0) {
                            if (fight.getId() - lowest_index - j == 0) {
                                nextFight = fight;
                            }
                        }
                    }
                }
                thisFight.addObserver(nextFight);
                layer.addFight(thisFight);
            }
            if (power ==1){
                for (Fight fight : layer.getFights()) {
                    fight.setThirdPlaceObserver(thridPlaceFight);
                }
            }
            layers1.add(layer);
        }
        Set<Competitor> competitorsCopy = new HashSet<>(competitors);
        for (Fight fight : layers1.getLast().getFights()) {
            fight.setCompetitor1(randomCompetitor(competitorsCopy));
            fight.setCompetitor2(randomCompetitor(competitorsCopy));
        }
        LadderLayer beforeFights = new LadderLayer();
        for (int i = 0; competitorsCopy.size() > 1; i++){
            if (i%2==0){
                Fight nextFight = new Fight();
                nextFight.setCompetitor1(randomCompetitor(competitorsCopy));
                nextFight.setCompetitor2(randomCompetitor(competitorsCopy));
                Long minId = Long.MAX_VALUE;
                for (Fight fight : layers1.getLast().getFights()){
                    if (fight.getId() < minId) {
                        minId = fight.getId();
                        nextFight.getObservers().clear();
                        nextFight.addObserver(fight);
                    }
                }
                beforeFights.addFight(nextFight);
            }
            else{
                Fight nextFight = new Fight();
                nextFight.setCompetitor1(randomCompetitor(competitorsCopy));
                nextFight.setCompetitor2(randomCompetitor(competitorsCopy));
                Long maxId = Long.MIN_VALUE;
                for (Fight fight : layers1.getLast().getFights()){
                    if (fight.getId() > maxId) {
                        maxId = fight.getId();
                        nextFight.getObservers().clear();
                        nextFight.addObserver(fight);
                    }
                }
                beforeFights.addFight(nextFight);
            }
        }
        if(competitors.size() == 1){
            Fight newFight = new Fight();
            newFight.setCompetitor1(randomCompetitor(competitorsCopy));
            List<Fight> fights = layers1.getLast().getFights().stream().toList();
            Fight nextFight = fights.get(fights.size()/2);
            newFight.addObserver(nextFight);
            newFight.setWinner(true);
            beforeFights.addFight(newFight);
        }
        layers1.add(beforeFights);
        layers = new HashSet<>(layers1);
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
