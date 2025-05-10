package pl.tkd.tournaments.tkd_tournament_maker.exceptions;

import lombok.Getter;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.TableData;

@Getter
public class RematchNeededException extends RuntimeException {
    private final TableData competitor1;
    private final TableData competitor2;
    public RematchNeededException(String message, TableData competitor1, TableData competitor2) {
        super(message);
        this.competitor1 = competitor1;
        this.competitor2 = competitor2;
    }

}
