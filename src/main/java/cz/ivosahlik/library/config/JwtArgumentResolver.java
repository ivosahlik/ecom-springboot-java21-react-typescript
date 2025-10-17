package cz.ivosahlik.library.config;

import cz.ivosahlik.library.annotation.CurrentUser;
import cz.ivosahlik.library.annotation.UserType;
import cz.ivosahlik.library.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtArgumentResolver implements HandlerMethodArgumentResolver {

    private final JWTUtils jwtUtils;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class) ||
               parameter.hasParameterAnnotation(UserType.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                 ModelAndViewContainer mavContainer,
                                 NativeWebRequest webRequest,
                                 WebDataBinderFactory binderFactory) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.warn("No authentication found in security context");
            return null;
        }

        String username = authentication.getName();

        if (parameter.hasParameterAnnotation(CurrentUser.class)) {
            log.debug("Resolved current user: {}", username);
            return username;
        }

        if (parameter.hasParameterAnnotation(UserType.class)) {
            // Get the JWT token from Principal
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
                // Extract userType from authorities
                String userType = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(authority -> authority.getAuthority().replace("ROLE_", "").toLowerCase())
                    .orElse("user");

                log.debug("Resolved user type: {}", userType);
                return userType;
            }

            log.warn("Unable to determine user type from principal");
            return null;
        }

        return null;
    }
}
