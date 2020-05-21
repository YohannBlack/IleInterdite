package modele;

import obsv.Observable;

import java.util.Arrays;
import java.util.List;

public class Joueur extends Observable {
    private Modele modele;
    private Zone zoneCourante; //La zone dans laquelle se trouve le joueur
    private int actionsRestantes = 3; //Le nombre d'actions restantes au joueur lors de son tour, initialisé à 3.
    private final int numJoueur; //Numéros de joueur, pour l'identifier.
    private static int nbJoueurs = 0; // Le nombre total de joueurs dans la partie.

    public Joueur (Modele modele) {
        this.modele = modele;
        nbJoueurs ++; //Lorsqu'un joueur est créé, on incrémente la variable qui représente le nombre de joueurs.
        numJoueur = nbJoueurs; // On attribue au joueur un numéro.
        placeJoueur(modele.getZone(numJoueur,numJoueur));
    }

    //Change la zone dans laqeulle se trouve le joueur. Sers à la fois à placer le joueur au début de la partie, mais
    //aussi à le faire effectuer tout déplacement.
    public void placeJoueur (Zone z) {
        Zone oldZone = this.zoneCourante;
        if(oldZone != null) oldZone.occupee = false;
        z.putJoueur(this);
        this.zoneCourante = z;
        z.occupee = true;
    }

    //Methode qui renvoie une liste de zones adjacentes à celle où se trouve le joueur.
    protected List<Zone> trouveAdjacentes() {
        return Arrays.asList(
                modele.getZone(zoneCourante.getX(), zoneCourante.getY() - 1),
                modele.getZone(zoneCourante.getX() - 1, zoneCourante.getY()),
                modele.getZone(zoneCourante.getX() + 1, zoneCourante.getY()),
                modele.getZone(zoneCourante.getX(), zoneCourante.getY() + 1)
        );
    }

    /** Methode de gestion d'une action du joueur. Elle sera appelée lorsque la vue notifiera une interraction (click etc)
     * On vérifie d'abord si le joueur a le droit d'effectuer une action, càd si il lui reste des actions.
     * */
    public void action(Zone z) {
        if (actionsRestantes != 0) {
            if (trouveAdjacentes().contains(z)) {
                placeJoueur(z);
                this.notifyObservers();
            }
        }
    }

    /** METHODES D'ENCAPSULATION **/
    //Retourne la zone dans laquelle se trouve le joueur.
    public Zone getZone() {return zoneCourante;}

    public int getNumJoueur() { return numJoueur ; }

    public int getActionsRestantes(){ return this.actionsRestantes; }

}