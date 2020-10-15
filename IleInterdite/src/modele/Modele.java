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
    private List<Zone> zoneSpecialInit = new ArrayList<>(); //Les zones ou seront les items specials
    protected List<Artefact> artefacts = new ArrayList<>(); //Les artefacts
    protected List<Cle> cle = new ArrayList<>(); //Les cles
    private final Type[] typeSpecial = {Type.Feu, Type.Eau, Type.Air, Type.Terre}; //Les types des differents items speciaux

    private final Random randomGenerator = new Random();

    public Modele(){
        zones = new Zone[LARGEUR+2][HAUTEUR+2];
        for(int i=0; i<LARGEUR+2; i++) {
            for(int j=0; j<HAUTEUR+2; j++) {
                zones[i][j] = new Zone(this,i, j, Type.Normal);
                if(i == LARGEUR/2 && j == HAUTEUR/2) zones[i][j] = new Zone(this, i, j, Type.Heliport);
            }
        }

        initZoneSpecial();
        initItemSpecial();
        for(Zone z : zoneSpecialInit) System.out.println(z.getX() + " " + z.getY() + " " + z.getContainArtefact());

//        for(Zone z : zoneInitArtefact) z.setArtefact(artefacts.get(randomGenerator.nextInt(artefacts.size())));

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
    public void initZoneSpecial(){
        while(zoneSpecialInit.size()!=4){
            Zone z = zoneAleatoire();
            zoneSpecialInit.add(z);
            //On verifie que la zone choisit n'est pas l'heliport ou qu'on ne choisit pas deux fois la meme zone
            if(zoneSpecialInit.get(zoneSpecialInit.size()-1).getType() == Type.Heliport && zoneSpecialInit.contains(z))
                zoneSpecialInit.remove(zoneSpecialInit.get(zoneSpecialInit.size()-1));
        }
    }

    /** Methode permettant d'initialiser les 4 artefacts du jeu */
    public void initItemSpecial(){
        //Initialisation des artefacts et cles du modele
        for(int i = 0; i < 4; i++){
            artefacts.add(new Artefact(this, typeSpecial[i]));
            cle.add(new Cle(this, typeSpecial[i]));
        }

        for(Zone z : zoneSpecialInit) z.setArtefact(artefacts.get(randomGenerator.nextInt(artefacts.size())));
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

    /**Methode permettant de passer au tour suivant
     * innondation de 3 zones aleatoires lors du changement de tour
     */
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

        tourJoueur((joueurActuel.getNumJoueur() + 1)%4);
        // On notifie les observateurs, afin que la vue soit mise à
        notifyObservers();
    }

    /**Methode permettant de set le tour du joueur actuel */
    public void tourJoueur(int i){
        //Changement du joueur actuel
        joueurActuel = joueurs[i];
        System.out.println("Tour du joueur : " + joueurActuel.getNumJoueur()+1);
        //Commencement de son tour
        joueurActuel.debutTour();
    }

    /**Methode qui permettra l'action du joueur actuel sur une zone cible
     * @param cible la zone que le joueur a choisi
     */
    public void actionJoueur(Zone cible){
        System.out.println("Emplacement joueur " + joueurActuel.getZone().getX() + " " + joueurActuel.getZone().getY());
        System.out.println("Zone cible : " + cible.getX() + " " + cible.getY());
        System.out.println("Artefact ? " + cible.getContainArtefact());
        //On verifie qu'il reste encore des actions au joueur actuel
        if (joueurActuel.getActionsRestantes() != 0) {
            //On regarde les differentes actions possibles
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
                    if((joueurActuel.getZone().trouveAdjacentes().contains(cible) || joueurActuel.getZone() == cible) && cible.getContainArtefact()){
                        joueurActuel.pickUpArtefact(cible);
                    } else
                        System.out.println("Il n'y a pas d'artefact sur cette zone");
                    break;
                case RECHERCHE:
                    joueurActuel.recherche();
                    break;
                default: System.out.print("Selectionnez une action valide");
            }
        } else System.out.print(" Vous ne pouvez plus effectuer d'action.");
        System.out.print("\n Emplacement nouveau " + joueurActuel.getZone().getX()+ " " + joueurActuel.getZone().getY() + "\n");
        notifyObservers();
    }

    public Zone getHeliport(){
        for(Zone[]c : zones)
            for(Zone z : c)
                if(z.getType() == Type.Heliport)
                    return z;
                return null;
    }

    /** Fin de partie lorsque la liste des cases non submergées est vide **/
//    public boolean FinPartie(){
//        ArrayList<Zone> nonSub = nonSub();
//        Zone heliport = getHeliport();
//        if(nonSub.isEmpty() || (getHeliport().joueursOn.size() = 4 && ))
//    }

    public Cle getCle(){
        Cle c = cle.get(randomGenerator.nextInt(cle.size()));
        cle.remove(c);
        return c;
    }

    public List<Zone> getZoneSpecialInit() { return zoneSpecialInit;}

    public Zone getZoneInitPlayer(int index){ return zoneInitPlayer[index]; }

    public Zone getZone(int i, int j) {
        return zones[i][j];
    }

    public Joueur getJoueurActuel(){ return joueurActuel; }
}
