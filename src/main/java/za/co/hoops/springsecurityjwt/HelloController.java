package za.co.hoops.springsecurityjwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import za.co.hoops.springsecurityjwt.model.AuthenticationResponse;
import za.co.hoops.springsecurityjwt.service.MyUserDetailsService;
import za.co.hoops.springsecurityjwt.util.JwtUtil;
import za.co.hoops.springsecurityjwt.model.AuthenticationRequest;

@RestController
public class HelloController {
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtTokenUtil; 
	
	@GetMapping(value = "/hello")
	public String hello() {
		return "Hello Earthlings";
	}
	
	@PostMapping(value = "/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody(required = true) AuthenticationRequest request) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
				);
		}catch (BadCredentialsException e) {
			throw new Exception("Incorrect username and password", e);
		}
		
		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(request.getUsername());
		
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
		
	}
}
