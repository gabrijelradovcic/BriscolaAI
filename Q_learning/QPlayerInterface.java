package Q_learning;
import GameEngine.*;
import NeuroEvolution.Game.GameState;

public interface QPlayerInterface {
    public int getDecision(GameState state,Briscola brisc);
    public int StatefromGame(GameState state);
}
