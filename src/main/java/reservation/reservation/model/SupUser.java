package reservation.reservation.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sup_user")
@PrimaryKeyJoinColumn(name = "utilisateur_id")
public class SupUser extends Utilisateur {

    @Id
    @Column(name = "utilisateur_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "supUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Salle> salles = new ArrayList<>();

    @OneToMany(mappedBy = "supUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "supUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Respo> respos = new ArrayList<>();

    public SupUser() {
        super();
    }


    public SupUser(String nomComplet, String tel, String email, String password) {
        super(nomComplet, tel, email, password, Role.SUPERUSER);
    }


    @Override
    public Role getRole() {
        return Role.SUPERUSER;  // Rôle fixe pour SuperUser
    }

    // Getters et Setters
    public List<Salle> getSalles() {
        return salles;
    }

    public void setSalles(List<Salle> salles) {
        this.salles = salles;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Respo> getRespos() {
        return respos;
    }

    public void setRespos(List<Respo> respos) {
        this.respos = respos;
    }


    public void addRespo(Respo respo) {
        if (respo != null && !respos.contains(respo)) {
            respos.add(respo);

        }
    }

    public void removeRespo(Respo respo) {
        if (respo != null && respos.contains(respo)) {
            respos.remove(respo);

        }
    }


    public int getNombreSallesGerées() {
        return salles.size();
    }

    public int getNombreResposSupervises() {
        return respos.size();
    }

    public int getNombreReservationsGerées() {
        return reservations.size();
    }
}