package pl.tkd.tournaments.tkd_tournament_maker.exceptions;

import lombok.Getter;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.tableCategory.TableData;

import java.util.List;

@Getter
public class RematchNeededException extends RuntimeException {
    public RematchNeededException(String message) {
        super(message);
    }

}
