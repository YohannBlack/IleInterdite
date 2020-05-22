package modele;

import obsv.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Modele extends Observable {

    public static final int HAUTEUR=10, LARGEUR=10;
    public Zone[][] zones;
    public final int JOUEURS = 4;
    private Joueur[] joueurs; //Tableau de joueurs.
    private Joueur joueurActuel; //Joueur dont c'est le tour.
    private Action SELECTED_ACTION = Action.NONE;
    private Zone[] zoneInit;

    private Random randomGenerator = new Random();

    public Modele(){
        zones = new Zone[LARGEUR+2][HAUTEUR+2];
        for(int i=0; i<LARGEUR+2; i++) {
            for(int j=0; j<HAUTEUR+2; j++) {
                zones[i][j] = new Zone(this,i, j);
            }
        }
        init();

        zoneInit = new Zone[4];
        setZoneInit();

        joueurs = new Joueur[JOUEURS];
        for (int i= 0; i<JOUEURS; i++) {
            joueurs[i] = new Joueur(this);
        }
        tourJoueur(0); //On initialise le d�but de la partie en commen�ant par le premier joueur.
    }


    private void init() {
        for (Zone[] c : this.zones) {
            for (Zone z : c) {
                z.etat = Etat.Normale;
            }
        }
    }

    private void setZoneInit(){
        zoneInit[0] = this.getZone(2, 2);
        zoneInit[1] = this.getZone(9, 9);
        zoneInit[2] = this.getZone(2, 9);
        zoneInit[3] = this.getZone(9, 2);
    }


    /**Methode qui renvoie la liste de zones de l'îles qui ne sont pas encore submergées**/
    private ArrayList<Zone> nonSub() {
        ArrayList<Zone> res = new ArrayList<>();
        for(Zone[] c : this.zones) {
            for(Zone z : c) {
                if (! (z.etat == Etat.Submergee)) res.add(z);
            }
        }
        return res;
    }

    /** Methode qui renvoie, parmis les zones de l'île qui ne sont pas submergées,
     * une zone choisie aléatoirement**/
    private Zone zoneAleatoire() {
        ArrayList<Zone> pickable = nonSub();
        int rand = randomGenerator.nextInt(pickable.size());
        return pickable.get(rand);
    }

    public void setSelectAction(Action action){
        this.SELECTED_ACTION = action;
    }

    public void tourSuivant() {
        //On commence par choisir aléatoirement trois zones de l'île parmis les zones qui ne sont pas submergées
        Zone s1 = zoneAleatoire();
        Zone s2 = zoneAleatoire();
        Zone s3 = zoneAleatoire();

        for(Zone[] c : this.zones) {
            for(Zone z : c) {
                // Pour chacune des zones de l'île, on appelle sa méthode evalue, qui calcule son prochain état.
                // On précise à la méthode quelles sont les 3 zones qui ont été choisies aléatoirement pour
                // être inondée.**/
                z.evalue(s1, s2, s3);
            }
        }

        for(Zone[] c : this.zones) { //On fait evoluer chacune des zones.
            for(Zone z : c) {
                z.evolue();
            }
        }
        // On notifie les observateurs, afin que la vue soit mise à
        notifyObservers();

        tourJoueur((joueurActuel.getNumJoueur() + 1)%4);
    }

    public void tourJoueur(int i){
        joueurActuel = joueurs[i];
        joueurActuel.debutTour();
    }

    public void actionJoueur(Zone cible){
        System.out.print("\n Emplacement joueur " + joueurActuel.getZone().getX() + " " + joueurActuel.getZone().getY());
        System.out.print("\n Zone cible : " + cible.getX() + " " + cible.getY());
        if (joueurActuel.getActionsRestantes() != 0) {
            switch(SELECTED_ACTION){
                case DEPLACEMENT :
                    if (joueurActuel.getZone().trouveAdjacentes().contains(cible) && cible.etat != Etat.Submergee) {
                        joueurActuel.deplaceJoueur(cible);
                        joueurActuel.decrementeAction();
                    } else
                        System.out.println("Déplacement Invalide");
                    break;
                case ASSECHER :
                    if (joueurActuel.getZone().trouveAdjacentes().contains(cible) && cible.etat == Etat.Inondee) {
                        cible.asseche();
                        joueurActuel.decrementeAction();
                    } else
                        System.out.println("Cette zone ne peut être assechée");
                    break;
                default: System.out.print("Selectionnez une action valide");
            }
        } else System.out.print(" Vous ne pouvez plus effectuer d'action.");
        System.out.print("\n Emplacement nouveau " + joueurActuel.getZone().getX()+ " " + joueurActuel.getZone().getY() + "\n");
    }

    /** Fin de partie lorsque la liste des cases non submergées est vide**/
    public boolean FinPartie(){
        ArrayList<Zone> nonSub = nonSub();
        return nonSub.isEmpty();
    }

    public Zone getZoneInit(int index){ return zoneInit[index]; }

    public Zone getZone(int i, int j) {
        return zones[i][j];
    }
}
