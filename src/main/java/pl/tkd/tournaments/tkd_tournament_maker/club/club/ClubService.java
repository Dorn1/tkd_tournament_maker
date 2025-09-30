package pl.tkd.tournaments.tkd_tournament_maker.club.club;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.CompetitorRepository;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Sex;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.Referee;
import pl.tkd.tournaments.tkd_tournament_maker.club.referee.RefereeRepository;
import pl.tkd.tournaments.tkd_tournament_maker.club.user.UserRepository;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.ObjectNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final CompetitorRepository competitorRepository;
    private final RefereeRepository refereeRepository;



    public void addCompetitorToClub(String firstname,
                                    String lastName,
                                    boolean male,
                                    Long birthYear,
                                    Long clubId) throws ObjectNotFoundException {
        Sex competitorSex = Sex.Female;
        if (male) competitorSex = Sex.Male;

        Club club = getClubById(clubId);
        Competitor newCompetitor = new Competitor();
        newCompetitor.setFirstName(firstname);
        newCompetitor.setLastName(lastName);
        newCompetitor.setSex(competitorSex);
        newCompetitor.setBirthYear(birthYear);
        newCompetitor.setClub(club);
        newCompetitor.setBelt(1);
        club.getCompetitors().add(newCompetitor);
        competitorRepository.save(newCompetitor);
        clubRepository.save(club);
    }

    public void addRefereeToClub(String firstname,
                                 String lastName,
                                 Long clubId) throws ObjectNotFoundException {

        Club club = getClubById(clubId);
        Referee newReferee = new Referee();
        newReferee.setFirstName(firstname);
        newReferee.setLastName(lastName);
        newReferee.setClub(club);
        refereeRepository.save(newReferee);
        clubRepository.save(club);


    }

    public Club getClubById(Long id) throws ObjectNotFoundException {
        if (clubRepository.findById(id).isPresent())
            return clubRepository.findById(id).get();
        throw new ObjectNotFoundException("Club doesn't exist");
    }

    public Club getClubByName(String name) {
        return clubRepository.findByUserName(name);
    }

    public List<Referee> getRefereeByName(String firstname, String lastname) {
        return refereeRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(firstname, lastname);
    }

    public Competitor getCompetitorById(Long id) throws ObjectNotFoundException {
        if (competitorRepository.findById(id).isPresent())
            return competitorRepository.findById(id).get();
        throw new ObjectNotFoundException("Competitor doesn't exist");
    }
}
