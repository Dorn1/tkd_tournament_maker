package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterDecorator;


import lombok.Getter;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.ICategoryFilter;


@Getter
public class AgeCategory extends CategoryFilterDecorator {
    Integer min;
    Integer max;
    public AgeCategory(ICategoryFilter filter, Integer min, Integer max) {
        this.min = min;
        this.max = max;
        super(filter);
    }
}
