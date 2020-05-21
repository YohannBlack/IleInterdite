package modele;

public class Zone {
    private Modele modele;
    protected Etat etat;
    public boolean occupee;
    public Joueur j;
    private final int x, y;

    public Zone(Modele m, int i, int j){
        this.modele = m;
        this.x = i;
        this.y = j;
        this.etat = Etat.Normale;
        this.occupee = false;
    }

    private Etat etatSuivant;

    public void putJoueur(Joueur j){
        this.j = j;
        this.occupee = true;
    }

    public boolean joueurOn(){ return this.occupee; }

    /**Methode qui inonde une zone. Lorsqu'elle est appelée, si la zone est normale elle sera inondée au tour
     * suivant, si elle était déjà inondée, elle sera submergée au tour suivant. Si la zone est submergée, il
     * ne se passe rien. **/
    public void inonde() {
        if (this.etat == Etat.Inondee) this.etatSuivant = Etat.Submergee ;
        else if (this.etat == Etat.Normale) this.etatSuivant = Etat.Inondee;
    }

    /** Evalue l'etat suivant de la zone, selon les differntes mécaniques de jeux :
     * 1- A la fin de chaque tour, trois zones choisies aléatoirement parmis celles qui ne sont pas encore submergées
     * sont inondées**/
    protected void evalue(Zone s1, Zone s2, Zone s3) {
        if ((this == s1) || (this == s2) || (this == s3)) inonde();
        else this.etatSuivant = etat;
    }

    protected void evolue() {
        this.etat = this.etatSuivant;
    }

    public Etat getEtat() {
        return this.etat;
    }
    /** METHODES D'ENCAPSULATION**/

    public Etat getSuiv() {
        return this.etatSuivant;
    }

    public int getX() {return this.x;}
    public int getY() {return this.y;}
}
