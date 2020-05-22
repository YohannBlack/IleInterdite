package vue;

import modele.Etat;
import modele.Joueur;
import modele.Modele;
import modele.Zone;
import obsv.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class VueIle extends JPanel implements Observer, MouseListener {
    private Modele modele;
    private final static int TAILLE=30;
    private MouseEvent over;

    //Initialisation des couleurs pour ne pas créer un nouvel objet Color à chaque fois
    private final static Color vert = new Color(245, 220, 120);
    private final static Color bleu = new Color(50, 150, 150);
    private final static Color noir = new Color(10, 7, 171);

    private final static Color[] couleurJoueur = {new Color(255, 0 , 0), new Color(255, 125, 0), new Color(255, 125, 200), new Color(165, 235, 166)};

    public VueIle(Modele modele){
        this.modele = modele;
        modele.addObserver(this);
        Dimension dim = new Dimension(TAILLE*Modele.LARGEUR,
                TAILLE*Modele.HAUTEUR);
        this.setPreferredSize(dim);
        addMouseListener(this);
    }


    public void paintComponent(Graphics g) {
        super.repaint();
        //Pour chaque zone
        for(int i=1; i<=Modele.LARGEUR; i++) {
            for(int j=1; j<=Modele.HAUTEUR; j++) {
                 // Appeler une fonction d'affichage auxiliaire.
                 // On lui fournit les informations de dessin [g] et les
                 // coordonnées du coin en haut à gauche.
                paintZone(g, modele.getZone(i, j), (i-1)*TAILLE, (j-1)*TAILLE);
            }
        }
    }

    private void paintZone(Graphics g, Zone zone, int x, int y) {
        Color c = Color.GRAY;
        // Sélection d'une couleur.
        if (zone.getEtat() == Etat.Normale) {
            c = vert;
        } else if (zone.getEtat() == Etat.Submergee) {
            c = noir;
        } else if (zone.getEtat() == Etat.Inondee) {
            c = bleu;
        } //else g.setColor(Color.GRAY);
        // Coloration d'un rectangle.
        if (mouseSelect(x, y)) g.setColor(c.darker()); else g.setColor(c);
        g.fillRect(x, y, TAILLE, TAILLE);
        g.setColor(c.darker());
        g.drawRect(x, y, TAILLE, TAILLE);
        if (zone.hasJoueurOn()){
            Joueur[] on = zone.getJoueur();
            int i = 0;
            int j = 0;
            for (Joueur p : on) {
                if(p.getNumJoueur() == 1) g.setColor(couleurJoueur[1]);
                else if(p.getNumJoueur() == 2) g.setColor(couleurJoueur[2]);
                else if(p.getNumJoueur() == 3) g.setColor(couleurJoueur[3]);
                else g.setColor(couleurJoueur[0]);

                if(i < TAILLE){
                    g.fillRect(x+i, y, TAILLE/2, TAILLE/2);
                    i+=TAILLE/2;
                } else {
                    g.fillRect(x+j, y, TAILLE/2, TAILLE/2);
                    j+=TAILLE/2;
                }
            }
        }
    }

    private boolean mouseSelect(int x, int y){
        if (over == null ) return false;
        boolean X = (x < over.getX()) && (over.getX() < (x + TAILLE));
        boolean Y = ((y < over.getY()) && (over.getY() < y + TAILLE));
        return (X && Y);
    }


    public void update() {
        repaint();
    }


    //Methodes du MouseListener
    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        Zone z = modele.getZone((e.getX()/TAILLE) + 1, (e.getY()/TAILLE) + 1);
        //System.out.print("\n Mouse Pressed at : " + z.getX() + " " + z.getY());
        modele.actionJoueur(z);
        over = e;
        super.repaint();
    }
    public void mouseReleased(MouseEvent e) {
        over = null;
        super.repaint();
    }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }

}
