package reservation.reservation.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "salle")
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "capacite", nullable = false)
    private int capacite;

    @Column(name = "dispo", nullable = false)
    private boolean dispo;

    @Column(name = "type_salle", nullable = false, length = 100)
    private String typeSalle;

    @Column(name = "num_etage", nullable = false)
    private int numEtage;

    @Column(name = "prix", nullable = false)
    private double prix;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "respo_id", referencedColumnName = "utilisateur_id")
    private Respo respo;

    @ManyToOne
    @JoinColumn(name = "etage_id")
    private Etage etage;

    @OneToMany(mappedBy = "salle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "sup_user_id")
    private SupUser supUser;

    // Constructeurs
    public Salle() {}

    public Salle(int capacite, boolean dispo, String typeSalle, int numEtage, double prix) {
        this.capacite = capacite;
        this.dispo = dispo;
        this.typeSalle = typeSalle;
        this.numEtage = numEtage;
        this.prix = prix;
    }



    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }

    public boolean isDispo() { return dispo; }
    public void setDispo(boolean dispo) { this.dispo = dispo; }

    public String getTypeSalle() { return typeSalle; }
    public void setTypeSalle(String typeSalle) { this.typeSalle = typeSalle; }

    public int getNumEtage() { return numEtage; }
    public void setNumEtage(int numEtage) { this.numEtage = numEtage; }

    public Respo getRespo() { return respo; }
    public void setRespo(Respo respo) { this.respo = respo; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public Etage getEtage() { return etage; }
    public void setEtage(Etage etage) { this.etage = etage; }

    public String toString() {
        return typeSalle + " (Capacité : " + capacite + ")";
    }

    public List<Reservation> getReservations() { return reservations; }
    public void setReservations(List<Reservation> reservations) { this.reservations = reservations; }
}