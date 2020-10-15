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
    private JLabel artefact = new JLabel("Possede les artefacts : ");
    private JLabel actionLeft = new JLabel("Actions restantes : 3/3");
    private ButtonGroup boutonsActions = new ButtonGroup();
    Color[] colorsJoueur = {new Color(85, 58, 159),
                            new Color(218, 142, 170),
                            new Color(26, 106, 20),
                            new Color(192, 19, 19)};


    public VueCommandes(Modele modele) {
        this.modele = modele;
        this.setLayout(new GridLayout(7, 0));
        modele.addObserver(this);

        boutonDeplacement();
        boutonAsseche();

        JButton boutonRecherche = new JButton("Recherche"); //Création d'un bouton
        this.add(boutonRecherche); //Ajout du bouton à la vue des commandes
        Controleur search = new Controleur(modele, Action.RECHERCHE); //Lien avec le modele du controleur, qui donnera l'instruction à réaliser
        boutonRecherche.addActionListener(search); //Association du bouton et de l'instruction;

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
//        this.add(artefact);
        joueur.setForeground(colorsJoueur[modele.getJoueurActuel().getNumJoueur()]);
    }

    public void boutonDeplacement(){
        Icon deplacement = new ImageIcon("res/deplacement.png");
        Icon deplacementEnable = new ImageIcon("res/deplacementEnabled.png");
        JToggleButton boutonDeplacement = new JToggleButton(deplacement);
        boutonsActions.add(boutonDeplacement);
        boutonDeplacement.setSelectedIcon(deplacementEnable);
        boutonDeplacement.setFocusPainted(false);
        boutonDeplacement.setBackground(new Color(200, 200, 150));
        boutonDeplacement.setToolTipText("Se déplacer");
        this.add(boutonDeplacement);
        Controleur move = new Controleur(modele, Action.DEPLACEMENT);
        boutonDeplacement.addActionListener(move);
    }

    public void boutonAsseche(){
        Icon sacSable = new ImageIcon("res/sacSable.png");
        Icon sacSableEnabled = new ImageIcon("res/sacSableEnabled.png");
        JToggleButton boutonAsseche = new JToggleButton(sacSable);
        boutonsActions.add(boutonAsseche);
        boutonAsseche.setSelectedIcon(sacSableEnabled);
        boutonAsseche.setFocusPainted(false);
        boutonAsseche.setBackground(new Color(200, 200, 150));
        boutonAsseche.setToolTipText("Assecher une zone");
        this.add(boutonAsseche);
        Controleur dry = new Controleur(modele, Action.ASSECHER);
        boutonAsseche.addActionListener(dry);
    }

    public void update(){
        joueur.setForeground(colorsJoueur[modele.getJoueurActuel().getNumJoueur()]);
        joueur.setText("Tour du joueur " +  (modele.getJoueurActuel().getNumJoueur() + 1));
        actionLeft.setText("Actions restantes " + modele.getJoueurActuel().getActionsRestantes() +"/3");

    }

}
