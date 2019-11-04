package oc.projet03.game.entitys;

import oc.projet03.game.Game;

public interface Player {
    Game game();
    int remainTry();
    public void decrementTry();
    public void resetData();
}
