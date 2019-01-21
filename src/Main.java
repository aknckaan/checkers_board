

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Main {
    static Board b;
    static Options o;
    static JFrame myFrame;
    public static void main(String[] args)
    {

        myFrame= new JFrame();
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel myPanel= new JPanel();
        GridBagLayout gb_l=new GridBagLayout();
        myPanel.setLayout(gb_l);
        myFrame.setSize(new Dimension(1000,900));
        myFrame.setVisible(true);
        myFrame.setResizable(false);
        myFrame.add(myPanel);

        loadOptions(myPanel,gb_l);
        loadBoard(myPanel,gb_l,o);

        Animator animator=new Animator(b,o);
        o.setActionListeners(animator);
        b.addMouseMotionListener(animator);
        b.addMouseListener(animator);


    }
    public static void loadBoard(JPanel myPanel, GridBagLayout gb_l,Options o)
    {

        b=new Board(o.colors);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=0;
        c.ipady = 0;
        c.ipadx = 0;
        gb_l.setConstraints(b, c);
        myPanel.add(b);
        myPanel.revalidate();
        myPanel.repaint();

    }
    public static void loadOptions(JPanel myPanel,GridBagLayout gb_l)
    {
       o=new Options();
        GridBagConstraints c = new GridBagConstraints();
        c.gridx=1;
        c.gridy=0;
        c.ipady = 0;
        c.ipadx = 0;
        gb_l.setConstraints(o, c);
        myPanel.add(o);
        myPanel.revalidate();
        myPanel.repaint();

    }

    public static class Animator implements ActionListener,MouseListener,MouseMotionListener,WindowListener
    {
        boolean vsAI=true;
        int difficulty=4;
        Board b;
        Options o;
        int isPressed=0;
        Tile selectedPiece;
        int selected_x;
        int selected_y;
        Generator gen;
        Minimax mm;
        ArrayList<Tile> forcedPieces;
        OptionsMenu om;
        boolean turn=true; //true for player 1, false for player 2

        public Animator(Board b, Options o)
        {
            this.b=b;
            this.o=o;
            isPressed=0;
            turn=true;
            forcedPieces = new ArrayList<Tile>();
            b.resetBoard();
            gen=new Generator();
            mm=new Minimax(b.getState(),turn,gen,difficulty);
            mm.generateTree();
            b.repaint();
        }

        public int validMove(Tile target, int index_x, int index_y, Board comp)
        {
            // 0 invalid
            // 1 just move
            // 2 kill and move
            
            target.setSelected(true);

            if (selectedPiece.piece==1 & index_y>selected_y & !selectedPiece.isKing )
            {
                return 0;
            }
            if (selectedPiece.piece==2 & index_y<selected_y & !selectedPiece.isKing) {
                return 0;
            }

            if (target.piece==0 )//move to empty tile
            {
                Tile between_tile=comp.tiles[(index_x+selected_x)/2][(index_y+selected_y)/2];
                if (Math.abs(index_x-selected_x)==1 & Math.abs(index_y-selected_y)==1)
                {
                    return 1;
                }
                else if (Math.abs(index_x-selected_x)==2 & Math.abs(index_y-selected_y)==2 & between_tile.piece!=selectedPiece.piece & between_tile.piece!=0)
                {
                    return 2;
                }

            }

            return 0;
        }

        public boolean availableMove(Tile curTile, Board comp,int index_x, int index_y )
        {

            if(curTile.piece==1)
            {
                try {
                    Tile up_right=comp.tiles[index_x+1][index_y-1];
                    if(up_right.piece==2)
                    {
                        Tile up_up_right=comp.tiles[index_x+2][index_y-2];

                        if (up_up_right.piece==0)
                        {
                            return true;
                        }
                    }

                }catch (Exception e)
                {

                }

                try {
                    Tile up_left=comp.tiles[index_x-1][index_y-1];
                    if(up_left.piece==2)
                    {
                        Tile up_up_left=comp.tiles[index_x-2][index_y-2];

                        if (up_up_left.piece==0)
                        {
                            return true;
                        }
                    }

                }catch (Exception e)
                {

                }
                 if (curTile.isKing)
                 {
                     try {
                         Tile up_right=comp.tiles[index_x+1][index_y+1];
                         if(up_right.piece==2)
                         {
                             Tile up_up_right=comp.tiles[index_x+2][index_y+2];

                             if (up_up_right.piece==0)
                             {
                                 return true;
                             }
                         }

                     }catch (Exception e)
                     {

                     }

                     try {
                         Tile up_left=comp.tiles[index_x-1][index_y+1];
                         if(up_left.piece==2)
                         {
                             Tile up_up_left=comp.tiles[index_x-2][index_y+2];

                             if (up_up_left.piece==0)
                             {
                                 return true;
                             }
                         }

                     }catch (Exception e)
                     {

                     }
                 }

            }
            if(curTile.piece==2)
            {
                try {
                    Tile up_right=comp.tiles[index_x+1][index_y+1];
                    if(up_right.piece==1)
                    {
                        Tile up_up_right=comp.tiles[index_x+2][index_y+2];

                        if (up_up_right.piece==0)
                        {
                            return true;
                        }
                    }

                }catch (Exception e)
                {

                }

                try {
                    Tile up_left=comp.tiles[index_x-1][index_y+1];
                    if(up_left.piece==1)
                    {
                        Tile up_up_left=comp.tiles[index_x-2][index_y+2];

                        if (up_up_left.piece==0)
                        {
                            return true;
                        }
                    }

                }catch (Exception e)
                {

                }

                if (curTile.isKing)
                {
                    try {
                        Tile up_right=comp.tiles[index_x+1][index_y-1];
                        if(up_right.piece==1)
                        {
                            Tile up_up_right=comp.tiles[index_x+2][index_y-2];

                            if (up_up_right.piece==0)
                            {
                                return true;
                            }
                        }

                    }catch (Exception e)
                    {

                    }

                    try {
                        Tile up_left=comp.tiles[index_x-1][index_y-1];
                        if(up_left.piece==1)
                        {
                            Tile up_up_left=comp.tiles[index_x-2][index_y-2];

                            if (up_up_left.piece==0)
                            {
                                return true;
                            }
                        }

                    }catch (Exception e)
                    {

                    }
                }

            }

            return false;
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            cleanDead();
            isPressed=1;
            System.out.println("Pressed");
            Board comp= b;
            Dimension dim=comp.tiles[0][0].getSize();
            double tile_size= dim.getWidth();
            double loc_x=e.getX();
            double loc_y=e.getY();
            int index_x= (int) Math.nextDown(loc_x/tile_size);
            int index_y= (int) Math.nextDown(loc_y/tile_size);

            selected_x=index_x;
            selected_y=index_y;

            selectAPiece(comp,index_x,index_y);
        }

        @Override
        public void mouseReleased(MouseEvent e) {

            if(isPressed==1)
            {

            Board comp= b;
            if (comp.isFinished()!=0)
                return;

            Dimension dim=comp.tiles[0][0].getSize();
            double tile_size= dim.getWidth();
            double loc_x=e.getX();
            double loc_y=e.getY();
            int index_x= (int) Math.nextDown(loc_x/tile_size);
            int index_y= (int) Math.nextDown(loc_y/tile_size);


                if(moveAPiece(comp, index_x,index_y) & vsAI)
                    aiMakeMove(comp);
            }

        }

        public boolean moveAPiece(Board comp, int index_x, int index_y)
        {

            Tile targetTile=comp.tiles[index_x][index_y];
            int move_num=validMove(targetTile,index_x,index_y,comp);

            if (move_num==1)//Normal Move
            {
                targetTile.addPiece(selectedPiece.piece);
                targetTile.isKing=selectedPiece.isKing;
                selectedPiece.addPiece(0);
                selectedPiece.setDot();
                selectedPiece.isKing=false;
                targetTile.forced=false;

            } else if (move_num==2) { // Eat move
                targetTile.addPiece(selectedPiece.piece);
                targetTile.isKing=selectedPiece.isKing;
                selectedPiece.addPiece(0);
                selectedPiece.setDot();
                selectedPiece.isKing=false;
                Tile between_tile=comp.tiles[(index_x+selected_x)/2][(index_y+selected_y)/2];
                between_tile.setDead();
                comp.removePiece(between_tile.piece);
                between_tile.addPiece(0);
                between_tile.isKing=false;

                if(availableMove(targetTile,comp,index_x,index_y)) // Eat and move
                {
                    targetTile.forced=true;
                    turn=!turn;
                }
                else{
                    targetTile.forced=false;
                }


            }
            else
            {
                isPressed=0;
                selectedPiece.setSelected(false);
                targetTile.setSelected(false);
                selectedPiece=null;
                comp.repaint();
                comp.revalidate();
                return false;
            }
            turn=!turn;
            selectedPiece.setSelected(false);
            targetTile.setSelected(false);
            selectedPiece=null;
            comp.repaint();
            comp.revalidate();

            updateForced(comp);
            if (forcedPieces.size()>0)
            {
                updateForced(comp);
            }

            isPressed=0;


            if(b.isFinished()==1)
            {
                JOptionPane.showMessageDialog(myFrame, "Player 2 won!");
            }
            if(b.isFinished()==2)
            {
                JOptionPane.showMessageDialog(myFrame, "Player 1 won!");
            }
            if(b.isFinished()==3)
            {
                JOptionPane.showMessageDialog(myFrame, "It is a draw!");
            }


            return true;
        }

        public void selectAPiece(Board comp, int index_x, int index_y)
        {

            selectedPiece=comp.tiles[index_x][index_y];

            if(((turn & selectedPiece.piece==1)||(!turn & selectedPiece.piece==2))&(forcedPieces.size()>0 & forcedPieces.contains(selectedPiece)||forcedPieces.size()==0 ))
            {
                selectedPiece.setSelected(true);
                comp.revalidate();
                comp.repaint();
            }
            else
            {
                isPressed=0;
                selectedPiece=null;
            }

        }

        public void updateForced(Board comp)
        {
            forcedPieces.clear();
            for (int i=0;i<comp.tiles.length;i++)
            {
                for (int j=0;j<comp.tiles[i].length;j++)
                {
                    comp.tiles[i][j].forced=false;
                    if((comp.tiles[i][j].piece==1 & turn &availableMove(comp.tiles[i][j],comp,i,j)) ||(comp.tiles[i][j].piece==2 & !turn & availableMove(comp.tiles[i][j],comp,i,j)))
                    {
                        comp.tiles[i][j].forced=true;
                        forcedPieces.add(comp.tiles[i][j]);
                    }

                    if(comp.tiles[i][j].piece==1 & j==0)
                    {
                        comp.tiles[i][j].isKing=true;
                    }
                    if(comp.tiles[i][j].piece==2 & j==7)
                    {
                        comp.tiles[i][j].isKing=true;
                    }
                }
            }
            comp.repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            //if (isPressed==1)
            //System.out.println("Dragging");
        }

        @Override
        public void mouseMoved(MouseEvent e) {


        }

        @Override
        public void actionPerformed(ActionEvent e) {


             if (e.getSource()==o.options)
            {
                System.out.println("Options button pressed");
                om= new OptionsMenu();
                om.addWindowListener(this);
            }
            else if (e.getSource()==o.reset)
            {
                System.out.println("Reset button pressed");
                turn=true;
                forcedPieces = new ArrayList<Tile>();
                isPressed=0;

                b.resetBoard();
                isPressed=0;
                selectedPiece=null;
                selected_x=-1;
                selected_y=-1;
                b.repaint();

                gen=new Generator();
                mm=new Minimax(b.getState(),turn,gen,difficulty);
                mm.generateTree();

            }
        }

        public void cleanDead()
        {
            for (int i=0;i<b.tiles.length;i++)
            {
                for (int j=0;j<b.tiles[i].length;j++)
                {
                    b.tiles[i][j].resetDead();
                    b.tiles[i][j].resetDot();
                }
            }
        }

        public void aiMakeMove(Board b)
        {
            if(b.isFinished()!=0)
            {
                System.out.println("Game Over");
                return;
            }
            ArrayList<PieceType[][]> moveSet= new ArrayList<PieceType[][]>();
            int currentIndex=0;
            cleanDead();

            while (!turn)
            {

                if(currentIndex==0)
                {
                    mm.update(b.getState());
                    Node curNode=mm.cur;
                    if (curNode.turn==true)
                    {
                        System.out.println("Error");
                    }
                    if(curNode.chainMoves.size()>0)
                        moveSet.addAll(curNode.chainMoves);

                    moveSet.add(mm.cur.curState);
                }


                PieceType[][] nextState=moveSet.get(currentIndex);
                currentIndex++;

                if(mm.cur==null)
                {
                    System.out.println("Game over");
                    return;
                }
                int changed_x1=-1;
                int changed_y1=-1;
                int changed_x2=-1;
                int changed_y2=-1;
                PieceType[][] board_State=b.getState();


                for (int i=0;i<nextState.length;i++)
                {

                    for (int j=0;j<nextState[i].length;j++)
                    {
                        if (nextState[i][j]==PieceType.P1_F)
                        {
                            nextState[i][j]=PieceType.P1;
                        }
                        if (nextState[i][j]==PieceType.P2_F)
                        {
                            nextState[i][j]=PieceType.P2;
                        }
                        if (nextState[i][j]==PieceType.P1_F_KING)
                        {
                            nextState[i][j]=PieceType.P1_KING;
                        }
                        if (nextState[i][j]==PieceType.P2_F_KING)
                        {
                            nextState[i][j]=PieceType.P2_KING;
                        }


                        if (!board_State[i][j].equals(nextState[i][j]))
                        {


                            //if (board_State[i][j]!=PieceType.P2_KING ||board_State[i][j]==PieceType.P2 || board_State[i][j]==PieceType.P2_F || board_State[i][j]==PieceType.P2_F_KING)
                            if ((board_State[i][j]==PieceType.P2_KING ||board_State[i][j]==PieceType.P2 || board_State[i][j]==PieceType.P2_F || board_State[i][j]==PieceType.P2_F_KING)& nextState[i][j]==PieceType.EMPTY)
                            {
                                    changed_x1=i;
                                    changed_y1=j;

                            }

                            else if(board_State[i][j]==PieceType.EMPTY & (nextState[i][j]==PieceType.P2_KING ||nextState[i][j]==PieceType.P2 || nextState[i][j]==PieceType.P2_F || nextState[i][j]==PieceType.P2_F_KING))

                            {
                                changed_x2=i;
                                changed_y2=j;
                            }
                        }
                    }

                }

                if (changed_x2<0 || changed_x1<0 || changed_y1<0 || changed_y2<0)
                {
                    System.out.println("Error");
                }
                selected_x=changed_x1;
                selected_y=changed_y1;
                selectAPiece(b,changed_x1,changed_y1);
                moveAPiece(b,changed_x2,changed_y2);
                //b.setState(nextState);
                b.repaint();
                if(b.isFinished()!=0)
                {
                    System.out.println("Game Over");
                    break;
                }
            }
            b.repaint();
            b.revalidate();
        }

        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
            o.colors=om.colors;
        }

        @Override
        public void windowClosed(WindowEvent e) {
            o.colors=om.colors;
            b.colors=om.colors;
            vsAI=om.vsAI;
            difficulty=om.difficulty;
            b.updateColors();
            o.reset.doClick();
        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {

        }
    }
    public static class Options extends JPanel {

        JButton options;
        JButton reset;
        JLabel title;
        JPanel titlePanel;
        Color squere1= Color.BLUE;
        Color squere2 = Color.LIGHT_GRAY;
        Color p2_color = Color.BLACK;
        Color p1_color = Color.WHITE;
        Color[] colors;
        public Options()
        {
            titlePanel= new JPanel();
            colors=new Color[]{squere1,squere2,p1_color,p2_color};
            options=new JButton("Options");
            reset=new JButton("Reset");
            title=new JLabel("Welcome to the Checkers Game");
            Font font = new Font("Verdana", Font.BOLD, 14);
            title.setFont(font);
            GridBagLayout gb_l=new GridBagLayout();
            this.setLayout(gb_l);
            GridBagConstraints gb_c=new GridBagConstraints();
            titlePanel.add(title);
            gb_c.gridx=0;
            gb_c.gridy=0;
            gb_c.anchor=GridBagConstraints.NORTH;
            gb_l.setConstraints(titlePanel,gb_c);
            this.add(titlePanel);
            gb_c.gridx=0;
            gb_c.gridy=2;
            gb_c.anchor=GridBagConstraints.CENTER;
            gb_l.setConstraints(options,gb_c);
            this.add(options);

            gb_c.gridx=0;
            gb_c.gridy=1;
            gb_c.anchor=GridBagConstraints.CENTER;
            gb_l.setConstraints(reset,gb_c);
            this.add(reset);


        }

        public void setActionListeners(ActionListener al)
        {
            options.addActionListener(al);
            reset.addActionListener(al);
        }

    }
    public static class Board extends JPanel {

        int p1_pieces;
        int p2_pieces;
        Color squere1;
        Color squere2;
        Color p1_color;
        Color p2_color;
        Color[] colors;
        BufferedImage king_img;
        BufferedImage cross_img;
        Tile[][] tiles;
        boolean isDraw=false;
        public Board(Color[] colors)
        {
            BufferedImage img = null;
            BufferedImage img2 = null;
            try {
                img = ImageIO.read(new File("king.gif"));
                img2 = ImageIO.read(new File("cross.png"));
                king_img=resize(img,60,60);
                cross_img=resize(img2,60,60);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            this.colors=colors;
            squere1=colors[0];
            squere2=colors[1];
            p1_color=colors[2];
            p2_color=colors[3];

            tiles= new Tile[8][];
            GridBagLayout gb_l=new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();
            this.setLayout(gb_l);
            boolean isBlack=false;
            for (int i=0;i<8;i++)
            {
                Tile[] jp_arr = new Tile[8];
                for (int j=0;j<8;j++) {

                    Tile jp;
                    if (isBlack) {
                        jp = new Tile(squere1,p1_color,p2_color,king_img,cross_img);
                        jp.piece=0;
                        isBlack = false;
                    } else {
                        jp = new Tile(squere2,p1_color,p2_color,king_img,cross_img);
                        jp.piece=0;
                        isBlack = true;
                    }
                    jp.setSize(150,150);
                    c.gridx=i;
                    c.gridy=j;
                    c.ipady = 80;
                    c.ipadx = 80;
                    c.anchor=GridBagConstraints.WEST;

                    gb_l.setConstraints(jp,c);
                    this.add(jp);
                    jp_arr[j]=jp;
                }
                tiles[i]=jp_arr;
                isBlack = !isBlack;

            }
            this.setVisible(true);

        }

        public void updateColors()
        {
            squere1=colors[0];
            squere2=colors[1];
            p1_color=colors[2];
            p2_color=colors[3];
            boolean isBlack=false;
            for (int i=0;i<8;i++)
            {
                for (int j=0;j<8;j++) {

                    tiles[i][j].c1=p1_color;
                    tiles[i][j].c2=p2_color;
                    tiles[i][j].king_img=king_img;
                    tiles[i][j].cross=cross_img;
                    if (isBlack) {
                        tiles[i][j].background=squere1;
                    } else {
                        tiles[i][j].background=squere2;

                    }
                    tiles[i][j].repaint();
                    tiles[i][j].revalidate();
                    isBlack = !isBlack;

                }
                isBlack = !isBlack;

            }
            repaint();
            revalidate();
            resetBoard();
        }

        public PieceType[][] getState()
        {
            PieceType[][] cur_State=new PieceType[tiles.length][];
            for (int i=0;i<tiles.length;i++)
            {
                PieceType[] arr=new PieceType[tiles[i].length];
                for (int j=0;j<tiles[i].length;j++)
                {

                    if(tiles[i][j].piece==0 )
                    {
                        arr[j]=PieceType.EMPTY;
                    }
                    if(tiles[i][j].piece==1 )
                    {
                        arr[j]=PieceType.P1;
                    }
                    if(tiles[i][j].piece==2 )
                    {
                        arr[j]=PieceType.P2;
                    }
                    if(tiles[i][j].piece==1 & tiles[i][j].isKing)
                    {
                        arr[j]=PieceType.P1_KING;
                    }
                    if(tiles[i][j].piece==2 & tiles[i][j].isKing)
                    {
                        arr[j]=PieceType.P2_KING;
                    }
                    if (tiles[i][j].piece==1 & tiles[i][j].forced &! tiles[i][j].isKing)
                    {
                        arr[j]=PieceType.P1_F;
                    }
                    if (tiles[i][j].piece==2 & tiles[i][j].forced &! tiles[i][j].isKing)
                    {
                        arr[j]=PieceType.P2_F;
                    }
                    if (tiles[i][j].piece==1 & tiles[i][j].forced & tiles[i][j].isKing)
                    {
                        arr[j]=PieceType.P1_F_KING;
                    }
                    if (tiles[i][j].piece==2 & tiles[i][j].forced & tiles[i][j].isKing)
                    {
                        arr[j]=PieceType.P2_F_KING;
                    }

                }
                cur_State[i]=arr;
            }
            return cur_State;
        }

        public void setState(PieceType[][] state)
        {
            for (int i=0;i<tiles.length;i++)
            {
                for (int j=0;j<tiles[i].length;j++)
                {

                    if(state[i][j]==PieceType.P1_F_KING)
                    {
                        tiles[i][j].piece=1;
                        tiles[i][j].isKing=true;
                        tiles[i][j].forced=true;
                    }else if(state[i][j]==PieceType.P1)
                    {
                        tiles[i][j].piece=1;
                        tiles[i][j].isKing=false;
                        tiles[i][j].forced=false;
                    }else if(state[i][j]==PieceType.P1_F)
                    {
                        tiles[i][j].piece=1;
                        tiles[i][j].isKing=false;
                        tiles[i][j].forced=true;
                    }
                    else if(state[i][j]==PieceType.P1_KING)
                    {
                        tiles[i][j].piece=1;
                        tiles[i][j].isKing=true;
                        tiles[i][j].forced=false;
                    }else if(state[i][j]==PieceType.EMPTY)
                    {
                        tiles[i][j].piece=0;
                        tiles[i][j].isKing=false;
                        tiles[i][j].forced=false;
                    }else if(state[i][j]==PieceType.P2)
                    {
                        tiles[i][j].piece=2;
                        tiles[i][j].isKing=false;
                        tiles[i][j].forced=false;
                    }else if(state[i][j]==PieceType.P2_F)
                    {
                        tiles[i][j].piece=2;
                        tiles[i][j].isKing=false;
                        tiles[i][j].forced=true;
                    }else if(state[i][j]==PieceType.P2_F_KING)
                    {
                        tiles[i][j].piece=2;
                        tiles[i][j].isKing=true;
                        tiles[i][j].forced=true;
                    }
                    else if(state[i][j]==PieceType.P2_KING)
                    {
                        tiles[i][j].piece=2;
                        tiles[i][j].isKing=true;
                        tiles[i][j].forced=false;
                    }
                }
            }

        }

        public BufferedImage resize(BufferedImage img, int newW, int newH) {
            Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
            BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = dimg.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();

            return dimg;
        }

        public  void resetBoardv2()
        {
            for(int x=0;x<tiles.length;x++)
            {
                for(int y=0;y<tiles[x].length;y++) {
                    Tile cur_tile = tiles[x][y];
                    cur_tile.addPiece(0);
                    cur_tile.isKing=false;
                    cur_tile.is_Selected=false;
                }
            }

            //p2
            Tile cur_tile = tiles[1][0];
            cur_tile.addPiece(2);
            cur_tile = tiles[3][0];
            cur_tile.addPiece(2);
            cur_tile = tiles[5][0];
            cur_tile.addPiece(2);
            cur_tile = tiles[7][0];
            cur_tile.addPiece(2);
            //p1
            cur_tile = tiles[1][6];
            cur_tile.addPiece(1);
            cur_tile = tiles[3][6];
            cur_tile.addPiece(1);
            cur_tile = tiles[5][6];
            cur_tile.addPiece(1);
            cur_tile = tiles[7][6];
            cur_tile.addPiece(1);

            p1_pieces=4;
            p2_pieces=4;
        }


        public  void resetBoard()
        {
            isDraw=false;

            for(int x=0;x<tiles.length;x++)
            {
                for(int y=0;y<tiles[x].length;y++) {
                    Tile cur_tile = tiles[x][y];
                    cur_tile.addPiece(0);
                    cur_tile.isKing=false;
                    cur_tile.is_Selected=false;
                    cur_tile.forced=false;
                }
            }

            //p2
            Tile cur_tile = tiles[1][0];
            cur_tile.addPiece(2);
            cur_tile = tiles[3][0];
            cur_tile.addPiece(2);
            cur_tile = tiles[5][0];
            cur_tile.addPiece(2);
            cur_tile = tiles[7][0];
            cur_tile.addPiece(2);

            cur_tile = tiles[0][1];
            cur_tile.addPiece(2);
            cur_tile = tiles[2][1];
            cur_tile.addPiece(2);
            cur_tile = tiles[4][1];
            cur_tile.addPiece(2);
            cur_tile = tiles[6][1];
            cur_tile.addPiece(2);

            cur_tile = tiles[1][2];
            cur_tile.addPiece(2);
            cur_tile = tiles[3][2];
            cur_tile.addPiece(2);
            cur_tile = tiles[5][2];
            cur_tile.addPiece(2);
            cur_tile = tiles[7][2];
            cur_tile.addPiece(2);

            //p1
            cur_tile = tiles[1][6];
            cur_tile.addPiece(1);
            cur_tile = tiles[3][6];
            cur_tile.addPiece(1);
            cur_tile = tiles[5][6];
            cur_tile.addPiece(1);
            cur_tile = tiles[7][6];
            cur_tile.addPiece(1);

            cur_tile = tiles[0][7];
            cur_tile.addPiece(1);
            cur_tile = tiles[2][7];
            cur_tile.addPiece(1);
            cur_tile = tiles[4][7];
            cur_tile.addPiece(1);
            cur_tile = tiles[6][7];
            cur_tile.addPiece(1);

            cur_tile = tiles[0][5];
            cur_tile.addPiece(1);
            cur_tile = tiles[2][5];
            cur_tile.addPiece(1);
            cur_tile = tiles[4][5];
            cur_tile.addPiece(1);
            cur_tile = tiles[6][5];
            cur_tile.addPiece(1);

            p1_pieces=12;
            p2_pieces=12;
        }

        public int isFinished()
        {
            if (isDraw)
                return 3;
            if (p1_pieces==0)
                return 1;
            else if (p2_pieces==0)
                return 2;
            else
                return 0;
        }

        public void removePiece(int player)
        {
            if (player==1)
                p1_pieces-=1;
            else
                p2_pieces-=1;
        }

        public void addPiece(int x, int y,int player)
        {
            Tile cur_tile=tiles[x][y];
            cur_tile.addPiece(player);
            this.repaint();
            this.revalidate();
        }
    }
    public static class Tile extends JPanel
    {

        boolean dead;
        int dead_piece=0;
        int piece=0;
        boolean isKing=false;
        boolean dot=false;
        boolean forced=false;
        Color background;
        Color c1;
        Color c2;
        Boolean is_Selected=false;
        BufferedImage cross;
        BufferedImage king_img;

        public Tile(Color background,Color c1,Color c2, BufferedImage king_img, BufferedImage cross )
        {
            this.king_img=king_img;
            this.cross=cross;
            this.piece=0;
            this.background=background;
            this.c1=c1;
            this.c2=c2;
            setBackground(background);



        }
        public void setDot()
        {
            dot=true;
        }
        public void resetDot()
        {
            dot=false;
        }
        public void setDead()
        {
                dead_piece=this.piece;
                dead=true;
        }

        public void addPiece(int piece)
        {

            this.piece=piece;

        }

        public void setSelected(Boolean is_Selected)
        {
            this.is_Selected=is_Selected;
        }

        public void resetDead()
        {
            dead_piece=0;
            dead=false;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(background);
            if(piece>0)
            {

                Color cur_color;

                if (piece==1)
                    cur_color=c1;
                else
                    cur_color=c2;
                Dimension dim=this.getSize();
                int r=70;
                if (is_Selected)
                {
                    int red=cur_color.getRed();
                    int green=cur_color.getGreen();
                    int blue=cur_color.getBlue();

                    Color new_color= new Color(red,green,blue,cur_color.getAlpha()-100);
                    cur_color=new_color;
                }
                    if (forced)
                    {
                        g.setColor(Color.green);
                        g.fillOval(dim.height/2-r/2,dim.width/2-r/2,r,r);
                        g.setColor(cur_color);
                        g.fillOval(dim.height/2-r/4,dim.width/2-r/4,r/2,r/2);
                    }
                    else
                    {
                        g.setColor(cur_color);
                        g.fillOval(dim.height/2-r/2,dim.width/2-r/2,r,r);
                    }

                    if (isKing)
                    {
                        g.setColor(Color.RED);
                        g.drawImage(king_img, dim.height/2-king_img.getWidth()/2, dim.width/2-king_img.getHeight()/2, this);
                    }

            }
            else if (dead)
            {
                Color cur_color;

                if (dead_piece==1)
                    cur_color=c1;
                else
                    cur_color=c2;
                Dimension dim=this.getSize();
                int r=70;
                int red=cur_color.getRed();
                int green=cur_color.getGreen();
                int blue=cur_color.getBlue();


                Color new_color= new Color(red,green,blue,cur_color.getAlpha()-100);
                cur_color=new_color;
                g.setColor(cur_color);
                g.fillOval(dim.height/2-r/2,dim.width/2-r/2,r,r);
                g.drawImage(cross, dim.height/2-cross.getWidth()/2, dim.width/2-cross.getHeight()/2, this);
            }
            else if(dot)
            {
                Dimension dim=this.getSize();
                int r=15;
                g.setColor(Color.darkGray);
                g.fillOval(dim.height/2-r/2,dim.width/2-r/2,r,r);
            }

        }

    }


}

