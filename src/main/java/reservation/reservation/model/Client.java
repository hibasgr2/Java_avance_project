package reservation.reservation.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client")
@PrimaryKeyJoinColumn(name = "utilisateur_id")
public class Client extends Utilisateur {

    @Id
    @Column(name = "utilisateur_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @Column(name = "solde")
    private double solde;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<>();

    public Client() {
        super();
        this.solde = 0.0;
    }


    public Client(String nomComplet, String tel, String email, String password, double solde) {
        super(nomComplet, tel, email, password,Role.CLIENT);
        this.solde = solde;
    }

    // Implémentation de la méthode abstraite getRole()
    @Override
    public Role getRole() {
        return Role.CLIENT;
    }

    // Méthodes spécifiques
    public void crediterSolde(double montant) {
        if (montant > 0) {
            this.solde += montant;
        }
    }

    public boolean debiterSolde(double montant) {
        if (montant > 0 && this.solde >= montant) {
            this.solde -= montant;
            return true;
        }
        return false;
    }

    // Getters et Setters
    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void addReservation(Reservation reservation) {
        if (reservation != null && !reservations.contains(reservation)) {
            reservations.add(reservation);
            reservation.setClient(this);
        }
    }

    public void removeReservation(Reservation reservation) {
        if (reservation != null && reservations.contains(reservation)) {
            reservations.remove(reservation);
            reservation.setClient(null);
        }
    }
}