package pl.tkd.tournaments.tkd_tournament_maker.exceptions;

import lombok.Getter;
import pl.tkd.tournaments.tkd_tournament_maker.Tournament.Category.Categories.TableCategory.TableData;

import java.util.List;

@Getter
public class RematchNeededException extends RuntimeException {
    private final List<TableData> competitors;
    public RematchNeededException(String message, List<TableData> competitors) {
        super(message);
        this.competitors = competitors;
    }

}
