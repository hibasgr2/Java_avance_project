package reservation.reservation.util;


import reservation.reservation.model.Utilisateur;

public class SessionCon {
    private static Utilisateur currentUser;

    public static void setUser(Utilisateur user) {
        currentUser = user;
    }

    public static Utilisateur getUser() {
        return currentUser;
    }
}