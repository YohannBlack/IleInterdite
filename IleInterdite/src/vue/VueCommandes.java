package vue;

import controleur.Controleur;
import modele.Modele;
import modele.Action;
import obsv.Observer;

import javax.swing.*;
import java.awt.*;

public class VueCommandes extends JPanel implements Observer {

    private Modele modele;
    private JLabel joueur = new JLabel("Tour du joueur 1");
    private JLabel actionLeft = new JLabel("Actions restantes : 3/3");
    Color[] colorsJoueur = {new Color(85, 58, 159),
                            new Color(218, 142, 170),
                            new Color(26, 106, 20),
                            new Color(192, 19, 19)};


    public VueCommandes(Modele modele) {
        this.modele = modele;
        this.setLayout(new GridLayout(6, 0));
        modele.addObserver(this);

        JButton boutonAsseche = new JButton("Assécher \n une \n zone"); //Création d'un bouton
        this.add(boutonAsseche); //Ajout du bouton à la vue des commandes
        Controleur dry = new Controleur(modele, Action.ASSECHER); //Lien avec le modele du controleur, qui donnera l'instruction à réaliser
        boutonAsseche.addActionListener(dry); //Association du bouton et de l'instruction

        JButton boutonDeplacement = new JButton("Se \n déplacer"); //Création d'un bouton
        this.add(boutonDeplacement); //Ajout du bouton à la vue des commandes
        Controleur move = new Controleur(modele, Action.DEPLACEMENT); //Lien avec le modele du controleur, qui donnera l'instruction à réaliser
        boutonDeplacement.addActionListener(move); //Association du bouton et de l'instruction

        JButton boutonArtefact = new JButton("Prendre \n Artefact"); //Création d'un bouton
        this.add(boutonArtefact); //Ajout du bouton à la vue des commandes
        Controleur take = new Controleur(modele, Action.ARTEFACT); //Lien avec le modele du controleur, qui donnera l'instruction à réaliser
        boutonArtefact.addActionListener(take); //Association du bouton et de l'instruction;

        JButton boutonAvance = new JButton("Tour \n Suivant"); //Création d'un bouton
        this.add(boutonAvance); //Ajout du bouton à la vue des commandes
        Controleur ctrl = new Controleur(modele, Action.TOUR_SUIVANT); //Lien avec le modele du controleur, qui donnera l'instruction à réaliser
        boutonAvance.addActionListener(ctrl); //Association du bouton et de l'instruction;

        this.add(joueur);
        this.add(actionLeft);
        joueur.setForeground(colorsJoueur[modele.getJoueurActuel().getNumJoueur()]);
    }

    public void update(){
        joueur.setForeground(colorsJoueur[modele.getJoueurActuel().getNumJoueur()]);
        joueur.setText("Tour du joueur " +  (modele.getJoueurActuel().getNumJoueur() + 1));
        actionLeft.setText("Actions restantes " + modele.getJoueurActuel().getActionsRestantes() +"/3");

    }

}
