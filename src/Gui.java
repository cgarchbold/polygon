import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
*  This class defines a gui for the polygon program.
*
* */

public class Gui extends JFrame implements ActionListener {

    JMenuBar menuBar;
    JMenu File;
    JButton reset, color;
    JButton fill, play, stop ;
    JLabel animation_sec;
    Image i;

    public Gui(){
        i = new Image();
        add(i);

        menuBar = new JMenuBar();
        File = new JMenu("File");
        reset = new JButton("Reset");
        color = new JButton("Set Color");
        fill = new JButton("Fill Poly");
        animation_sec = new JLabel("Control:");
        play = new JButton("Play");
        stop = new JButton("Stop");

        reset.addActionListener(this);
        color.addActionListener(this);
        fill.addActionListener(this);

        menuBar.add(File);
        menuBar.add(reset);
        menuBar.add(color);
        menuBar.add(fill);
        menuBar.add(animation_sec);
        menuBar.add(play);
        menuBar.add(stop);
        this.setJMenuBar(menuBar);

        fill.setOpaque(true);
        menuBar.setOpaque(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(800,800);
        setVisible(true);
    }

    public void reset(){
        remove(i);
        i = new Image();
        add(i);
        this.revalidate();
    }


    public static void main(String[] args) {
        Gui g = new Gui();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == reset){
            reset();
        }
        if(e.getSource() == color){
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            i.setColor(newColor);
        }
        if(e.getSource() == fill){
            if(i.isPoly_fill()){
                i.setPoly_fill(false);
                fill.setBackground(null);
            }
            else{
                i.setPoly_fill(true);
                fill.setBackground(Color.LIGHT_GRAY);
            }
        }
    }
}
