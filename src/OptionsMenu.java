import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class OptionsMenu extends JFrame implements ActionListener {

    static int squere1_loc=0;
    static int squere2_loc=0;
    static int p1_color_loc=0;
    static int p2_color_loc=0;
    static int opponent_loc=0;
    static int difficulty=4;
    static boolean vsAI=true;

    Color squere1= Color.BLUE;
    Color squere2 = Color.LIGHT_GRAY;
    Color p1_color = Color.BLACK;
    Color p2_color = Color.WHITE;
    Color[] colors;
    JButton button;
    String[] color1Strings = { "RED", "MAGENTA", "BLUE", "GREEN","DARK GRAY" };
    String[] color2Strings = { "YELLOW", "CYAN", "ORANGE","LIGHT GRAY" };
    String[] color3Strings = { "BROWN", "BLACK", "GOLD", "LILAC"};
    String[] color4Strings = { "ICE BLUE", "WHITE", "GRAY" };

    JPanel opponnent_panel;
    JPanel player1Panel;
    JPanel player2Panel;
    JPanel tile2Panel;
    JPanel tile1Panel;
    JPanel mainPanel;
    JPanel slider_panel;
    JLabel tile1Label;
    JLabel tile2Label;
    JLabel slider_label;

    JLabel player1Label;
    JLabel player2Label;
    JLabel opponentLabel;

    JComboBox color1List;
    JComboBox color2List;
    JComboBox color3List;
    JComboBox color4List;

    JComboBox opponent;
    String[] opponentList = { "Player vs AI","Player vs Player"};
    JSlider slider;

    public  OptionsMenu()
    {

        opponnent_panel = new JPanel();
        opponent = new JComboBox(opponentList);
        opponentLabel=new JLabel("Choose an opponent");
        opponnent_panel.add(opponentLabel);
        opponnent_panel.add(opponent);

        slider_panel = new JPanel();
        slider_label=new JLabel("Tree Depth");
        slider = new JSlider(JSlider.HORIZONTAL, 2, 10, 4);
        slider.setMinorTickSpacing(2);
        slider.setSnapToTicks(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setLabelTable(slider.createStandardLabels(2));
        slider.setValue(difficulty);
        slider_panel.add(slider_label);
        slider_panel.add(slider);

        colors=new Color[]{squere1,squere2,p1_color,p2_color};
        this.setSize(new Dimension(300,350));
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel();

        tile1Panel = new JPanel();
        tile1Label = new JLabel("Tile color 1");
        color1List = new JComboBox(color1Strings);
        tile1Panel.add(tile1Label);
        tile1Panel.add(color1List);

        player1Panel = new JPanel();
        player1Label = new JLabel("Player 2 color");
        color3List = new JComboBox(color3Strings);
        player1Panel.add(player1Label);
        player1Panel.add(color3List);

        player2Panel = new JPanel();
        player2Label = new JLabel("Player 1 color");
        color4List = new JComboBox(color4Strings);
        player2Panel.add(player2Label);
        player2Panel.add(color4List);

        color2List = new JComboBox(color2Strings);
        tile2Label = new JLabel("Tile color 2");
        tile2Panel = new JPanel();
        tile2Panel.add(tile2Label);
        tile2Panel.add(color2List);


        button = new JButton("Save and Reset Board" );
        button.addActionListener(this);

        mainPanel.add(tile1Panel);
        mainPanel.add(tile2Panel);
        mainPanel.add(player1Panel);
        mainPanel.add(player2Panel);
        mainPanel.add(slider_panel);
        mainPanel.add(opponnent_panel);
        mainPanel.add(button);

        this.add(mainPanel);

        color1List.setSelectedIndex(squere1_loc);
        color2List.setSelectedIndex(squere2_loc);
        color3List.setSelectedIndex(p1_color_loc);
        color4List.setSelectedIndex(p2_color_loc);

        opponent.setSelectedIndex(opponent_loc);
        if (opponent_loc==0)
            vsAI=true;
        else
            vsAI=false;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selected=String.valueOf(color1List.getSelectedItem());
        String selected2=String.valueOf(color2List.getSelectedItem());
        String selected3=String.valueOf(color3List.getSelectedItem());
        String selected4=String.valueOf(color4List.getSelectedItem());

        difficulty=slider.getValue();

        squere1_loc=color1List.getSelectedIndex();
        squere2_loc=color2List.getSelectedIndex();
        p1_color_loc=color3List.getSelectedIndex();
        p2_color_loc=color4List.getSelectedIndex();

        opponent_loc=opponent.getSelectedIndex();

        if (opponent_loc==0)
        {
            vsAI=true;
        }
        else
            vsAI=false;

        if(selected=="RED")
        {
            squere2=Color.RED;
        }
        else if(selected=="MAGENTA")
        {
            squere2=Color.MAGENTA;
        }
        else if(selected=="BLUE")
        {
            squere2=Color.BLUE;
        }
        else if(selected=="GREEN")
        {
            squere2=Color.GREEN;
        }else if(selected=="DARK GRAY")
        {
            squere2=Color.DARK_GRAY;
        }

        if(selected2=="YELLOW")
        {
            squere1=Color.YELLOW;
        }
        else if(selected2=="CYAN")
        {
            squere1=Color.CYAN;
        }
        else if(selected2=="ORANGE")
        {
            squere1=Color.ORANGE;
        }
        else if(selected2=="LIGHT GRAY")
        {
            squere1=Color.LIGHT_GRAY;
        }


        if(selected3=="BROWN")
        {
            p2_color=new Color(139,69,19);
        }
        else if(selected3=="BLACK")
        {
            p2_color=Color.BLACK;
        }
        else if(selected3=="GOLD")
        {
            p2_color=new Color(207,181,59);
        }
        else if(selected3=="LILAC")
        {
            p2_color=new Color(71,40,82);
        }


        if(selected4=="ICE BLUE")
        {
            p1_color=new Color(92,192,192);
        }
        else if(selected4=="WHITE")
        {
            p1_color=Color.WHITE;
        }
        else if(selected4=="GRAY")
        {
            p1_color=new Color(128,128,128);
        }
        colors=new Color[]{squere1,squere2,p1_color,p2_color};
        setVisible(false);
        dispose();
    }

}
