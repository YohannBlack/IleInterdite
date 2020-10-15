package vue;

import modele.*;
import obsv.Observer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class VueIle extends JPanel implements Observer, MouseListener {

    private final static long serialVersionUID = 1L;
    private Modele modele;
    private final static int TAILLE=30;
    private MouseEvent over;
    private BufferedImage[] imageJoueurs;
    private BufferedImage[] imageArtefacts;
    private final Color[] couleurArtefact = { new Color(255, 128, 0, 150),
                                         new Color(51, 255, 255, 150),
                                         new Color(102, 51, 0, 100),
                                         new Color(128, 128, 128, 200)
                                        };

    //Initialisation des couleurs pour ne pas créer un nouvel objet Color à chaque fois
    private final static Color vert = new Color(245, 220, 120);
    private final static Color bleu = new Color(50, 150, 150);
    private final static Color noir = new Color(10, 7, 171);

//    private final static Color[] couleurJoueur = {new Color(255, 0 , 0), new Color(255, 125, 0), new Color(255, 125, 200), new Color(165, 235, 166)};

    public VueIle(Modele modele){
        this.modele = modele;
        modele.addObserver(this);
        Dimension dim = new Dimension(TAILLE*Modele.LARGEUR,
                TAILLE*Modele.HAUTEUR);
        this.setPreferredSize(dim);
        addMouseListener(this);

        String[] filePaths = {"res/pionBleu.png", "res/pionRose.png", "res/pionVert.png", "res/pionRouge.png", "res/H.png" };
        String[] filePathsArtefact = {"res/zoneFire.png", "res/zoneEau.png", "res/zoneTerre.png", "res/zoneAir.png"};
        imageJoueurs = new BufferedImage[5];
        imageArtefacts = new BufferedImage[4];

        for(int i = 0; i < 5; i++){
            try{
                imageJoueurs[i] = ImageIO.read(new File(filePaths[i]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for(int i = 0; i < 4; i++){
            try{
                imageArtefacts[i] = ImageIO.read(new File(filePathsArtefact[i]));
            } catch (IOException e){
                e.printStackTrace();
            }
        }
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

        //Sélection d'une couleur.
        if (zone.getEtat() == Etat.Normale) {
            c = vert;
        } else if (zone.getEtat() == Etat.Submergee) {
            c = noir;
        } else if (zone.getEtat() == Etat.Inondee) {
            c = bleu;
        }


        //on vérifie si il faut afficher la zone comme selectionnée par la souris
        if (mouseSelect(x, y)) g.setColor(c.darker()); else g.setColor(c);
        //Coloration d'un rectangle. */
        g.fillRect(x, y, TAILLE, TAILLE);
        //On dessiner les contour des zones d'une couleur plus foncée, pour mieux les reperer.
        g.setColor(c.darker());
        g.drawRect(x, y, TAILLE, TAILLE);

        if (zone.getType() == Type.Heliport) {
            g.setColor(c);
            g.fillRect(x, y, TAILLE, TAILLE);
            g.drawImage(imageJoueurs[4], x, y , TAILLE, TAILLE, null);
        }

        paintZoneSpecial(g, zone, x, y, c);

        //Si la zone peinte est occupée par au moins un joueur, on peint ces joueurs.
        if (zone.hasJoueurOn()){
            Joueur[] on = zone.getJoueur();
            int i = 0;
            int j = 0;
            int t = TAILLE;
            if (on.length > 1) t = t/2;
            for (Joueur p : on) {
                if(i < TAILLE) {
                    g.drawImage(imageJoueurs[p.getNumJoueur()], x + i, y, t, t, null);
                    i+= TAILLE/2;
                }else{
                    g.drawImage(imageJoueurs[p.getNumJoueur()], x + j, y + TAILLE/2, t, t, null);
                    j+= TAILLE/2;
                }
            }
        }

    }

    /**Methode permettant de colorier les zones speciales de l'ile
     * @param g le grpahique
     * @param zone a colorier
     * @param x coordonnee
     * @param y coordonnee
     */
    private void paintZoneSpecial(Graphics g, Zone zone, int  x, int y, Color color){
        Image art = null;
        //Verification que la zone soit speciale
        if(zone.getContainArtefact() && zone.getType() == Type.Feu){
//            cArt = couleurArtefact[0];
            art = imageArtefacts[0];
        } else if(zone.getContainArtefact() && zone.getType() == Type.Eau){
//            cArt = couleurArtefact[1];
            art = imageArtefacts[1];
        } else if(zone.getContainArtefact() && zone.getType() == Type.Terre){
//            cArt = couleurArtefact[2];
            art = imageArtefacts[2];
        } else if(zone.getContainArtefact() && zone.getType() == Type.Air){
//            cArt = couleurArtefact[3];
            art = imageArtefacts[3];
        }

        if(zone.getContainArtefact()){
            g.setColor(color);
            g.fillRect(x, y, TAILLE, TAILLE);
//            g.drawRect(x, y, TAILLE, TAILLE);
            g.drawImage(art, x, y , TAILLE, TAILLE, null);
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
