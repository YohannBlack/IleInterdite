package modele;

public class Artefact {

    private Modele modele;
    private final Type type;
    private boolean isPickedUp = false;
    private Joueur pickedUpBy = null;

    public Artefact(Modele modele, Type type){
        this.modele = modele;
        this.type = type;
    }

    /** Pour debugger */
    public void tostring(){
        System.out.println("L'artefact " + type + " est present");
    }

    /** Methode permettant de changer l'attribut de l'artefact pour savoir si oui ou non il a ete ramasse
     * @param pickedUp un boolean true si l'artefact est ramasse
     */
    public void setPickedUp(boolean pickedUp) { isPickedUp = pickedUp;}

    /** Methode permettant de changer l'attribut de l'artefact pour savoir quel joueur l'a recuperre
     * @param j le joueur qui a recuperre
     */
    public void setPickedUpBy(Joueur j){ pickedUpBy = j;}

    /** Methode permettant de retourner le type de l'artefact
     *
     * @return le type de l'artefact
     */
    public Type getType(){ return this.type; }
}
