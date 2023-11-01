package NeuroEvolution.Game;


import java.util.*;




public class Deck {
    private List<Card> cards;
    public static int[] suits= {1,2,3,4};
    public static int[] ranks= {1,2,3,4,5,6,7,11,12,13};
    public static int[] values={0,11,0,10,0,0,0,0,0,0,0,2,3,4};


    public Deck(){
        // initialize the deck
        refill();
    }

    public Deck copy(){
        Deck d = new Deck();
        d.setCards(new ArrayList<Card>(cards));
        return d;
    }

    public void setCards(List<Card> cards){
        this.cards = cards;
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
        try{
            return cards.remove(0);
        }catch(IndexOutOfBoundsException e){
            return null;
        }
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
                cards.add(new Card(ranks[j],suits[i],values[j]));
            }
        }
        shuffle();
    }

    public void addCard(Card c){
        // add a card to a random position in the deck
        cards.add((int)(Math.random()*cards.size()),c);
    }




    public String toString(){
        // return a string representation of the deck
        String s = "";
        for(int i=0;i<cards.size();i++){
            s+=cards.get(i).toString()+"\n";
        }
        return s;
    }



    
}
