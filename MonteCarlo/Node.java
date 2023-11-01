package MonteCarlo;

import java.util.ArrayList;
import java.util.List;
import GameEngine.Card;
import Players.Player;

public class Node {
    private Card card;
    private Player player;
    private List<Node>children = new ArrayList<>();
    private Node parent;
    private int runs;
    private int wins;

    public Node(Node parent,Player player){
        runs = 0;
        wins = 0;
        this.parent = parent;
        this.player = player;
    }

    public void addChildren(Node child){
        children.add(child);
    }
    public void increaseRuns(){
        runs++;
    }
    public void increaseWins(){
        wins++;
    }
    public List<Node> getChildren(){
        return children;
    }
    public Node getParent(){
        return parent;
    }
    public double getPercentige(){
        return 1.0*wins/runs;
    }

    public Player getPlayer(){
        return player;
    }
    public void setCard(Card card){
        this.card = card;
    }
    public Card getCard(){
        return card;
    }
    public String toString(){
        String name = card.toString();
        return name;
    }
}