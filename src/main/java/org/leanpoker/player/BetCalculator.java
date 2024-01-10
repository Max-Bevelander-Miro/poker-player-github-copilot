package org.leanpoker.player;

public class BetCalculator {

    public int calculate(GameState gameState, Bet bet) {
        PlayerObj currentPlayer = gameState.getPlayers().get(gameState.getIn_action());
        int remaining = currentPlayer.getStack();
        int call = getCall(gameState, currentPlayer);
        switch (bet) {
            case ALL_IN:
                return remaining;
            case ONE_THIRD_RAISE:
                return remaining / 3;
            case MATCH_TO_ONE_THIRD:
                int totalAvailableAtStartOfRound = currentPlayer.getStack() + currentPlayer.getBet();
                if (currentPlayer.getBet() > (totalAvailableAtStartOfRound / 3)) {
                    return 0;
                }
                return call + (3 * gameState.getMinimum_raise());
            case RAISE:
                return Math.min(remaining, call + gameState.getMinimum_raise());
            case MATCH:
                return Math.min(remaining, call);
            case FOLD:
                if (call <= 10) {
                    return call;
                }
                return 0;
            default:
                return 0;
        }
    }

    private int getCall(GameState gameState, PlayerObj currentPlayer) {
        return gameState.getCurrent_buy_in() - currentPlayer.getBet();
    }
}
