import modele.Modele;
import vue.Vue;

import java.awt.*;

public class Main {

    public static void main(String[] args){
        EventQueue.invokeLater(() -> {
            Modele modele = new Modele();
            Vue vue = new Vue(modele);
        });
    }
}
