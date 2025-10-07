package pl.tkd.tournaments.tkd_tournament_maker.club.club;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.CompetitorRepository;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.CompetitorTableDTO;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.Referee;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.RefereeDTO;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.RefereeRepository;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.ObjectNotFoundException;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament.Tournament;

import java.util.*;

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
            dto.setUserName(referee.getUsername());
            dto.setRefereeClass(referee.getRefereeClass());
            referees.add(dto);
        }
        return referees;
    }

    public List<CompetitorTableDTO> getCompetitorsByClub(String clubName) {
        Club club = getClubByName(clubName);
        List<CompetitorTableDTO> competitors = new LinkedList<>();
        for (Competitor competitor : competitorRepository.findByClub(club)) {
            Set<Long> tournamentIds = new HashSet<>();
            for (Tournament tournament : competitor.getTournaments()) {
                tournamentIds.add(tournament.getId());
            }
            CompetitorTableDTO dto = new CompetitorTableDTO();
            dto.setId(competitor.getId());
            dto.setFirstname(competitor.getFirstName());
            dto.setLastname(competitor.getLastName());
            dto.setTournamentIds(tournamentIds);
            dto.setUserName(competitor.getUsername());
            dto.setBelt(competitor.getBelt());
            competitors.add(dto);
        }
        return competitors;
    }

    public Competitor getCompetitorById(Long id) throws ObjectNotFoundException {
        if (competitorRepository.findById(id).isPresent())
            return competitorRepository.findById(id).get();
        throw new ObjectNotFoundException("Competitor doesn't exist");
    }

    public  RefereeDTO getRefereeDTOById(Long id) throws ObjectNotFoundException {
        RefereeDTO dto = new RefereeDTO();
        Optional<Referee> referee = refereeRepository.findById(id);
        if (referee.isPresent()){
            dto.setId(referee.get().getId());
            dto.setFirstname(referee.get().getFirstName());
            dto.setLastname(referee.get().getLastName());
            dto.setTournamentIds(new HashSet<>());
            for (Tournament tournament : referee.get().getTournaments()) {
                dto.getTournamentIds().add(tournament.getId());
            }
            dto.setUserName(referee.get().getUsername());
            dto.setRefereeClass(referee.get().getRefereeClass());
            return dto;
        }
        else {
            throw new ObjectNotFoundException("Referee not found");
        }
    }
}
