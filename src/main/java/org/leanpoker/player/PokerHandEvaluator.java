package org.leanpoker.player;

import java.util.*;

public class PokerHandEvaluator {

    public static PokerHandRanking evaluateHand(Card[] hand) {
            if (isRoyalFlush(hand)) {
                return PokerHandRanking.ROYAL_FLUSH;
            } else if (isStraightFlush(hand)) {
                return PokerHandRanking.STRAIGHT_FLUSH;
            } else if (isFourOfAKind(hand)) {
                return PokerHandRanking.FOUR_OF_A_KIND;
            } else if (isFullHouse(hand)) {
                return PokerHandRanking.FULL_HOUSE;
            } else if (isFlush(hand)) {
                return PokerHandRanking.FLUSH;
            } else if (isStraight(hand)) {
                return PokerHandRanking.STRAIGHT;
            } else if (isThreeOfAKind(hand)) {
                return PokerHandRanking.THREE_OF_A_KIND;
            } else if (isTwoPair(hand)) {
                return PokerHandRanking.TWO_PAIR;
            } else if (isPair(hand)) {
                return PokerHandRanking.PAIR;
            } else {
                return PokerHandRanking.HIGH_CARD;
            }
        }

    private static boolean isFlush(Card[] hand) {
        String suit = hand[0].getSuit();
        for (Card card : hand) {
            if (!card.getSuit().equals(suit)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isStraight(Card[] hand) {
        int[] ranks = new int[hand.length];
        for (int i = 0; i < hand.length; i++) {
            ranks[i] = hand[i].getRankAsNumber();
        }
        Arrays.sort(ranks);
        for (int i = 0; i < ranks.length - 1; i++) {
            if (ranks[i] + 1 != ranks[i + 1]) {
                return false;
            }
        }
        return true;
    }

    private static boolean isThreeOfAKind(Card[] hand) {
        Map<Integer, Integer> rankCounts = new HashMap<>();
        for (Card card : hand) {
            rankCounts.put(card.getRankAsNumber(), rankCounts.getOrDefault(card.getRankAsNumber(), 0) + 1);
        }
        return rankCounts.values().contains(3);
    }

    private static boolean isTwoPair(Card[] hand) {
        Map<Integer, Integer> rankCounts = new HashMap<>();
        for (Card card : hand) {
            rankCounts.put(card.getRankAsNumber(), rankCounts.getOrDefault(card.getRankAsNumber(), 0) + 1);
        }
        int pairCount = 0;
        for (int count : rankCounts.values()) {
            if (count == 2) {
                pairCount++;
            }
        }
        return pairCount == 2;
    }

    private static boolean isPair(Card[] hand) {
        Map<Integer, Integer> rankCounts = new HashMap<>();
        for (Card card : hand) {
            rankCounts.put(card.getRankAsNumber(), rankCounts.getOrDefault(card.getRankAsNumber(), 0) + 1);
        }
        return rankCounts.values().contains(2);
    }
    private static boolean isFourOfAKind(Card[] hand) {
        Map<Integer, Integer> rankCounts = new HashMap<>();
        for (Card card : hand) {
            rankCounts.put(card.getRankAsNumber(), rankCounts.getOrDefault(card.getRankAsNumber(), 0) + 1);
        }
        return rankCounts.values().contains(4);
    }

    private static boolean isFullHouse(Card[] hand) {
        Map<Integer, Integer> rankCounts = new HashMap<>();
        for (Card card : hand) {
            rankCounts.put(card.getRankAsNumber(), rankCounts.getOrDefault(card.getRankAsNumber(), 0) + 1);
        }
        return rankCounts.values().contains(3) && rankCounts.values().contains(2);
    }

    private static boolean isStraightFlush(Card[] hand) {
        return isFlush(hand) && isStraight(hand);
    }

    private static boolean isRoyalFlush(Card[] hand) {
        if (!isFlush(hand)) {
            return false;
        }
        int[] ranks = new int[hand.length];
        for (int i = 0; i < hand.length; i++) {
            ranks[i] = hand[i].getRankAsNumber();
        }
        Arrays.sort(ranks);
        return ranks[0] == 10 && ranks[1] == 11 && ranks[2] == 12 && ranks[3] == 13 && ranks[4] == 14;
    }
}