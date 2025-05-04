package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
    Set<LadderLayer> layers;

    @OneToMany
    Set<Competitor> competitors;

    public void initialLayer() throws IllegalAccessException {
        if (layers != null) throw new IllegalAccessException("");
        layers = new HashSet<>();
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
        layers.add(layer);
    }

    public void newLayer() throws IllegalAccessException {
        if (layers == null) throw new IllegalAccessException("");
        LadderLayer layer = new LadderLayer();
        List<LadderLayer> layersList = new ArrayList<>(layers);
        layersList.sort(Comparator.comparing(LadderLayer::getId));
        LadderLayer previousLayer = layersList.getLast();
        List<Fight> previousLayerFights = new ArrayList<>(previousLayer.getFights());
        previousLayerFights.sort(Comparator.comparing(Fight::getId));
        Fight exceptionalFight = null;
        if(previousLayerFights.size()%2!=0){
            exceptionalFight = previousLayerFights.getLast();
            previousLayerFights.remove(exceptionalFight);
        }
        for (int i = 0; i < previousLayerFights.size(); i+=2) {
            Fight fight = new Fight();
            fight.setCompetitor1(previousLayerFights.get(i).getWinner());
            fight.setCompetitor2(previousLayerFights.get(i).getWinner());
            layer.addFight(fight);
        }
        if (exceptionalFight != null) layer.addFight(exceptionalFight);
        layers.add(layer);
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
