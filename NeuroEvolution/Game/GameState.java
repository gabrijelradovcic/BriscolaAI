package NeuroEvolution.Game;


public class GameState{
    public int yourScore;
    public int opponentScore;
    public int BriscolaSuit;
    public int[] usedCardnumbers;
    public int[] usedCardsuits;
    public int[] yourCardnumbers;
    public int[] yourCardsuits;
    public GameState(int yourScore, int opponentScore, int BriscolaSuit, int[] usedCardnumbers, int[] usedCardsuits, int[] yourCardnumbers, int[] yourCardsuits){
        this.yourScore=yourScore;
        this.opponentScore=opponentScore;
        this.BriscolaSuit=BriscolaSuit;
        this.usedCardnumbers=usedCardnumbers;
        this.usedCardsuits=usedCardsuits;
        this.yourCardnumbers=yourCardnumbers;
        this.yourCardsuits=yourCardsuits;
    }
    
}
