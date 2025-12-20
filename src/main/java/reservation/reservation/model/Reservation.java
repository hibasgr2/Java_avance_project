package reservation.reservation.model;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salle_id", nullable = false)
    private Salle salle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id",referencedColumnName = "utilisateur_id", nullable = false)
    private Client client;

    @Column(name = "date_reservation", nullable = false)
    private LocalDateTime dateReservation;

    @Column(name = "etat", nullable = false, length = 50)
    private String etat;

    @Column(name = "total", nullable = false)
    private double total;

    @ManyToOne
    @JoinColumn(name = "sup_user_id")
    private SupUser supUser;

    @ManyToOne
    @JoinColumn(name = "respo_id",referencedColumnName = "utilisateur_id")
    private Respo respo;

    // Constructeurs
    public Reservation() {
        this.dateReservation = LocalDateTime.now();
    }

    public Reservation(Salle salle, Client client, String etat, double total) {
        this.salle = salle;
        this.client = client;
        this.etat = etat;
        this.total = total;
        this.dateReservation = LocalDateTime.now();
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Salle getSalle() { return salle; }
    public void setSalle(Salle salle) { this.salle = salle; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public LocalDateTime getDateReservation() { return dateReservation; }
    public void setDateReservation(LocalDateTime dateReservation) { this.dateReservation = dateReservation; }

    public String getEtat() { return etat; }
    public void setEtat(String etat) { this.etat = etat; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}