package info.ashutosh.utility;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class SessionUtility {

    // Method to create a session and store user-related attributes on login
    public static HttpSession createSession(HttpServletRequest request, HttpServletResponse response, String userId, String username) {
        // Retrieve the session (create a new one if not already present)
        HttpSession session = request.getSession(true);  // true creates a new session if one doesn't exist

        // Store user-related attributes in the session
        session.setAttribute("userId", userId);
        session.setAttribute("username", username);
        session.setAttribute("myAttribute", "loggedIn");  // Example session attribute to mark login
        session.setMaxInactiveInterval(30 * 60);  // Optional: set session timeout to 30 minutes

        // Add the session cookie to the response to ensure it's sent to the client
        Cookie sessionCookie = new Cookie("sessionId", session.getId());
        sessionCookie.setHttpOnly(true);  // Set HttpOnly flag for security
        sessionCookie.setPath("/");  // Ensure the cookie is valid for the whole domain
        sessionCookie.setMaxAge(30 * 60);  // Optional: set cookie expiration (same as session timeout)
        response.addCookie(sessionCookie);

        return session;  // Return session in case the caller needs it
    }

    // Method to destroy the session on logout
    public static void destroySession(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);  // false to avoid creating a new session if none exists
        if (session != null) {
            // Invalidate the session to remove all attributes and terminate it
            session.invalidate();

            // Destroy the session cookie by setting its max age to 0
            Cookie sessionCookie = new Cookie("sessionId", null);  // Invalidate the session cookie
            sessionCookie.setHttpOnly(true);  // Ensure the cookie is secure
            sessionCookie.setPath("/");  // Ensure the cookie is valid across the entire domain
            sessionCookie.setMaxAge(0);  // Expire the cookie immediately
            response.addCookie(sessionCookie);  // Add the expired cookie to the response
        }
    }

    // Method to check if the session is valid
    public static boolean isSessionValid(HttpServletRequest request) {
        HttpSession session = request.getSession(false);  // false to avoid creating a new session if none exists
        if (session == null) {
            return false;  // No session found, session is invalid
        }

        String requestedSessionId = request.getRequestedSessionId();
        if (requestedSessionId != null && requestedSessionId.equals(session.getId())) {
            return true;  // Session is valid
        }

        return false;  // Session is invalid
    }
}
