package reservation.reservation.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "immeuble")
public class Immeuble {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "adresse", nullable = false, length = 300)
    private String adresse;



    @OneToMany(mappedBy = "immeuble", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Etage> etages = new ArrayList<>();


    // Constructeurs
    public Immeuble() {}

    public Immeuble(String adresse) {
        this.adresse = adresse;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public List<Etage> getEtages() {
        return etages;
    }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
}