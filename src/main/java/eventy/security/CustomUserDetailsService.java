package eventy.security;

import eventy.services.interfaces.UserDataService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserDataService userDataService;

    public CustomUserDetailsService(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDataService.getByEmail(username).fold((ex) -> {
            throw new UsernameNotFoundException(ex.getMessage());
        }, (user) -> new CustomUserDetails(user));
    }
}
