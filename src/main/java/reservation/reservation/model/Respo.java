package reservation.reservation.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "respo")
@PrimaryKeyJoinColumn(name = "utilisateur_id")
public class Respo extends Utilisateur {



    @Column(name = "actif", nullable = false)
    private boolean actif = true;

    @OneToMany(mappedBy = "respo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Salle> salles = new ArrayList<>();

    @OneToMany(mappedBy = "respo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "sup_user_id")
    private SupUser supUser;


    public Respo() {
        super();
        this.actif = true;
    }


    public Respo(String nomComplet, String tel, String email,String password, boolean actif) {
        super(nomComplet, tel, email, password,Role.RESPO);
        this.actif = actif;
    }


    @Override
    public Role getRole() {
        return Role.RESPO;  // Rôle fixe pour Respo
    }



    // Getters et Setters
    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public List<Salle> getSalles() {
        return salles;
    }

    public void setSalles(List<Salle> salles) {
        this.salles = salles;
    }

    // Méthodes utilitaires
    public void addSalle(Salle salle) {
        if (salle != null && !salles.contains(salle)) {
            salles.add(salle);
            salle.setRespo(this);
        }
    }

    public void removeSalle(Salle salle) {
        if (salle != null && salles.contains(salle)) {
            salles.remove(salle);
            salle.setRespo(null);
        }
    }



    public void activer() {
        this.actif = true;
    }

    public void desactiver() {
        this.actif = false;
    }


    public boolean peutGererSalles() {
        return this.actif;
    }


    public int getNombreSalles() {
        return salles.size();
    }



}