package cz.ivosahlik.library.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
/**
 * @deprecated This class is deprecated and will be removed in future releases.
 * Use {@link cz.ivosahlik.library.annotation.CurrentUser} and {@link cz.ivosahlik.library.annotation.UserType}
 * annotations for JWT claim extraction instead.
 */
@Deprecated
public class ExtractJWT {

    public static String payloadJWTExtraction(String token, String extraction) {
        try {
            if (token == null) {
                log.error("JWT extraction error: Token is null");
                return null;
            }

            token = token.replace("Bearer ", "");
            log.debug("Processing token: {}", token.substring(0, Math.min(10, token.length())) + "...");

            String[] chunks = token.split("\\.");
            if (chunks.length < 3) {
                log.error("JWT extraction error: Invalid token format, expected 3 parts but got {}", chunks.length);
                return null;
            }

            Base64.Decoder decoder = Base64.getUrlDecoder();

            String payload;
            try {
                payload = new String(decoder.decode(chunks[1]));
                log.debug("Decoded payload: {}", payload);
            } catch (IllegalArgumentException e) {
                log.error("JWT extraction error: Could not decode payload: {}", e.getMessage());
                return null;
            }

            // Special handling for "sub" extraction to get username from subject
            if (extraction.equals("\"sub\"")) {
                String result = extractSubject(payload);
                log.debug("Extracted subject: {}", result);
                return result;
            }

            // Special handling for userType extraction
            if (extraction.equals("\"userType\"")) {
                String result = extractUserType(payload);
                log.debug("Extracted userType: {}", result);
                return result;
            }

            String[] entries = payload.split(",");
            Map<String, String> map = new HashMap<>();

            for (String entry : entries) {
                String[] keyValue = entry.split(":");
                if (keyValue.length >= 2 && keyValue[0].equals(extraction)) {
                    int remove = 1;
                    if (keyValue[1].endsWith("}")) {
                        remove = 2;
                    }
                    keyValue[1] = keyValue[1].substring(0, keyValue[1].length() - remove);
                    keyValue[1] = keyValue[1].substring(1);

                    map.put(keyValue[0], keyValue[1]);
                }
            }
            if (!map.containsKey(extraction)) {
                log.error("JWT extraction error: Key {} not found in payload", extraction);
                return null;
            }
            log.debug("Extracted value for {}: {}", extraction, map.get(extraction));
            return map.get(extraction);
        } catch (Exception e) {
            log.error("Error extracting from JWT: {}", e.getMessage(), e);
            return null;
        }
    }

    private static String extractSubject(String payload) {
        // Look for "sub" field in JWT payload
        if (payload.contains("\"sub\"")) {
            int subIndex = payload.indexOf("\"sub\"");
            // Find the value after "sub":
            int valueStart = payload.indexOf(":", subIndex) + 1;
            // Find the end of the value (comma or closing brace)
            int valueEnd;
            if (payload.indexOf(",", valueStart) > 0) {
                valueEnd = payload.indexOf(",", valueStart);
            } else {
                valueEnd = payload.indexOf("}", valueStart);
            }

            if (valueStart > 0 && valueEnd > valueStart) {
                // Extract and clean the value (remove quotes and whitespace)
                String value = payload.substring(valueStart, valueEnd).trim();
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                return value;
            }
        }
        return null;
    }

    private static String extractUserType(String payload) {
        // Look for "userType" field in JWT payload
        if (payload.contains("\"userType\"")) {
            int typeIndex = payload.indexOf("\"userType\"");
            // Find the value after "userType":
            int valueStart = payload.indexOf(":", typeIndex) + 1;
            // Find the end of the value (comma or closing brace)
            int valueEnd;
            if (payload.indexOf(",", valueStart) > 0) {
                valueEnd = payload.indexOf(",", valueStart);
            } else {
                valueEnd = payload.indexOf("}", valueStart);
            }

            if (valueStart > 0 && valueEnd > valueStart) {
                // Extract and clean the value (remove quotes and whitespace)
                String value = payload.substring(valueStart, valueEnd).trim();
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                return value;
            }
        }
        return null;
    }
}
