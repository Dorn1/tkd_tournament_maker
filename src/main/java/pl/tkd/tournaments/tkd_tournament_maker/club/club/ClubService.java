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
import pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament.TournamentRepository;
import pl.tkd.tournaments.tkd_tournament_maker.tournament.tournament.dto.TournamentTableDTO;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final CompetitorRepository competitorRepository;
    private final RefereeRepository refereeRepository;
    private final TournamentRepository tournamentRepository;


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
        for (Referee referee : refereeRepository.findByClub(club).stream()
                .filter(referee -> !referee.getDisabled())
                .toList()) {
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
        for (Competitor competitor : competitorRepository.findByClub(club).stream().filter(competitor -> !competitor.getDisabled()).toList()) {
            Set<Long> tournamentIds = new HashSet<>();
            List<Tournament> competitorTournaments = new ArrayList<>();
            for (Long id : competitor.getTournamentIds()){
                competitorTournaments.add(tournamentRepository.findById(id).orElseThrow());
            }

            for (Tournament tournament : competitorTournaments) {
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

    public List<TournamentTableDTO> getTournamentsByClub(String clubName) {
        Club club = getClubByName(clubName);
        List<TournamentTableDTO> tournaments = new LinkedList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for (Tournament tournament : tournamentRepository.findByClubAsOrganizer(club)) {
            TournamentTableDTO dto = new TournamentTableDTO();
            dto.setId(tournament.getId());
            dto.setName(tournament.getName());
            dto.setDate(format.format(tournament.getStartDate()));
            dto.setEndDate(format.format(tournament.getEndDate()));
            dto.setLocation(tournament.getLocation());
            tournaments.add(dto);
        }
        return tournaments;
    }    public List<TournamentTableDTO> getTournamentsByGuest(String clubName) {
        Club club = getClubByName(clubName);
        List<TournamentTableDTO> tournaments = new LinkedList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for (Tournament tournament : tournamentRepository.findByClubAsMember(club)) {
            TournamentTableDTO dto = new TournamentTableDTO();
            dto.setId(tournament.getId());
            dto.setName(tournament.getName());
            dto.setDate(format.format(tournament.getStartDate()));
            dto.setEndDate(format.format(tournament.getEndDate()));
            dto.setLocation(tournament.getLocation());
            tournaments.add(dto);
        }
        return tournaments;
    }

    public List<ClubDTO> getClubs() {
        List<ClubDTO> clubs = new LinkedList<>();
        for (Club club : clubRepository.findAll().stream().filter(club -> !club.getDisabled()).toList()) {
            ClubDTO dto = new ClubDTO();
            dto.setId(club.getId());
            dto.setUsername(club.getUsername());
            dto.setAdmin(club.isAdmin());
            clubs.add(dto);
        }
        return clubs;
    }

    public Competitor getCompetitorById(Long id) throws ObjectNotFoundException {
        if (competitorRepository.findById(id).isPresent())
            return competitorRepository.findById(id).get();
        throw new ObjectNotFoundException("Competitor doesn't exist");
    }

    public RefereeDTO getRefereeDTOById(Long id) throws ObjectNotFoundException {
        RefereeDTO dto = new RefereeDTO();
        Optional<Referee> referee = refereeRepository.findById(id);
        if (referee.isPresent()) {
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
        } else {
            throw new ObjectNotFoundException("Referee not found");
        }
    }

    public CompetitorTableDTO getCompetitorDTOById(Long id) throws ObjectNotFoundException {
        CompetitorTableDTO dto = new CompetitorTableDTO();
        Optional<Competitor> competitor = competitorRepository.findById(id);
        if (competitor.isPresent()) {
            dto.setId(competitor.get().getId());
            dto.setFirstname(competitor.get().getFirstName());
            dto.setLastname(competitor.get().getLastName());
            dto.setTournamentIds(new HashSet<>());
            List<Tournament> competitorTournaments = new ArrayList<>();
            for (Long tid : competitor.get().getTournamentIds()){
                competitorTournaments.add(tournamentRepository.findById(tid).orElseThrow());
            }

            for (Tournament tournament : competitorTournaments) {
                dto.getTournamentIds().add(tournament.getId());
            }
            dto.setUserName(competitor.get().getUsername());
            dto.setBelt(competitor.get().getBelt());
            dto.setWeight(competitor.get().getWeight());
            return dto;
        } else {
            throw new ObjectNotFoundException("Competitor not found");
        }
    }

    public ClubDTO getClubDTOById(Long clubId) throws ObjectNotFoundException {
        ClubDTO dto = new ClubDTO();
        Optional<Club> club = clubRepository.findById(clubId);
        if (club.isPresent()) {
            dto.setId(club.get().getId());
            dto.setUsername(club.get().getUsername());
            return dto;
        } else {
            throw new ObjectNotFoundException("Club not found");
        }
    }
}
