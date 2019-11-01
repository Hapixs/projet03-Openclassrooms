package oc.projet03.game.entitys;

import oc.projet03.game.Game;

abstract public class CraftPlayer implements Player {
    private Game game;
    public CraftPlayer(Game g) {
        game = g;
    }
    @Override
    public Game game() {
        return game;
    }
}
