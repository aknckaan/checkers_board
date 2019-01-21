import java.util.ArrayList;

public class Generator {

    PieceType[][] startState;
    public enum command{LEFT_UP, RIGHT_UP,RIGHT_DOWN,LEFT_DOWN,D_LEFT_UP, D_RIGHT_UP,D_RIGHT_DOWN,D_LEFT_DOWN};

    Node currentNode;
    public Generator()
    {
    }

    public ArrayList<Node> genStates(Node node, boolean turn)
    {
        currentNode = node;
        PieceType[][] state=node.curState;
        ArrayList<Node> states=new ArrayList<Node>();
        PieceType[][] new_States= setForced( state,!turn);
        ArrayList<Tuple> pieceArray=genMovement(new_States,turn);
        for (int i=0;i<pieceArray.size();i++)
        {
            Node newNode=new Node(pieceArray.get(i).state,node.pastStates);
            newNode.turn=turn;
            newNode.chainMoves.addAll(pieceArray.get(i).path);
            states.add(newNode);
        }

        return states;
    }

    public PieceType[][]  setForced(PieceType[][] old_state,boolean turn)
    {

        PieceType[][] state=cloneArray(old_state);

            for (int i=0;i<state.length;i++)
            {
                for (int j=0;j<state[i].length;j++)
                {
                    if (state[i][j]!=PieceType.EMPTY)
                    {


                        ArrayList<command> available_moves=availableMove( state[i][j], state, i, j);

                        if ((state[i][j]==PieceType.P2 || state[i][j]==PieceType.P2_F))
                        {
                            if(j==7)
                            {
                                state[i][j]=PieceType.P2_KING;
                            }

                            if (turn&(available_moves.contains(command.D_LEFT_DOWN)||available_moves.contains(command.D_LEFT_UP)||available_moves.contains(command.D_RIGHT_DOWN)||available_moves.contains(command.D_RIGHT_UP)))
                            {
                                state[i][j]=PieceType.P2_F;

                                if(j==7)
                                {
                                    state[i][j]=PieceType.P2_F_KING;
                                }
                            }
                            else if(state[i][j]==PieceType.P2_F)
                            {
                                state[i][j]=PieceType.P2;
                            }

                        }
                        else if ((state[i][j]==PieceType.P2_KING || state[i][j]==PieceType.P2_F_KING))
                        {

                            if (turn&(available_moves.contains(command.D_LEFT_DOWN)||available_moves.contains(command.D_LEFT_UP)||available_moves.contains(command.D_RIGHT_DOWN)||available_moves.contains(command.D_RIGHT_UP)))
                            {
                                state[i][j]=PieceType.P2_F_KING;
                            }
                            else if(state[i][j]==PieceType.P2_F_KING)
                                state[i][j]=PieceType.P2_KING;
                        }

                        else if ((state[i][j]==PieceType.P1 || state[i][j]==PieceType.P1_F))
                        {

                            if(j==0)
                            {
                                state[i][j]=PieceType.P1_KING;
                            }

                            if (!turn&(available_moves.contains(command.D_LEFT_DOWN)||available_moves.contains(command.D_LEFT_UP)||available_moves.contains(command.D_RIGHT_DOWN)||available_moves.contains(command.D_RIGHT_UP)))
                            {
                                state[i][j]=PieceType.P1_F;
                                if(j==0)
                                {
                                    state[i][j]=PieceType.P1_F_KING;
                                }
                            }
                            else if(state[i][j]==PieceType.P1_F)
                            {
                                state[i][j]=PieceType.P1;
                            }

                        }
                        else if ((state[i][j]==PieceType.P1_KING || state[i][j]==PieceType.P1_F_KING))
                        {

                            if (!turn&(available_moves.contains(command.D_LEFT_DOWN)||available_moves.contains(command.D_LEFT_UP)||available_moves.contains(command.D_RIGHT_DOWN)||available_moves.contains(command.D_RIGHT_UP)))
                            {
                                state[i][j]=PieceType.P1_F_KING;
                            }
                            else if(state[i][j]==PieceType.P1_F_KING)
                                state[i][j]=PieceType.P1_KING;
                        }
                    }

                }
            }
            return state;
    }

    public ArrayList<Tuple> genMovement(PieceType[][] state, boolean turn)
    {
        ArrayList<Tuple> states=new ArrayList<Tuple>();

        boolean forced_available=false;
        boolean reset=false;
        boolean skip=false;
        ArrayList<Integer> forced_x=new ArrayList<Integer>();
        ArrayList<Integer> forced_y=new ArrayList<Integer>();

        for(int i=0;i<state.length;i++) {
            for (int j = 0; j < state[i].length; j++) {
                if(!turn & (state[i][j]==PieceType.P2_F || state[i][j]==PieceType.P2_F_KING))
                {
                    forced_x.add(i);
                    forced_y.add(j);
                    forced_available=true;
                }
                if(turn & (state[i][j]==PieceType.P1_F || state[i][j]==PieceType.P1_F_KING))
                {
                    forced_x.add(i);
                    forced_y.add(j);
                    forced_available=true;
                }
            }
        }

        if (forced_available)
        {
            for(int w=0;w<forced_x.size();w++)
            {
                int i = forced_x.get(w);
                int j= forced_y.get(w);
                int moved_x=-1;
                int moved_y=-1;
                ArrayList<command> movements=availableMove(state[i][j],state,i,j);

                for (int k=0;k<movements.size();k++)
                {
                    Tuple t=new Tuple();
                    PieceType [][] newState=cloneArray(state);
                    command elm=movements.get(k);
                    if(elm==command.RIGHT_UP)
                    {
                        newState[i+1][j-1]=state[i][j];
                        newState[i][j]=PieceType.EMPTY;
                    }
                    else if(elm==command.D_RIGHT_UP)
                    {
                        newState[i+2][j-2]=state[i][j];
                        newState[i+1][j-1]=PieceType.EMPTY;
                        newState[i][j]=PieceType.EMPTY;
                        moved_x=i+2;
                        moved_y=j-2;

                    }else if(elm==command.LEFT_UP)
                    {
                        newState[i-1][j-1]=state[i][j];
                        newState[i][j]=PieceType.EMPTY;
                    }
                    else if(elm==command.D_LEFT_UP)
                    {
                        newState[i-2][j-2]=state[i][j];
                        newState[i-1][j-1]=PieceType.EMPTY;
                        newState[i][j]=PieceType.EMPTY;
                        moved_x=i-2;
                        moved_y=j-2;
                    }
                    else if(elm==command.RIGHT_DOWN)
                    {
                        newState[i+1][j+1]=state[i][j];
                        newState[i][j]=PieceType.EMPTY;
                    }
                    else if(elm==command.D_RIGHT_DOWN)
                    {
                        newState[i+2][j+2]=state[i][j];
                        newState[i+1][j+1]=PieceType.EMPTY;
                        newState[i][j]=PieceType.EMPTY;
                        moved_x=i+2;
                        moved_y=j+2;
                    }
                    else if(elm==command.LEFT_DOWN)
                    {
                        newState[i-1][j+1]=state[i][j];
                        newState[i][j]=PieceType.EMPTY;
                    }
                    else if(elm==command.D_LEFT_DOWN)
                    {
                        newState[i-2][j+2]=state[i][j];
                        newState[i-1][j+1]=PieceType.EMPTY;
                        newState[i][j]=PieceType.EMPTY;
                        moved_x=i-2;
                        moved_y=j+2;
                    }
                    PieceType [][] trial=setForced(newState,!turn);
                    newState=setForced(newState,turn);
                    t.state=newState;

                    if(trial[moved_x][moved_y]==PieceType.P2_F || trial[moved_x][moved_y]==PieceType.P2_F_KING || trial[moved_x][moved_y]==PieceType.P1_F|| trial[moved_x][moved_y]==PieceType.P1_F_KING)
                    {
                        states.addAll(genNextJump(trial,moved_x,moved_y,turn,t));
                    }
                    else
                    {

                        states.add(t);
                    }

                }
            }
            return states;
        }

        if(! forced_available)
        {
            for(int i=0;i<state.length;i++)
            {
                for (int j=0;j<state[i].length;j++)
                {

                    if (((state[i][j]==PieceType.P2 || state[i][j]==PieceType.P2_F || state[i][j]==PieceType.P2_KING || state[i][j]==PieceType.P2_F_KING)& !turn)||((state[i][j]==PieceType.P1 || state[i][j]==PieceType.P1_F || state[i][j]==PieceType.P1_KING || state[i][j]==PieceType.P1_F_KING)& turn))
                    {

                        ArrayList<command> movements=availableMove(state[i][j],state,i,j);

                        for (int k=0;k<movements.size();k++)
                        {
                            Tuple t=new Tuple();
                            PieceType [][] newState=cloneArray(state);
                            command elm=movements.get(k);
                            if(elm==command.RIGHT_UP)
                            {
                                newState[i+1][j-1]=state[i][j];
                                newState[i][j]=PieceType.EMPTY;
                            }
                            else if(elm==command.D_RIGHT_UP)
                            {
                                newState[i+2][j-2]=state[i][j];
                                newState[i+1][j-1]=PieceType.EMPTY;
                                newState[i][j]=PieceType.EMPTY;
                            }else if(elm==command.LEFT_UP)
                            {
                                newState[i-1][j-1]=state[i][j];
                                newState[i][j]=PieceType.EMPTY;
                            }
                            else if(elm==command.D_LEFT_UP)
                            {
                                newState[i-2][j-2]=state[i][j];
                                newState[i-1][j-1]=PieceType.EMPTY;
                                newState[i][j]=PieceType.EMPTY;
                            }
                            else if(elm==command.RIGHT_DOWN)
                            {
                                newState[i+1][j+1]=state[i][j];
                                newState[i][j]=PieceType.EMPTY;
                            }
                            else if(elm==command.D_RIGHT_DOWN)
                            {
                                newState[i+2][j+2]=state[i][j];
                                newState[i+1][j+1]=PieceType.EMPTY;
                                newState[i][j]=PieceType.EMPTY;
                            }
                            else if(elm==command.LEFT_DOWN)
                            {
                                newState[i-1][j+1]=state[i][j];
                                newState[i][j]=PieceType.EMPTY;
                            }
                            else if(elm==command.D_LEFT_DOWN)
                            {
                                newState[i-2][j+2]=state[i][j];
                                newState[i-1][j+1]=PieceType.EMPTY;
                                newState[i][j]=PieceType.EMPTY;
                            }
                            newState=setForced(newState,turn);
                            t.state=newState;
                            states.add(t);
                        }

                    }

                }
            }
        }



        return states;
    }


    public ArrayList<Tuple> genNextJump(PieceType[][] state,int index_x,int index_y, boolean turn, Tuple tuple)
    {
        ArrayList<Tuple> states=new ArrayList<Tuple>();

        //System.out.println("Jump Called");
        boolean forced_available=false;
        boolean reset=false;
        boolean skip=false;

        if (state[index_x][index_y]==PieceType.P2_F || state[index_x][index_y]==PieceType.P2_F_KING || state[index_x][index_y]==PieceType.P1_F|| state[index_x][index_y]==PieceType.P1_F_KING)
        {
            forced_available=true;
        }

        if (forced_available)
        {
              int i= index_x;
              int j= index_y;
              int moved_x=-1;
              int moved_y=-1;
              boolean isMoved=false;
                ArrayList<command> movements=availableMove(state[i][j],state,i,j);

                for (int k=0;k<movements.size();k++)
                {
                    PieceType [][] newState=cloneArray(state);
                    command elm=movements.get(k);
                    Tuple t=new Tuple();
                    t.path.addAll(tuple.path);
                    t.path.add(tuple.state);

                    if(elm==command.D_RIGHT_UP)
                    {
                        newState[i+2][j-2]=state[i][j];
                        newState[i+1][j-1]=PieceType.EMPTY;
                        newState[i][j]=PieceType.EMPTY;
                        moved_x=i+2;
                        moved_y=j-2;
                        isMoved=true;

                    }
                    if(elm==command.D_LEFT_UP)
                    {
                        newState[i-2][j-2]=state[i][j];
                        newState[i-1][j-1]=PieceType.EMPTY;
                        newState[i][j]=PieceType.EMPTY;
                        moved_x=i-2;
                        moved_y=j-2;
                        isMoved=true;
                    }
                    if(elm==command.D_RIGHT_DOWN)
                    {
                        newState[i+2][j+2]=state[i][j];
                        newState[i+1][j+1]=PieceType.EMPTY;
                        newState[i][j]=PieceType.EMPTY;
                        moved_x=i+2;
                        moved_y=j+2;
                        isMoved=true;
                    }
                     if(elm==command.D_LEFT_DOWN)
                    {
                        newState[i-2][j+2]=state[i][j];
                        newState[i-1][j+1]=PieceType.EMPTY;
                        newState[i][j]=PieceType.EMPTY;
                        moved_x=i-2;
                        moved_y=j+2;
                        isMoved=true;
                    }

                    newState=setForced(newState,!turn);
                    t.state=newState;

                    if(isMoved & newState[moved_x][moved_y]==PieceType.P2_F || newState[moved_x][moved_y]==PieceType.P2_F_KING || newState[moved_x][moved_y]==PieceType.P1_F|| newState[moved_x][moved_y]==PieceType.P1_F_KING)
                    {
                        //currentNode.chainMoves.add(newState);
                        ArrayList<Tuple> tup_arr=genNextJump(newState,moved_x,moved_y,turn,t);
                        states.addAll(tup_arr);
                    }
                    else
                        states.add(t);
                }

            return states;
        }
        return states;
    }


    public PieceType[][] cloneArray(PieceType[][] current)
    {
        PieceType[][] old=new PieceType[current.length][current.length];
        for(int i=0; i<current.length; i++)
            for(int j=0; j<current[i].length; j++)
                old[i][j]=current[i][j];
        return old;
    }

    public ArrayList<command> availableMove(PieceType curTile, PieceType[][] comp,int index_x, int index_y)
    {

        ArrayList<command> movements=new ArrayList<command>();

        boolean notForced=true;
        if(curTile==PieceType.P1 || curTile==PieceType.P1_F || curTile==PieceType.P1_KING || curTile==PieceType.P1_F_KING)
        {
            try {
                PieceType up_right=comp[index_x+1][index_y-1];
                if(up_right==PieceType.P2||up_right==PieceType.P2_KING||up_right==PieceType.P2_F_KING||up_right==PieceType.P2_F)
                {
                    PieceType up_up_right=comp[index_x+2][index_y-2];

                    if (up_up_right==PieceType.EMPTY)
                    {
                        movements.add(command.D_RIGHT_UP);
                        notForced=true;
                    }
                }
                else if (curTile!=PieceType.P1_F & curTile!=PieceType.P1_F_KING &up_right==PieceType.EMPTY & notForced)
                {
                    movements.add(command.RIGHT_UP);
                }

            }catch (Exception e)
            {

            }

            try {
                PieceType up_left=comp[index_x-1][index_y-1];
                if(up_left==PieceType.P2||up_left==PieceType.P2_KING||up_left==PieceType.P2_F_KING||up_left==PieceType.P2_F)
                {
                    PieceType up_up_left=comp[index_x-2][index_y-2];

                    if (up_up_left==PieceType.EMPTY)
                    {
                        movements.add(command.D_LEFT_UP);
                        notForced=true;
                    }
                }
                else if (curTile!=PieceType.P1_F & curTile!=PieceType.P1_F_KING &up_left==PieceType.EMPTY & notForced)
                {
                    movements.add(command.LEFT_UP);
                }

            }catch (Exception e)
            {

            }
            if (curTile==PieceType.P1_KING ||curTile==PieceType.P1_F_KING )
            {
                try {
                    PieceType down_right=comp[index_x+1][index_y+1];
                    if(down_right==PieceType.P2||down_right==PieceType.P2_KING||down_right==PieceType.P2_F_KING||down_right==PieceType.P2_F)
                    {
                        PieceType up_up_right=comp[index_x+2][index_y+2];

                        if (up_up_right==PieceType.EMPTY)
                        {
                            movements.add(command.D_RIGHT_DOWN);
                            notForced=true;
                        }
                    }
                    else if (curTile!=PieceType.P1_F & curTile!=PieceType.P1_F_KING &down_right==PieceType.EMPTY & notForced)
                    {
                        movements.add(command.RIGHT_DOWN);
                    }

                }catch (Exception e)
                {

                }

                try {
                    PieceType down_left=comp[index_x-1][index_y+1];
                    if(down_left==PieceType.P2||down_left==PieceType.P2_KING||down_left==PieceType.P2_F_KING||down_left==PieceType.P2_F)
                    {
                        PieceType up_up_left=comp[index_x-2][index_y+2];

                        if (up_up_left==PieceType.EMPTY)
                        {
                            movements.add(command.D_LEFT_DOWN);
                            notForced=true;
                        }
                    }
                    else if (curTile!=PieceType.P1_F & curTile!=PieceType.P1_F_KING & down_left==PieceType.EMPTY & notForced)
                    {
                        movements.add(command.LEFT_DOWN);
                    }

                }catch (Exception e)
                {

                }
            }

        }
        if(curTile==PieceType.P2 || curTile==PieceType.P2_F || curTile==PieceType.P2_KING || curTile==PieceType.P2_F_KING)
        {
            try {
                PieceType down_right=comp[index_x+1][index_y+1];
                if(down_right==PieceType.P1||down_right==PieceType.P1_KING||down_right==PieceType.P1_F_KING||down_right==PieceType.P1_F)
                {
                    PieceType up_up_right=comp[index_x+2][index_y+2];

                    if (up_up_right==PieceType.EMPTY)
                    {
                        movements.add(command.D_RIGHT_DOWN);
                        notForced=true;
                    }
                }
                else if (curTile!=PieceType.P2_F & curTile!=PieceType.P2_F_KING & down_right==PieceType.EMPTY & notForced)
                {
                    movements.add(command.RIGHT_DOWN);
                }

            }catch (Exception e)
            {

            }

            try {
                PieceType down_left=comp[index_x-1][index_y+1];
                if(down_left==PieceType.P1||down_left==PieceType.P1_KING||down_left==PieceType.P1_F_KING||down_left==PieceType.P1_F)
                {
                    PieceType up_up_left=comp[index_x-2][index_y+2];

                    if (up_up_left==PieceType.EMPTY)
                    {
                        movements.add(command.D_LEFT_DOWN);
                        notForced=true;
                    }
                }
                else if (curTile!=PieceType.P2_F & curTile!=PieceType.P2_F_KING &down_left==PieceType.EMPTY & notForced)
                {
                    movements.add(command.LEFT_DOWN);
                }

            }catch (Exception e)
            {

            }

            if (curTile==PieceType.P2_KING ||curTile==PieceType.P2_F_KING )
            {
                try {
                    PieceType up_right=comp[index_x+1][index_y-1];
                    if(up_right==PieceType.P1||up_right==PieceType.P1_KING||up_right==PieceType.P1_F_KING||up_right==PieceType.P1_F)
                    {
                        PieceType up_up_right=comp[index_x+2][index_y-2];

                        if (up_up_right==PieceType.EMPTY)
                        {
                            movements.add(command.D_RIGHT_UP);
                            notForced=true;
                        }
                    }
                    else if (curTile!=PieceType.P2_F & curTile!=PieceType.P2_F_KING &up_right==PieceType.EMPTY & notForced)
                    {
                        movements.add(command.RIGHT_UP);
                    }

                }catch (Exception e)
                {

                }

                try {
                    PieceType up_left=comp[index_x-1][index_y-1];
                    if(up_left==PieceType.P1||up_left==PieceType.P1_KING||up_left==PieceType.P1_F_KING||up_left==PieceType.P1_F)
                    {
                        PieceType up_up_left=comp[index_x-2][index_y-2];

                        if (up_up_left==PieceType.EMPTY)
                        {
                            movements.add(command.D_LEFT_UP);
                            notForced=true;
                        }
                    }
                    else if (curTile!=PieceType.P2_F & curTile!=PieceType.P2_F_KING &up_left==PieceType.EMPTY & notForced)
                    {
                        movements.add(command.LEFT_UP);
                    }

                }catch (Exception e)
                {

                }
            }

        }

        return movements;
    }

    public class Tuple
    {
        ArrayList<PieceType[][]> path;
        PieceType[][] state;
        public Tuple()
        {
            path=new ArrayList<PieceType[][]>();

        }
    }

}
