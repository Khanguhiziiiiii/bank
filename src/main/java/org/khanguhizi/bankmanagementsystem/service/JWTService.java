package org.khanguhizi.bankmanagementsystem.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    /*
    These read values from your application.properties file, for example:
        security.jwt.secret-key=your_secret_key_here
        security.jwt.expiration-time=3600000
        secretKey: used to sign and verify JWTs.
        jwtExpiration: defines how long a token is valid (e.g., 1 hour in milliseconds).
     */

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /*
        Gets the username (stored as the “subject” in JWT) from a token.
        Delegates the actual extraction to extractClaim.
     */

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /*
        A generic method that extracts any specific claim from the token.
            Gets all claims using extractAllClaims.
            Applies a function (like Claims::getSubject or Claims::getExpiration) to get a specific value.
     */

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

                /*
                    This is where we store extra information (metadata) that will be embedded inside the JWT payload — for example, user’s role, username, email, etc.
                    These are called claims in JWT terminology.
                 */

        if (!userDetails.getAuthorities().isEmpty()) {
            claims.put("role", userDetails.getAuthorities().iterator().next().getAuthority());
        } else {
            claims.put("role", "ROLE_USER");
        }

            /*
                userDetails.getAuthorities() returns the list of roles or permissions assigned to the user.
                .iterator().next() gets the first one (since many apps have only one role per user).
                If the user has no role, it defaults to "ROLE_USER".
                    Example:
                    If the user is an admin → "ROLE_ADMIN"
                    If no role is found → default "ROLE_USER"
             */

        return generateToken(claims, userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /*
        Allows including extra information in the token (like roles, userId, etc.).
        Calls buildToken to actually create it.
     */

    public long getExpirationTime() {
        return jwtExpiration;
    }

    /*
        Simple getter for the expiration time configuration.
     */


    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    /*
    Creates the actual JWT token using the builder pattern:
        .claims(extraClaims): Add any custom data.
        .subject(...): Set username as the token subject.
        .issuedAt(...): The time the token was created.
        .expiration(...): Expiration time based on the configured duration.
        .signWith(...): Signs the token using the secret key.
        .compact(): Converts everything into a final encoded JWT string.
     */

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }


    /*
        Checks:
            The username in the token matches the one in the user details.
            The token has not expired.
     */

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /*
        Extracts the expiration date and checks if it’s before the current time (meaning it’s expired).
     */

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /*
        Uses the generic extractClaim method to get the expiration date.
     */

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    /*
        Parses the entire token and returns the Claims (the payload data).
        Uses the signing key to ensure the token hasn’t been tampered with.
     */

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /*
        Converts the Base64-encoded secret key from the config file into a cryptographic Key object.
        Used to sign and verify tokens.
     */

}
