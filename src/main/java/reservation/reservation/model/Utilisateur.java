package reservation.reservation.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "utilisateur")
@Inheritance(strategy = InheritanceType.JOINED)  // Stratégie JOINED
public abstract class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_complet", nullable = false)
    private String nomComplet;

    private String tel;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;



    public Utilisateur() {
        this.dateCreation = LocalDateTime.now();
    }

    public Utilisateur(String nomComplet, String tel, String email,
                       String password,Role role) {
        this.nomComplet = nomComplet;
        this.tel = tel;
        this.email = email;
        this.password = password;
        this.dateCreation = LocalDateTime.now();
        this.role = role;
    }


    public abstract Role getRole();

    // Getters & Setters
    public Long getId() { return id; }

    public String getNomComplet() { return nomComplet; }
    public void setNomComplet(String nomComplet) { this.nomComplet = nomComplet; }

    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDateTime getDateCreation() { return dateCreation; }
}