package controleur;

import modele.Action;
import modele.Modele;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controleur implements ActionListener {

    Modele modele;
    private Action action;

    public Controleur(Modele modele, Action action) {
        this.modele = modele;
        this.action = action;
    }

    public void actionPerformed(ActionEvent e) {
        if (action == Action.TOUR_SUIVANT) modele.tourSuivant(); else modele.setSelectAction(action);
    }

}
