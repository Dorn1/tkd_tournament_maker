package pl.tkd.tournaments.tkd_tournament_maker.Club.Club;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ClubService {
    private final ClubRepository clubRepository;
    @Autowired
    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }
    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }
    public void addClub(String name) {
        Club c = new Club(name);
        clubRepository.save(c);
    }
}
