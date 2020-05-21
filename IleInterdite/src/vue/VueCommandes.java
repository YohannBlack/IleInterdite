package vue;

import controleur.Controleur;
import modele.Modele;

import javax.swing.*;

public class VueCommandes extends JPanel {

    private Modele modele;

    public VueCommandes(Modele modele) {
        this.modele = modele;
        JButton boutonAvance = new JButton("Tour \n Suivant"); //Création d'un bouton
        this.add(boutonAvance); //Ajout du bouton à la vue des commandes
        Controleur ctrl = new Controleur(modele); //Lien avec le modele du controleur, qui donnera l'instruction à réaliser
        boutonAvance.addActionListener(ctrl); //Association du bouton et de l'instruction
    }
}
