

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Minimax {

    PieceType[][] startState;
    Generator gen;
    Node cur;
    Queue<Node> currentLeaves;
    Node root;
    int max_depth=4;
    boolean turn;

    public Minimax(PieceType[][] startState,boolean turn,Generator gen, int difficulty)
    {

        max_depth=difficulty;
        this.turn=turn;
        this.gen=gen;
        this.startState=startState;
        root=new Node(startState,null);
    }

    public void update(PieceType[][] curState)
    {
        if (cur==null)
        {
            cur=root;
        }


        for (int i=0;i<cur.next.size();i++)
        {
            PieceType[][] a_state=cur.next.get(i).curState;

            boolean breaked=false;
            for (int j=0;j<a_state.length;j++)
            {
                for (int k=0;k<a_state[j].length;k++)
                {

                    if(!curState[j][k].equals(a_state[j][k]))
                    {
                        breaked=true;
                        break;
                    }

                }
                if (breaked)
                    break;
            }
            if (!breaked)
            {
                cur=cur.next.get(i);
                pruneTree(cur);
            }
        }


        int max=0;
        int index=0;
        for (int i=0;i<cur.next.size();i++)
        {
            if (cur.next.get(i).score>max)
                index=i;
        }
        cur=cur.next.get(index);
        max_depth +=2;
        generateTree();


    }

    public void generateTree()
    {
        if (currentLeaves==null)
        {
            currentLeaves= new LinkedList<Node>();
            currentLeaves.add(root);
        }
        else if (currentLeaves.size()==0)
            return;


        int cur_depth=currentLeaves.peek().depth;
        while ( currentLeaves.peek().depth<max_depth )
        {
            if (cur_depth!=currentLeaves.peek().depth)
            {
                cur_depth=currentLeaves.peek().depth;
            }
            Node curNode=currentLeaves.remove();


            ArrayList<Node> next_states=gen.genStates(curNode,!curNode.turn);
            curNode.addNodes(next_states);
            currentLeaves.addAll(curNode.next);

            if(currentLeaves.size()==0)
                break;



        }
        if (currentLeaves.size()>0)
        {
            evaluateTree();

        }

        System.out.println("Current Tree Dept: "+max_depth);

    }
    public void evaluateTree()
    {
        int iter=currentLeaves.size();
        int i=0;
        while (i<iter)
        {
            i++;
            Node n=currentLeaves.remove();
            n.score=calculateHeuristic(n.curState);
            n.prev.backPropagate(n.score);
            currentLeaves.add(n);
        }
    }

    public void pruneTree(Node p1Node)
    {
        System.out.println("Tree size: "+currentLeaves.size());
        int iter=currentLeaves.size();
        int i=0;
        while (i<iter)
        {
            i++;
            Node n=currentLeaves.remove();
            Node cur=n;
            while (cur.prev!=null)
            {
                cur=cur.prev;
                if (cur.node_id==p1Node.node_id)
                    currentLeaves.add(n);
            }


        }
        System.out.println("Pruned Tree size: "+currentLeaves.size());
    }

    public int calculateHeuristic(PieceType[][] curBoard)
    {
        int number_p1=0;
        int number_p2=0;

        for (int i=0;i<curBoard.length;i++)
        {
            for (int j =0;j<curBoard[i].length;j++)
            {
                if (curBoard[i][j]==PieceType.P1)
                {
                    number_p1+=1;
                }
                else if (curBoard[i][j]==PieceType.P1_F)
                {
                    number_p1+=2;
                }
                else if (curBoard[i][j]==PieceType.P1_F_KING)
                {
                    number_p1+=8;
                }else if (curBoard[i][j]==PieceType.P1_KING)
                {
                    number_p1+=5;
                }
                else if (curBoard[i][j]==PieceType.P2||curBoard[i][j]==PieceType.P2_F)
                {
                    number_p2+=1;
                }
                else if (curBoard[i][j]==PieceType.P2_F)
                {
                    number_p2+=2;
                }
                else if (curBoard[i][j]==PieceType.P2_F_KING)
                {
                    number_p2+=8;
                }
                else if (curBoard[i][j]==PieceType.P2_KING)
                {
                    number_p2+=5;
                }
            }
        }
        return number_p2-number_p1;
    }


}
