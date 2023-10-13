package jpcompany.smartwire.security.session.provider;

import jpcompany.smartwire.security.common.PrincipalDetails;
import jpcompany.smartwire.security.jwt.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class WebAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        PrincipalDetails principalDetails = (PrincipalDetails) userDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, principalDetails.getMember().getLoginPassword())) {
            throw new BadCredentialsException("BadCredentialsException");
        }

        return new JwtAuthenticationToken(principalDetails.getMember(), null, principalDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}