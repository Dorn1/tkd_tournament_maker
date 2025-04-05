package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter;

import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Belt;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Sex;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterDecorator.AgeCategory;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterDecorator.BeltCategory;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterDecorator.SexCategory;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterDecorator.WeightCategory;

public class CategoryFilterBuilder {
    private ICategoryFilter categoryFilter;
    public CategoryFilterBuilder() {
        this.categoryFilter = new BaseCategoryFilter();
    }



    public void addminAge(int minAge) {
        categoryFilter = new AgeCategory(categoryFilter, minAge, 1000);
    }
    public void addmaxAge(int maxAge) {
        categoryFilter = new AgeCategory(categoryFilter, 0, maxAge);
    }
    public void setSex(Sex sex) {
        categoryFilter = new SexCategory(categoryFilter,sex);
    }
    public void addminBelt(Belt minBelt) {
        categoryFilter = new BeltCategory(categoryFilter,minBelt,Belt.BLACK_9_DAN);
    }
    public void addmaxBelt(Belt maxBelt) {
        categoryFilter = new BeltCategory(categoryFilter,Belt.WHITE,maxBelt);
    }
    public void addminWeight(int minWeight) {
        categoryFilter = new WeightCategory(categoryFilter, minWeight, 1000);
    }
    public void addmaxWeight(int maxWeight) {
        categoryFilter = new WeightCategory(categoryFilter, 0, maxWeight);
    }
}
