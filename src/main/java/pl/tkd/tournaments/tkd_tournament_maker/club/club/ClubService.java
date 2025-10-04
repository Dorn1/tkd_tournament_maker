package pl.tkd.tournaments.tkd_tournament_maker.club.club;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.CompetitorRepository;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Sex;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.Referee;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.RefereeDTO;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.RefereeRepository;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.ObjectNotFoundException;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament.Tournament;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final CompetitorRepository competitorRepository;
    private final RefereeRepository refereeRepository;


    public Club getClubById(Long id) throws ObjectNotFoundException {
        if (clubRepository.findById(id).isPresent())
            return clubRepository.findById(id).get();
        throw new ObjectNotFoundException("Club doesn't exist");
    }

    public Club getClubByName(String name) {
        return clubRepository.findByUsername(name);
    }

    public List<RefereeDTO> getRefereesByClub(String clubName) {
        Club club = getClubByName(clubName);
        List<RefereeDTO> referees = new LinkedList<>();
        for (Referee referee : refereeRepository.findByClub(club)) {
            RefereeDTO dto = new RefereeDTO();
            dto.setId(referee.getId());
            dto.setFirstname(referee.getFirstName());
            dto.setLastname(referee.getLastName());
            dto.setTournamentIds(new HashSet<>());
            for (Tournament tournament : referee.getTournaments()) {
                dto.getTournamentIds().add(tournament.getId());
            }
            dto.setRefereeClass(referee.getRefereeClass());
            referees.add(dto);
        }
        return referees;
    }

    public Competitor getCompetitorById(Long id) throws ObjectNotFoundException {
        if (competitorRepository.findById(id).isPresent())
            return competitorRepository.findById(id).get();
        throw new ObjectNotFoundException("Competitor doesn't exist");
    }

}
