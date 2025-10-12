package pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTournamentRequest {
    private String name;
    private Long startDate;
    private Long endDate;
    private String location;
    private String organizerName;
}
