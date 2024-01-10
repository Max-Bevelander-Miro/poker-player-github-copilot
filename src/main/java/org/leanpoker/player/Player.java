package org.leanpoker.player;

import com.fasterxml.jackson.databind.JsonNode;

public class Player {

    static PokerHandEvaluator pokerHandEvaluator = new PokerHandEvaluator();
    static BetCalculator betCalculator = new BetCalculator();
    static final String VERSION = "Default Java folding player";

    public static int betRequest(JsonNode request) {
        return 0;
    }

    public static int betRequest(GameState gameState) {

        PlayerObj player = gameState.getPlayers().stream()
                .filter(playerObj -> playerObj.getHole_cards() != null)
                .filter(p -> p.getHole_cards().length > 0)
                .findFirst()
                .orElse(null);
        if(player == null){
            return 0;
        }
        //is opening hand
        if (isOpeningHand(gameState)){
            return betCalculator.calculate(gameState, PokerHandEvaluator.evaluateOpeningHand(player.getHole_cards()));
        }
        return 0;
    }

    private static boolean isOpeningHand(GameState gameState) {
        return gameState.getCommunity_cards().isEmpty();
    }

    public static void showdown(JsonNode game) {
    }
}
