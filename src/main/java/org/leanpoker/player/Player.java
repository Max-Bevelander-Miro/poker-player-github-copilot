package org.leanpoker.player;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Arrays;
import java.util.stream.Stream;

public class Player {

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
        if (isOpeningHand(gameState)) {
            return betCalculator.calculate(gameState, PokerHandEvaluator.evaluateOpeningHand(player.getHole_cards()));
        }

        return getNextHandCalc(gameState, player);
    }

    private static int getNextHandCalc(GameState gameState, PlayerObj player) {
        try {
            Card[] myCards = player.getHole_cards();
            Card[] communityCards = gameState.getCommunity_cards()
                    .toArray(new Card[0]);
            Card[] allCards = Stream.concat(Arrays.stream(myCards), Arrays.stream(communityCards)).toArray(Card[]::new);
            PokerHandRanking ranking = PokerHandEvaluator.evaluateHand(allCards);
            if (ranking.ordinal() > 4) {
                return betCalculator.calculate(gameState, Bet.RAISE);
            }

            if (ranking.ordinal() > 2) {
                return betCalculator.calculate(gameState, Bet.MATCH);
            }
            return betCalculator.calculate(gameState, Bet.FOLD);
        } catch (Exception ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
            throw ex;
        }
    }

    private static boolean isOpeningHand(GameState gameState) {
        return gameState.getCommunity_cards().isEmpty();
    }

    public static void showdown(JsonNode game) {
    }
}
