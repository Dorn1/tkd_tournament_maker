package pl.tkd.tournaments.tkd_tournament_maker.Club.Club;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Belt;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.CompetitorRepository;
import pl.tkd.tournaments.tkd_tournament_maker.Club.Competitor.Sex;
import pl.tkd.tournaments.tkd_tournament_maker.exceptions.ObjectNotFoundException;

import java.util.Date;
import java.util.List;

@Service
public class ClubService {
    private final ClubRepository clubRepository;
    private final CompetitorRepository competitorRepository;

    @Autowired
    public ClubService(ClubRepository clubRepository, CompetitorRepository competitorRepository) {
        this.clubRepository = clubRepository;
        this.competitorRepository = competitorRepository;
    }

    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    public void addClub(String name) {
        Club c = new Club();
        c.setName(name);
        clubRepository.save(c);
    }
    public void addCompetitorToClub(String firstname,
                                    String lastName,
                                    boolean male,
                                    Long birthDate,
                                    Long clubId) throws ObjectNotFoundException {
        Sex competitorSex = Sex.Female;
        if(male) competitorSex = Sex.Male;

        if(clubRepository.findById(clubId).isPresent()){
            Club club = clubRepository.findById(clubId).get();
            Competitor newCompetitor = new Competitor();
            newCompetitor.setFirstName(firstname);
            newCompetitor.setLastName(lastName);
            newCompetitor.setSex(competitorSex);
            newCompetitor.setBirthDate(new Date(birthDate));
            newCompetitor.setClub(club);
            club.getCompetitors().add(newCompetitor);
            competitorRepository.save(newCompetitor);
            clubRepository.save(club);
        }
        else
            throw new ObjectNotFoundException("Club doesn't exist");


    }

    public Club getClub(Long id) throws ObjectNotFoundException {
        if (clubRepository.findById(id).isPresent()) {
            return clubRepository.findById(id).get();
        }
        throw new ObjectNotFoundException("Club doesn't exist");
    }
}
