package reservation.reservation.service;

import reservation.reservation.dao.ReservationDAO;
import reservation.reservation.model.*;

import java.time.LocalDateTime;

public class ReservationService {

    private final ReservationDAO reservationDAO = new ReservationDAO();

    public Reservation createReservation(Utilisateur client, Salle salle, LocalDateTime reservationDate, String desc) throws Exception {

        if (((Client)client).getSolde()< salle.getPrix()) {
            throw new Exception("Solde insuffisant pour effectuer cette réservation.");
        }

        // La vérification de conflit est déjà faite par la recherche de salles disponibles.
        // On pourrait ajouter une double-vérification ici si nécessaire.

        Reservation reservation = new Reservation();
        reservation.setClient(((Client)client));
        reservation.setSalle(salle);
        reservation.setDateReservation(reservationDate);
        reservation.setDescription(desc);
        reservation.setTotal(salle.getPrix());
        reservation.setEtat(EtatReservation.EN_ATTENTE);
        // Associer au responsable de la salle pour la validation
        if (salle.getRespo() != null) {
            reservation.setRespo(salle.getRespo());
        }

        reservationDAO.save(reservation);
        return reservation;
    }

    public void cancelClientReservation(int reservationId, Client client) throws Exception {
        Reservation reservation = reservationDAO.findById(reservationId);

        if (reservation == null) {
            throw new Exception("Réservation non trouvée.");
        }
        if (!reservation.getClient().getId().equals(client.getId())) {
            throw new Exception("Vous n'êtes pas autorisé à annuler cette réservation.");
        }
        if (reservation.getEtat() != EtatReservation.EN_ATTENTE) {
            throw new Exception("Cette réservation ne peut plus être annulée. Contactez le responsable.");
        }

        reservation.setEtat(EtatReservation.ANNULEE);
        reservationDAO.update(reservation);
    }
}
