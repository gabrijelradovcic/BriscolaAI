package GameEngine;


import Players.Player;

public class Briscola {

    public Player player1;
    public Player player2;
    public Card briscola;
    public boolean firstIsOnTurn = true;
    public Deck deck;

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Deck getDeck() {
        return deck;
    }
    public void setDeck(Deck deck){
        this.deck = deck;
    }
    public void setfirstIsOnTurn(boolean turn){
        this.firstIsOnTurn = turn;
    }
    public void setBriscolaCard(Card briscola){
        this.briscola = briscola;
    }

    public Briscola(String name1, String name2) {
        player1 = new Player(name1);
        player2 = new Player(name2);
        start();
    }

    public Briscola(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        start();
    }

    public void dealCards() {
        for (int i = 0; i < 3; i++) {
            player1.addCard(deck.deal());
            player2.addCard(deck.deal());
        }
        briscola = deck.getBriscola();
    }

    public void start() {
        deck = new Deck();
        dealCards();
    }

    public void gamePlay() {
        while (!deck.isEmpty() && !player1.isHandEmpty()) {

        }
        getWinner();
    }

    public Player getWinner() {
        if (player1.getPoints() > player2.getPoints()) {
            return player1;
        } else if (player1.getPoints() < player2.getPoints()) {
            return player2;
        } else {
            return null;
        }
    }

    public Player checkWin(Card cardofplayer1, Card cardofplayer2){
        if(firstIsOnTurn){
            if(cardofplayer1.suit().equals(cardofplayer2.suit())){
                if(cardofplayer1.cardStrength()>cardofplayer2.cardStrength()){
                    firstIsOnTurn = true;
                    return player1;
                }else{
                    firstIsOnTurn = false;
                    return player2;
                }
            }else{
                    if(cardofplayer2.suit().equals(briscola.suit())){
                        firstIsOnTurn = false;
                        return player2;
                    }
                    else{
                        firstIsOnTurn = true;
                        return player1;
                    }
            }
        }
        else{
                if(cardofplayer2.suit().equals(cardofplayer1.suit())){
                    if(cardofplayer1.cardStrength()>cardofplayer2.cardStrength()){
                        firstIsOnTurn = true;
                        return player1;
                    }else{
                        firstIsOnTurn = false;
                        return player2;
                    }
                }else{
                    if(cardofplayer1.suit().equals(briscola.suit())){
                        firstIsOnTurn = true;
                        return player1;
                    }else{
                        firstIsOnTurn = false;
                        return player2;
                    }
                }
            }
        }
    public boolean isOver(){
        if(deck.isEmpty()&&player1.isHandEmpty()&&player2.isHandEmpty()){
            return true;
        }
        return false;
    }

    public boolean isOverQuick(){
        //check if a player has more then 60 points
        if(player1.getPoints()>60||player2.getPoints()>60){
            return true;
        }
        return false;
    }

    public Briscola clone(){
        Briscola newBriscola = new Briscola(player1.clone(), player2.clone());
        newBriscola.setBriscolaCard(briscola.clone());
        newBriscola.setfirstIsOnTurn(firstIsOnTurn);
        newBriscola.setDeck(deck.clone());
        return newBriscola;
    }
    

}
