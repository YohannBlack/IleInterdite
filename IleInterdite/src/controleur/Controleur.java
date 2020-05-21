package controleur;

import modele.Modele;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controleur implements ActionListener {

    Modele modele;

    public Controleur(Modele modele){ this.modele = modele; }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        modele.tourSuivant();
    }
}
