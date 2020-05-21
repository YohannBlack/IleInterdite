package vue;

import modele.Etat;
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

    /**Initialisation des couleurs pour ne pas créer un nouvel objet Color à chaque fois*/
    private final static Color vert = new Color(245, 220, 120);
    private final static Color bleu = new Color(50, 150, 150);
    private final static Color noir = new Color(10, 7, 171);
    private final static Color rouge = new Color(171, 7, 0);

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
        /** Pour chaque cellule... */
        for(int i=1; i<=Modele.LARGEUR; i++) {
            for(int j=1; j<=Modele.HAUTEUR; j++) {
                /**
                 * ... Appeler une fonction d'affichage auxiliaire.
                 * On lui fournit les informations de dessin [g] et les
                 * coordonnées du coin en haut à gauche.
                 */
                paint(g, modele.getZone(i, j), (i-1)*TAILLE, (j-1)*TAILLE);
            }
        }
    }

    private void paint(Graphics g, Zone zone, int x, int y) {
        /** Sélection d'une couleur. */
        if (zone.getEtat() == Etat.Normale) {
            g.setColor(vert);
        } else if (zone.getEtat() == Etat.Submergee) {
            g.setColor(noir);
        } else if (zone.getEtat() == Etat.Inondee) {
            g.setColor(bleu);
        } else g.setColor(Color.GRAY);
        /** Coloration d'un rectangle. */
        g.fillRect(x, y, TAILLE, TAILLE);
    }


    public void update() {
        repaint();
    }

    private boolean mouseOver(int x, int y){
        if(over == null) return false;
        boolean X = (x < over.getX()) && (over.getX() < x + TAILLE);
        boolean Y = (y < over.getY()) && (over.getY() < y + TAILLE);
        return (X && Y);
    }


    //Methodes du MouseListener
    public void mouseClicked(MouseEvent e) {
        over = e;
        super.repaint();
    }
    public void mousePressed(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }
    /*public void mouseEntered(MouseEvent e) {
        //FIXME ne repère pas la souris.
        System.out.println(("yo")); //Ca s'affiche pas, ce qui veut dire que le passage de la souris n'est pas détecté, et je sais pas encore pourquoi

        int x = e.getX(); //Recupere la position x de l'endroit où la souris est rentrée (evenement e).
        int y = e.getY(); //Idem pour le y. A noter que je ne suis pas sûre de si la position est correcte : est ce que la position obtenue
                        // est celle de la souris sur le component (c'est à dire sur VueIle, est dans ce cas là c'est bon) ou la position de la souris
                        // sur toute la fenetre (cad que ça comprend l'espace avec le bouton "Tour Suivant", et là ça peut poser problème dans certains cas)
        Graphics g = e.getComponent().getGraphics(); //Recupere le contexte graphique de l'evenement
        super.repaint(); //à partir de là j'ai essayé de refaire l'action de paintComponent, mais sur une seule zone.
        g.setColor(Color.BLACK);
        g.fillRect(x%TAILLE, y%TAILLE, TAILLE, TAILLE);

    }
    public void mouseExited(MouseEvent e) { }
    /** Afin de rendre la vue plus r�active, et � faciliter le rep�rage du curseur sur l'�le, on
     * a impl�ment� les fonctions de l'interface MouseListener :
     * - Lorque la souris entre sur la zone, celle-ci s'assombris.
     * - Lorsque la souris quitte la zone, elle s'�clairci, ce qui revient � reprendre se couleur d'origine.**/
    public void mouseEntered(MouseEvent e) {
        changeColor(rouge.darker());
    }
    public void mouseExited(MouseEvent e) { changeColor(rouge.brighter()); }
    /** Methode appel�e lorsqu'une zone doit changer de couleur.
     * On modifie son attribut de couleur
     * On sp�cifie la couleur de fond (= couleur de la zone, puisqu'elle est vide.)
     * Enfin on appelle la methode de la classe m�re JPANEL qui r�affiche la zone.
     * **/
    public void changeColor(Color c) {
        setBackground(c);
        super.repaint();
    }
}
