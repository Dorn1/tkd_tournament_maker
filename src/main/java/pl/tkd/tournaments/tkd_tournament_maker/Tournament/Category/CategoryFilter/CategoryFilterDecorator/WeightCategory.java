package pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.CategoryFilterDecorator;

import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.CategoryFilter.ICategoryFilter;


public class WeightCategory extends CategoryFilterDecorator {
    public WeightCategory(ICategoryFilter filter, Integer min, Integer max) {
        super(filter,min,max);
    }

//    @Override
//    public Set<Competitor> filter(Set<Competitor> competitors) {
//        return super
//                .filter(competitors)
//                .stream()
//                .filter(c -> c.getWeight() <= super.getMax() &&
//                        c.getWeight() >= super.getMin())
//                .collect(Collectors.toSet());
//
//    }
}
