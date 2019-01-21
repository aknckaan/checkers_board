import java.util.ArrayList;

public class Node
{

    static int id=0;
    ArrayList<PieceType[][]> chainMoves;
    int node_id;
    int depth=0;
    Node prev;
    ArrayList<Node> next;
    PieceType[][] curState;
    ArrayList<PieceType[][]> pastStates=new  ArrayList<PieceType[][]>();
    ArrayList<Integer> anchestors= new ArrayList<Integer>();
    boolean turn;
    int score=0;
    boolean calculated=false;

    public Node(PieceType[][] curState, ArrayList<PieceType[][]> pastStates,PieceType[][] prevState,boolean turn)
    {
        chainMoves = new ArrayList<PieceType[][]>();
        node_id=id;
        id+=1;
        this.turn=turn;
        this.curState=curState;
        prev=null;
        next=new ArrayList<Node>();
        copy(pastStates);
        if (prevState!=null)
            pastStates.add(prevState);

    }

    public Node(PieceType[][] curState, ArrayList<PieceType[][]> pastStates)
    {
        chainMoves = new ArrayList<PieceType[][]>();
        node_id=id;
        id+=1;
        this.curState=curState;
        prev=null;
        next=new ArrayList<Node>();
        if (pastStates!=null)
            copy(pastStates);
    }



    public void copy(ArrayList<PieceType[][]> pastStates)
    {
        for (int i=0;i<pastStates.size();i++)
        {
            this.pastStates.add(pastStates.get(i));
        }
    }

    public void resetScore()
    {
        score=0;
        calculated=false;
        if (prev!=null)
            prev.resetScore();
    }

    public void backPropagate(int score)
    {
        if (!calculated)
        {
            this.score=score;
            return;
        }

        if(turn & this.score<score)
        {
            this.score=score;
        }

        if(!turn & this.score>score)
        {
            this.score=score;
        }

        calculated=true;

        if (prev!=null)
        prev.backPropagate(score);

    }

    public boolean isLooping()
    {
        for (int i=0;i<pastStates.size();i++)
        {
            if(pastStates.get(i).equals(curState))
            {
                return true;
            }
        }
        return false;
    }
    public void addNode(Node n)
    {
        next.add(n);
    }
    public void addNodes(ArrayList<Node> newStates)
    {
        for (int i=0;i<newStates.size();i++)
        {
            Node n=newStates.get(i);
            n.depth=this.depth+1;

            if (!n.isLooping())
            {
                n.prev=this;
                n.anchestors.add(node_id);
                next.add(n);
            }

        }


    }

}