package org.leanpoker.player;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.*;
import java.util.stream.Stream;

public class Player {

    static BetCalculator betCalculator = new BetCalculator();
    static final String VERSION = "fightttttt!";

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
            if (ranking.ordinal() > PokerHandRanking.PAIR.ordinal()) {
                return betCalculator.calculate(gameState, Bet.ALL_IN);
            }
            if (ranking.ordinal() == PokerHandRanking.PAIR.ordinal()) {
                // if pair is highest in table
                if (checkIfHighestPair(allCards)) {
                    return betCalculator.calculate(gameState, Bet.ALL_IN);
                }
//                if (checkIfWeakPair(allCards)) {
//                    return betCalculator.calculate(gameState, Bet.FOLD);
//                }
                return betCalculator.calculate(gameState, Bet.FOLD);
            }
            return betCalculator.calculate(gameState, Bet.FOLD);
        } catch (Exception ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
            throw ex;
        }
    }
    //

    private static boolean checkIfHighestPair(Card[] hand) {

        int maxRank = -1;
        int pairRank = -1;

        Map<Integer, Integer> rankCounts = new HashMap<>();
        for (Card card : hand) {
            maxRank = Math.max(maxRank, card.getRankAsNumber());
            if (rankCounts.containsKey(card.getRankAsNumber())) {
                pairRank = card.getRankAsNumber();
            } else {
                rankCounts.put(card.getRankAsNumber(), 1);
            }
        }
        return maxRank == pairRank;
    }

    private static boolean checkIfWeakPair(Card[] hand) {
        int pairRank = -1;

        Map<Integer, Integer> rankCounts = new HashMap<>();
        for (Card card : hand) {
            if (rankCounts.containsKey(card.getRankAsNumber())) {
                pairRank = card.getRankAsNumber();
            } else {
                rankCounts.put(card.getRankAsNumber(), 1);
            }
        }
        Arrays.sort(hand, Comparator.comparing(Card::getRankAsNumber).reversed());
        return hand[1].getRankAsNumber() > pairRank;
    }

    private static boolean isOpeningHand(GameState gameState) {
        return gameState.getCommunity_cards().isEmpty();
    }

    public static void showdown(JsonNode game) {
    }
}
