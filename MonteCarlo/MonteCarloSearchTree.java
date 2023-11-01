package MonteCarlo;


import java.util.List;


import GameEngine.Briscola;
import Players.Player;

public class MonteCarloSearchTree {
    Node root;
    Node lastMove;
    Player player1;
    Player player2;

    public MonteCarloSearchTree() {
        lastMove = root;
    }

    public void nextmove(Briscola briscolaOriginal) {
        Briscola briscola = briscolaOriginal.clone();
        player1 = briscolaOriginal.getPlayer1().clone();
        player2 = briscolaOriginal.getPlayer2().clone();
        briscola.getDeck().shuffle();
        while (briscola.isOver() == false) {
            if(player1.getPoints()>60||player2.getPoints()>60){
                break;
            }
            if(player1.isHandEmpty()){
                break;
            }
            Node firstMove = findNextNode(lastMove, player1);
            Node secondMove = findNextNode(firstMove, player2);
            lastMove = secondMove;
            Player potWinner = briscola.checkWin(firstMove.getCard(), secondMove.getCard());
            potWinner.addPoints(firstMove.getCard().pointValue() + secondMove.getCard().pointValue());
            if (!briscola.getDeck().isEmpty()) {
                player1.addCard(briscola.getDeck().deal());
                player2.addCard(briscola.getDeck().deal());
            }
        }
        Player winner = briscola.getWinner();
        while (lastMove.getParent() != null) {
            if (winner == null) {
                lastMove.increaseRuns();
                lastMove = lastMove.getParent();
                lastMove.increaseRuns();
                continue;
            }
            if (winner.getPoints() == player1.getPoints()) {
                lastMove.increaseRuns();
                lastMove = lastMove.getParent();
                lastMove.increaseRuns();
                lastMove.increaseWins();
            } else {
                lastMove.increaseRuns();
                lastMove.increaseWins();
                lastMove = lastMove.getParent();
                lastMove.increaseRuns();
            }
        }
    }

    public void runMonteCarlo(Briscola briscola) {
        for (int i = 0; i < 10000; i++) {
            nextmove(briscola);
        }
    }

    public Node findNextNode(Node lastmove, Player player) {
        Node nextnode = null;
        int cardNumber1 = (int) (Math.random() * 3);
        while(player.getCard(cardNumber1)==null&&!player.isHandEmpty()){
            cardNumber1 = (int) (Math.random() * 3);
        }
        List<Node> children = lastMove.getChildren();
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getCard().matches(player.getCard(cardNumber1))) {
                nextnode = children.get(i);
                break;
            }
        }
        if (nextnode == null) {
            nextnode = new Node(lastMove, player);
            lastmove.addChildren(nextnode);
            nextnode.setCard(player.getCard(cardNumber1));
        }
        player.removeCard(cardNumber1);
        return nextnode;
    }

    public int getDecision(Briscola briscola) {
        this.root = new Node(null, null);
        lastMove = root;
        runMonteCarlo(briscola);
        List<Node> posibilities = root.getChildren();
        int maxChild = 0;
        if(briscola.getPlayer2().hand[maxChild]==null){
            maxChild = 1;
            if(briscola.getPlayer2().hand[maxChild]==null){
                maxChild = 2;
                
            }
        }
        double maxPercentage = 0;
        for (int i = 0; i < posibilities.size(); i++) {
            if (posibilities.get(i).getPercentige() > maxPercentage) {
                if(briscola.getPlayer2().hand[i]!=null){
                    maxPercentage = posibilities.get(i).getPercentige();
                    maxChild = i;
                }
            }
        }
        return maxChild;
    }
}