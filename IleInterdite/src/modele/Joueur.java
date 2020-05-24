package modele;

import obsv.Observable;

import java.util.ArrayList;
import java.util.List;

public class Joueur extends Observable {
    private Modele modele;
    private Zone zoneCourante; //La zone dans laquelle se trouve le joueur
    private int actionsRestantes = 3; //Le nombre d'actions restantes au joueur lors de son tour, initialisé à 3.
    private final int numJoueur; //Numéros de joueur, pour l'identifier.
    private static int nbJoueurs = 0; // Le nombre total de joueurs dans la partie.
    private List<Artefact> artefact = new ArrayList<>(); //La liste d'artefact que le joueur a recolte le long de la partie

    public Joueur(Modele modele) {
        this.modele = modele;
        numJoueur = nbJoueurs; // On attribue au joueur un numéro.
        nbJoueurs ++; //Lorsqu'un joueur est créé, on incrémente la variable qui représente le nombre de joueurs.
        zoneCourante = modele.getZoneInitPlayer(numJoueur);
        zoneCourante.putJoueur(this);
    }

    //Change la zone dans laqeulle se trouve le joueur. Sers à le faire effectuer tout déplacement.

    /** Methode changeant la zone dans laquelle se trouve le joueur.
     *  Sert a effectuer tout deplacement du joueur
     * @param z la zone ou il faut se deplacer
     */
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
        this.actionsRestantes--;
    }

    /** Methode permettant de faire l'action de recuperer un artefact sur une zone cible
     * @param zone cible
     */
    public void pickUpArtefact(Zone zone){
        //Rajoute l'artefact de la zone a la liste
        this.artefact.add(zone.getArtefact());
        //Modification des attributs de l'artefact
        zone.getArtefact().setPickedUp(true);
        zone.getArtefact().setPickedUpBy(this);
        //Decrement l'action du joueur
        decrementeAction();
        printArtefact();
    }

    public void printArtefact(){
        for(Artefact a : artefact){
            System.out.println("L'artefact " + a.getType() + " est en possession du joueur " + this.numJoueur);
        }
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


    public Artefact getArtefact(int index) { return this.artefact.get(index); }
}