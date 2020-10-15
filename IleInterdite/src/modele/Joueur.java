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
    private List<Artefact> artefact = new ArrayList<>();
    private List<Cle> cle = new ArrayList<>();

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
        //Retire le joueur de l'ancienne zone
        this.zoneCourante.removeJoueur(this);
        //Change la zone courrante du joueur
        this.zoneCourante = z;
        //Place le joueur sur la nouvelle zone
        z.putJoueur(this);
    }


    public void debutTour() {
        this.actionsRestantes = 3;
    }

    public void decrementeAction() {
        this.actionsRestantes--;
    }

    /** Methode permettant de faire l'action de recuperer un artefact sur une zone cible
     * @WARNING: Ne marche que si le joueur possede la cle correspondante
     * @param zone cible
     */
    public void pickUpArtefact(Zone zone){
        //Veirifaction pour la cle
        if(containClePourArtefact(zone.getArtefact())) {
            //Recuperation de l'artefact
            this.artefact.add(zone.pickUpArtefact());
            //Change l'attribut de l'artefact
            for (Artefact a : artefact) a.setPickedUpBy(this);
            //Modification de l'emplacement du joueur
            deplaceJoueur(zone);
            //Diminutions des actions
            decrementeAction();
            printArtefact();
        } else {
            System.out.println("Ce joueur n'a pas la cle correspondante");
        }
    }

    public void recherche(){
        if(Math.random() < 0.2) {
            cle.add(modele.getCle());
            printCle();
        } else if (Math.random() >= 0.2 && Math.random() < 0.4){
            if(zoneCourante.getEtat() == Etat.Inondee) zoneCourante.setEtat(Etat.Submergee);
            zoneCourante.etat = Etat.Inondee;
            System.out.println("Montée des eaux");
        } else {
            System.out.println("La recherche n'a rien donnée");
        }
        decrementeAction();
    }

    /** Methode permettant de verifier sur le joueur possede la cle associer a l'artefact
     *  correspondant
     * @param a l'artefact a recuperer
     * @return TRUE si on a la cle correspondante FALSE sinon
     */
    public boolean containClePourArtefact(Artefact a){
        for(Cle c : cle)
            if(a.getType() == c.getType()) return true;

        return false;
    }

    //Debug
    public void printArtefact(){
        for(Artefact a : artefact){
            System.out.println("L'artefact " + a.getType() + " est en possession du joueur " + this.numJoueur);
        }
    }

    //Debug
    public void printCle(){
        for(Cle c : cle){
            System.out.println("La cle " + c.getType() + " est en possesion du joueur " + this.numJoueur);
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
}