package pl.tkd.tournaments.tkd_tournament_maker.security.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.tkd.tournaments.tkd_tournament_maker.security.authentication.dto.AuthenticationRequest;
import pl.tkd.tournaments.tkd_tournament_maker.security.authentication.dto.AuthenticationResponse;
import pl.tkd.tournaments.tkd_tournament_maker.security.authentication.dto.RegisterRequest;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest authenticationRequest
    ) {
        AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);
        if(response.getToken() == null) {
            return ResponseEntity.status(401).body(response);
        }
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }

    @GetMapping("/userId")
    public ResponseEntity<Long> getUserId(
            @RequestParam String userName
    ){
        return ResponseEntity.ok(authenticationService.getUserId(userName));
    }
    @GetMapping("/userType")
    public ResponseEntity<String> getUserType(
            @RequestParam String userName
    ){
        return ResponseEntity.ok(authenticationService.getUserType(userName));
    }
}
