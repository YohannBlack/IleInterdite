package modele;

import obsv.Observable;

import java.util.List;

public class Joueur extends Observable {
    private Modele modele;
    private Zone zoneCourante; //La zone dans laquelle se trouve le joueur
    private int actionsRestantes = 3; //Le nombre d'actions restantes au joueur lors de son tour, initialisé à 3.
    private final int numJoueur; //Numéros de joueur, pour l'identifier.
    private static int nbJoueurs = 0; // Le nombre total de joueurs dans la partie.

    public Joueur(Modele modele) {
        this.modele = modele;
        numJoueur = nbJoueurs; // On attribue au joueur un numéro.
        nbJoueurs ++; //Lorsqu'un joueur est créé, on incrémente la variable qui représente le nombre de joueurs.
        zoneCourante = modele.getZoneInit(numJoueur);
        zoneCourante.putJoueur(this);
    }

    //Change la zone dans laqeulle se trouve le joueur. Sers à le faire effectuer tout déplacement.
    public void deplaceJoueur (Zone z) {
        Zone quittee = this.zoneCourante;
        quittee.removeJoueur(this);
        this.zoneCourante = z;
        z.putJoueur(this);
    }


    public void debutTour() {
        this.actionsRestantes = 3;
    }

    public void decrementeAction() {
        this.actionsRestantes --;
    }



    /** METHODES D'ENCAPSULATION **/
    //Retourne la zone dans laquelle se trouve le joueur.
    public Zone getZone() {return zoneCourante;}

    public int getNumJoueur() { return numJoueur ; }

    public int indexJoueurSuivant() {
        if (numJoueur == nbJoueurs - 1) return 0;
        else return numJoueur+1;
    }

    public int getActionsRestantes(){ return this.actionsRestantes; }


}