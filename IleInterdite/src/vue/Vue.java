package vue;

import modele.Modele;

import javax.swing.*;
import java.awt.*;

public class Vue {
    private JFrame frame;
    private VueIle ile;
    private VueCommandes commandes;

    public Vue(Modele modele) {
        frame = new JFrame();
        frame.setTitle("île interdite");
        frame.setLayout(new FlowLayout());

        ile = new VueIle(modele);
        frame.add(ile);
        commandes = new VueCommandes(modele);
        frame.add(commandes);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
