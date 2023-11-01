package GameEngine;

import java.util.*;




public class Deck {
    private List<Card> cards;
    public static String[] suits = {"Clubs", "Diamonds", "Hearts", "Spades"};
    public static String[] ranks = {"Ace", "2", "3", "4", "5", "6", "7", "Jack", "Queen", "King"};
    public static int[] values={11,0,10,0,0,0,0,2,3,4};
    public static int[] strenght = {10,1,9,2,3,4,5,6,7,8};

    public Deck(){
        // initialize the deck
        refill();
    }

    public boolean isEmpty(){
        return cards.isEmpty();
    }


    public void shuffle(){
        // shuffle the deck
        Collections.shuffle(cards);
    }

    public Card deal(){
        // deal a card
        return cards.remove(0);
    }
    public Card getBriscola(){
        return cards.get(cards.size()-1);
    }
    public List<Card> cloneCards(){
        return new ArrayList<Card>(cards);
    }
    public void setCards(List<Card> cards){
        this.cards = cards;
    }

    public int size(){
        // return the number of cards left in the deck
        return cards.size();
    }

    public void refill(){
        // fill the deck
        cards = new ArrayList<Card>();
        for(int i=0;i<suits.length;i++){
            for(int j=0;j<ranks.length;j++){
                cards.add(new Card(ranks[j],suits[i],values[j],strenght[j],i+4*j));
            }
        }
        shuffle();
    }

    public void addCard(Card c){
        // add a card to the deck
        cards.add(c);
    }




    public String toString(){
        // return a string representation of the deck
        String s = "";
        for(int i=0;i<cards.size();i++){
            s+=cards.get(i).toString()+"\n";
        }
        return s;
    }
    public Deck clone(){
        Deck newdeck = new Deck();
        newdeck.setCards(new ArrayList<>(cards));
        return newdeck;
    }



    
}
