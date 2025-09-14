package pl.tkd.tournaments.tkd_tournament_maker.security.authentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.tkd.tournaments.tkd_tournament_maker.club.club.Club;
import pl.tkd.tournaments.tkd_tournament_maker.club.club.ClubRepository;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Competitor;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.CompetitorRepository;
import pl.tkd.tournaments.tkd_tournament_maker.club.competitor.Sex;
import pl.tkd.tournaments.tkd_tournament_maker.club.user.Role;
import pl.tkd.tournaments.tkd_tournament_maker.club.user.User;
import pl.tkd.tournaments.tkd_tournament_maker.club.user.UserRepository;
import pl.tkd.tournaments.tkd_tournament_maker.security.authentication.jwt.JwtService;

import java.util.InputMismatchException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final CompetitorRepository competitorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private static final String ROLE = "role";
    private static final String NAME = "name";
    private static final String LASTNAME = "lastName";
    private static final String BELT = "belt";
    private static final String SEX = "sex";
    private static final String BIRTHYEAR = "birthYear";
    private static final String WEIGHT = "weight";
    private static final String CLUBID = "clubId";

    public AuthenticationResponse register(RegisterRequest request) {
        try {
            Map<String, String> variables = request.getVariables();
            String role = variables.get(ROLE);
            switch (role) {
                case "CLUB":
                    Club clubUser = Club.builder()
                            .email(request.getEmail())
                            .password(passwordEncoder.encode(request.getPassword()))
                            .role(Role.valueOf(role))
                            .build();
                    Club club = Club.builder()
                            .email(request.getEmail())
                            .password(passwordEncoder.encode(request.getPassword()))
                            .role(Role.valueOf(role))
                            .admin(false)
                            .name(variables.get(NAME))
                            .build();
                    clubRepository.save(club);
                    String clubToken = jwtService.generateToken(clubUser);
                    return AuthenticationResponse
                            .builder()
                            .token(clubToken)
                            .build();
                case "COMPETITOR":
                    Competitor competitorUser = Competitor.builder()
                            .email(request.getEmail())
                            .password(passwordEncoder.encode(request.getPassword()))
                            .role(Role.valueOf(role))
                            .build();
                    Club competitorClub = clubRepository.findById(Long.valueOf(variables.get(CLUBID)))
                            .orElseThrow(() -> new InputMismatchException("provided non existing Club"));
                    Competitor competitor = Competitor.builder()
                            .email(request.getEmail())
                            .password(passwordEncoder.encode(request.getPassword()))
                            .role(Role.valueOf(role))
                            .belt(Integer.valueOf(variables.get(BELT)))
                            .sex(Sex.valueOf(variables.get(SEX)))
                            .birthYear(Long.valueOf(variables.get(BIRTHYEAR)))
                            .firstName(variables.get(NAME))
                            .lastName(variables.get(LASTNAME))
                            .weight(Double.valueOf(variables.get(WEIGHT)))
                            .club(competitorClub)
                            .build();
                    competitorRepository.save(competitor);
                    String competitorToken = jwtService.generateToken(competitorUser);
                    return AuthenticationResponse
                            .builder()
                            .token(competitorToken)
                            .success(true)
                            .build();

                default:
                    throw new IllegalArgumentException("Invalid role");
            }
        }
        catch (Exception e){
            log.info(e.getMessage());
            return AuthenticationResponse.builder().success(false).build();
        }


    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        User user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }
}
