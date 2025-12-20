package reservation.reservation.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "etage")
public class Etage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "respo_id", referencedColumnName = "utilisateur_id")
    private Respo respo;

    @OneToMany(mappedBy = "etage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Salle> salles = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "immeuble_id")
    private Immeuble immeuble;

    // Constructeurs
    public Etage() {}

    public Etage(Respo respo) {
        this.respo = respo;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Respo getRespo() { return respo; }
    public void setRespo(Respo respo) { this.respo = respo; }

    public List<Salle> getSalles() { return salles; }
    public void setSalles(List<Salle> salles) { this.salles = salles; }
}
