package oc.projet03.game.entitys;

import oc.projet03.game.Game;

abstract public class CraftPlayer implements Player {
    private Game game;
    private int rmTry;
    private int maxTry;
    public CraftPlayer(Game g, int maxTry) {
        game = g;
        this.maxTry=maxTry;
        this.rmTry=maxTry;
    }
    @Override
    public Game game() {
        return game;
    }
    @Override
    public int remainTry() {
        return rmTry;
    }
    @Override
    public void decrementTry() {
        rmTry--;
    }
    @Override
    public void resetData() {
        rmTry=maxTry;
    }
}
