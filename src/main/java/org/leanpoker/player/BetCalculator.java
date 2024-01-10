package org.leanpoker.player;

public class BetCalculator {

    public int calculate(GameState gameState, Bet bet) {
        switch (bet) {
            case RAISE:
                return getCall(gameState) + gameState.getMinimum_raise();
            case MATCH:
                return getCall(gameState);
            case FOLD:
                return 0;
            default:
                return 0;
        }
    }

    private int getCall(GameState gameState) {
        return gameState.getCurrent_buy_in() - gameState.getPlayers().get(gameState.getIn_action()).getBet();
    }
}
