import javax.swing.*;

/*
*  This class defines a gui for the polygon program.
*
* */

public class Gui extends JFrame {

    public Gui(){
        Image i = new Image();
        add(i);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(800,800);
        setVisible(true);
    }


    public static void main(String[] args) {
        Gui g = new Gui();
    }
}
