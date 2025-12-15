package pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament.dto;

import lombok.Data;
import pl.tkd.tournaments.tkd_tournament_maker.club.club.ClubDTO;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.CompetitorTableDTO;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.category.categories.CategoryDTO;

import java.util.List;
@Data
public class TournamentStatisticsDTO {
    Long id;
    String name;
    String date;
    String endDate;
    String location;
    List<ClubDTO> clubs;
    List<CompetitorTableDTO> competitors;
    List<CategoryDTO> categories;
}
