package modele;

import obsv.Observable;

import java.lang.reflect.Array;
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
    private Zone[] zoneInitPlayer;
    private List<Zone> zoneInitArtefact = new ArrayList<>(); //Les zones ou seront les artefacts
    protected List<Artefact> artefacts = new ArrayList<>();

    private final Random randomGenerator = new Random();

    public Modele(){
        zones = new Zone[LARGEUR+2][HAUTEUR+2];
        for(int i=0; i<LARGEUR+2; i++) {
            for(int j=0; j<HAUTEUR+2; j++) {
                zones[i][j] = new Zone(this,i, j, Type.Normal);
                if(i == LARGEUR/2 && j == HAUTEUR/2) zones[i][j] = new Zone(this, i, j, Type.Heliport);
            }
        }

        //Initialisation des artefacts
        initZoneArtefact();
        initArtefact();

        //FIXME debug
        //Quand on run il y aura 4 zone en bleu fonce, les 4 zone avec les artefacts
        //Le print dit qu'il y a bien les artefacts dans ces zones
        //Mais impossible de les ramasser je ne sais pas pourquoi
        //Lorsque je print getContainArtefact() dans la methode actionJoueur
        //Appremment c'est sur false maintenant... Je ne sais pas quand est-ce que ca change
        //ni pourquoi
        for(Zone[]c : zones) {
            for (Zone z : c)
                System.out.println(z.getX() + " " + z.getY() + " " + z.getContainArtefact());
        }

        init();

        zoneInitPlayer = new Zone[4];
        initPlayer();

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
                //Si la zone contient un artefact on la submerge
                if(z.getContainArtefact()) z.etat = Etat.Submergee;
            }
        }
    }

    /** Methode donnant les spawn point des differents joueurs */
    private void initPlayer(){
        zoneInitPlayer[0] = this.getZone(2, 2);
        zoneInitPlayer[1] = this.getZone(HAUTEUR-1, LARGEUR-1);
        zoneInitPlayer[2] = this.getZone(2, LARGEUR-1);
        zoneInitPlayer[3] = this.getZone(HAUTEUR-1, 2);
    }

    /** Methode donnant 4 zone aleatoire pour les 4 artefacts */
    private void initZoneArtefact(){
        for(int i = 0; i < 4; i++){
            this.zoneInitArtefact.add(zoneAleatoire());
        }
    }

    /**Methode permettant d'initialiser les 4 artefacts du jeu */
    private void initArtefact(){
        artefacts.add(new Artefact(this, Type.Feu));
        artefacts.add(new Artefact(this, Type.Eau));
        artefacts.add(new Artefact(this, Type.Terre));
        artefacts.add(new Artefact(this, Type.Air));
        //Pour chaque zone de zoneInitArtefact, on set un artefact
        for(Zone z : zoneInitArtefact) z.setArtefact(artefacts.get(randomGenerator.nextInt(artefacts.size())));
    }



    /**Methode qui renvoie la liste de zones de l'îles qui ne sont pas encore submergées**/
    private ArrayList<Zone> nonSub() {
        ArrayList<Zone> res = new ArrayList<>();
        for(Zone[] c : this.zones) {
            for(Zone z : c) {
                if ((z.etat != Etat.Submergee) && (z.getX() > 0 && z.getX() <= LARGEUR) && (z.getY() > 0 && z.getY() <= HAUTEUR)) res.add(z);
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
                // être inondée.
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
        System.out.println("Tour du joueur : " + joueurActuel.getNumJoueur()+1);
        joueurActuel.debutTour();
    }

    public void actionJoueur(Zone cible){
        System.out.println("Emplacement joueur " + joueurActuel.getZone().getX() + " " + joueurActuel.getZone().getY());
        System.out.println("Zone cible : " + cible.getX() + " " + cible.getY());
        System.out.println("Artefact ? " + cible.getContainArtefact());
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
                case ARTEFACT:
                    if(joueurActuel.getZone().trouveAdjacentes().contains(cible) && cible.getContainArtefact()){
                        joueurActuel.pickUpArtefact(cible);
                    } else
                        System.out.println("Il n'y a pas d'artefact sur cette zone");
                    break;
                default: System.out.print("Selectionnez une action valide");
            }
        } else System.out.print(" Vous ne pouvez plus effectuer d'action.");
        System.out.print("\n Emplacement nouveau " + joueurActuel.getZone().getX()+ " " + joueurActuel.getZone().getY() + "\n");
        notifyObservers();
    }

    /** Fin de partie lorsque la liste des cases non submergées est vide**/
    public boolean FinPartie(){
        ArrayList<Zone> nonSub = nonSub();
        return nonSub.isEmpty();
    }

    public List<Zone> getZoneInitArtefact() { return zoneInitArtefact;}

    public Zone getZoneInitPlayer(int index){ return zoneInitPlayer[index]; }

    public Zone getZone(int i, int j) {
        return zones[i][j];
    }

    public Joueur getJoueurActuel(){ return joueurActuel; }
}
