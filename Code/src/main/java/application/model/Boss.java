package application.model;

public class Boss {
    private final String nom;
    private String view;
    private float PV;
    private boolean vaincu;

    public Boss(String nom, String view, float PV) {
            this.nom = nom;
            this.view = view;
            this.PV=PV;
            this.vaincu=false;
        }

        public String getNom() {
            return nom;
        }

        public String getView() {
        return view;
    }

    public float getPV() {
        return PV;
    }

    public void setPV(float PV) {
        this.PV = PV;
    }

    public boolean isVaincu() {
        return vaincu;
    }

    public void setVaincu(boolean vaincu) {
        this.vaincu = vaincu;
    }

}
