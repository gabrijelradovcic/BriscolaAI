package NeuroEvolution.Game;



public class Card {
    private int suit;
    private int rank;
    private int pointValue;

    public Card(int cardRank,int cardSuit, int cardPointValue) {
        rank = cardRank;
        suit = cardSuit;
        pointValue = cardPointValue;
    }

    public int suit() {
        return suit;
    }
    public int rank() {
        return rank;
    }

    public int pointValue() {
        return pointValue;
    }

    public boolean matches(Card otherCard) {
        return otherCard.suit() == suit && otherCard.rank() == rank && otherCard.pointValue() == pointValue;
    }

    public String toString() {
        return rank + " of " + suit + " (point value = " + pointValue + ")";
    }
}