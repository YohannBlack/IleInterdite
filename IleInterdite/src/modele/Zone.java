package modele;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Zone {
    private Modele modele;
    protected Etat etat;
    private final int x, y;
    private boolean occupee;
    private List<Joueur> joueursOn;

    public Zone(Modele m, int i, int j){
        this.modele = m;
        this.x = i;
        this.y = j;
        this.etat = Etat.Normale;
        this.joueursOn = new ArrayList<>();
        this.occupee = false;
    }

    public void putJoueur(Joueur j){
        this.joueursOn.add(j);
        this.occupee = true;
    }

    public void removeJoueur(Joueur j){
        if(joueursOn.contains(j)){
            joueursOn.remove(j);
            if(joueursOn.isEmpty()) occupee = false;
        }
    }

    public boolean hasJoueurOn(){ return this.occupee; }

    private Etat etatSuivant;

    /**Methode qui inonde une zone. Lorsqu'elle est appelée, si la zone est normale elle sera inondée au tour
     * suivant, si elle était déjà inondée, elle sera submergée au tour suivant. Si la zone est submergée, il
     * ne se passe rien. **/
    public void inonde() {
        if (this.etat == Etat.Inondee) this.etatSuivant = Etat.Submergee ;
        else if (this.etat == Etat.Normale) this.etatSuivant = Etat.Inondee;
    }

    public void asseche(){
        this.etat = Etat.Normale;
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

    protected List<Zone> trouveAdjacentes(){
        return Arrays.asList(
                modele.getZone(x, y - 1),
                modele.getZone(x - 1, y),
                modele.getZone(x + 1, y),
                modele.getZone(x, y + 1)
        );
    }

    public Etat getEtat() {
        return this.etat;
    }
    /** METHODES D'ENCAPSULATION**/

    public Etat getSuiv() {
        return this.etatSuivant;
    }

    public Joueur[] getJoueur(){
        Joueur[] res = new Joueur[joueursOn.size()];
        for (int i = 0; i<joueursOn.size(); i++){
            res[i] = joueursOn.get(i);
        }
        return res;
    }

    public int getX() {return this.x;}
    public int getY() {return this.y;}
}
