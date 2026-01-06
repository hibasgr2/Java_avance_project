package reservation.reservation.model;

public class AffectationDTO {
    private String immeubleAdresse;
    private int etageId;
    private String respoNom;

    public AffectationDTO(String immeubleAdresse, int etageId, String respoNom) {
        this.immeubleAdresse = immeubleAdresse;
        this.etageId = etageId;
        this.respoNom = respoNom;
    }

    // Getters et Setters
    public String getImmeubleAdresse() { return immeubleAdresse; }
    public void setImmeubleAdresse(String immeubleAdresse) { this.immeubleAdresse = immeubleAdresse; }

    public int getEtageId() { return etageId; }
    public void setEtageId(int etageId) { this.etageId = etageId; }

    public String getRespoNom() { return respoNom; }
    public void setRespoNom(String respoNom) { this.respoNom = respoNom; }
}