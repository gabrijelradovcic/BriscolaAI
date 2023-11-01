package GameEngine;

public class Card {
    private String suit;
    private String rank;
    private int pointValue;
    private int strength;
    private int positionOfImage;

    public Card(String cardRank, String cardSuit, int cardPointValue, int cardStrength, int positionOfImage) {
        rank = cardRank;
        suit = cardSuit;
        pointValue = cardPointValue;
        strength = cardStrength;
        this.positionOfImage = positionOfImage;
    }

    public String suit() {
        return suit;
    }

    public String rank() {
        return rank;
    }

    public int pointValue() {
        return pointValue;
    }
    public int cardStrength() {
        return strength;
    }

    public boolean matches(Card otherCard) {
        return otherCard.suit().equals(suit) && otherCard.rank().equals(rank) && otherCard.pointValue() == pointValue;
    }

    public String toString() {
        return rank + " of " + suit + " (point value = " + pointValue + ")";
    }

    public int getPositionOfImage(){
        return positionOfImage;
    }

    public Card clone(){
        return new Card(rank, suit, pointValue, strength, positionOfImage);
    }
}