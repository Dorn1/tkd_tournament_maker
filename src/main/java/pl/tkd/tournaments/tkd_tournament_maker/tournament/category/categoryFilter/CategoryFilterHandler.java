package pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categoryFilter;

import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Sex;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categoryFilter.categoryFilterDecorator.AgeCategory;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categoryFilter.categoryFilterDecorator.BeltCategory;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categoryFilter.categoryFilterDecorator.SexCategory;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categoryFilter.categoryFilterDecorator.WeightCategory;

public class CategoryFilterHandler {
    private ICategoryFilter categoryFilter;
    public CategoryFilterHandler() {
        this.categoryFilter = new BaseCategoryFilter();
    }

    public void addminAge(Long minAge) {
        categoryFilter = new AgeCategory(categoryFilter, minAge, 1000L);
    }

    public void addmaxAge(Long maxAge) {
        categoryFilter = new AgeCategory(categoryFilter, 0L, maxAge);
    }

    public void setSex(Sex sex) {
        categoryFilter = new SexCategory(categoryFilter, sex);
    }

    public void addminBelt(Integer minBelt) {
        categoryFilter = new BeltCategory(categoryFilter, minBelt, Integer.MAX_VALUE);
    }

    public void addmaxBelt(Integer maxBelt) {
        categoryFilter = new BeltCategory(categoryFilter, Integer.MIN_VALUE, maxBelt);
    }

    public void addminWeight(Double minWeight) {
        categoryFilter = new WeightCategory(categoryFilter, minWeight, Double.MAX_VALUE);
    }

    public void addmaxWeight(Double maxWeight) {
        categoryFilter = new WeightCategory(categoryFilter, Double.MIN_VALUE, maxWeight);
    }

    public ICategoryFilter build() {
        return categoryFilter;
    }

}
