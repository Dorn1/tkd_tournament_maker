package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter;

import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Sex;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterDecorator.AgeCategory;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterDecorator.BeltCategory;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterDecorator.SexCategory;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterDecorator.WeightCategory;

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
