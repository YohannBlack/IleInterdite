package vue;

import controleur.Controleur;
import modele.Modele;
import modele.Action;

import javax.swing.*;
import java.awt.*;

public class VueCommandes extends JPanel {

    private Modele modele;

    public VueCommandes(Modele modele) {
        this.modele = modele;
        this.setLayout(new GridLayout(3, 0));

        JButton boutonAsseche = new JButton("Assécher \n une \n zone"); //Création d'un bouton
        this.add(boutonAsseche); //Ajout du bouton à la vue des commandes
        Controleur dry = new Controleur(modele, Action.ASSECHER); //Lien avec le modele du controleur, qui donnera l'instruction à réaliser
        boutonAsseche.addActionListener(dry); //Association du bouton et de l'instruction

        JButton boutonDeplacement = new JButton("Se \n déplacer"); //Création d'un bouton
        this.add(boutonDeplacement); //Ajout du bouton à la vue des commandes
        Controleur move = new Controleur(modele, Action.DEPLACEMENT); //Lien avec le modele du controleur, qui donnera l'instruction à réaliser
        boutonDeplacement.addActionListener(move); //Association du bouton et de l'instruction

        JButton boutonAvance = new JButton("Tour \n Suivant"); //Création d'un bouton
        this.add(boutonAvance); //Ajout du bouton à la vue des commandes
        Controleur ctrl = new Controleur(modele, Action.TOUR_SUIVANT); //Lien avec le modele du controleur, qui donnera l'instruction à réaliser
        boutonAvance.addActionListener(ctrl); //Association du bouton et de l'instruction
    }

}
