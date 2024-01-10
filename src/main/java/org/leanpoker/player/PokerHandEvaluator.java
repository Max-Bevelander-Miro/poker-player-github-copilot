package org.leanpoker.player;

import java.util.*;

public class PokerHandEvaluator {

    public static Bet evaluateOpeningHand(Card[] hand) {
        if (isGoodHand(hand)) {
            return Bet.ALL_IN;
        } else if (isOneThirdHand(hand)) {
            return Bet.ONE_THIRD_RAISE;
        } else if (isPairFirstHand(hand)) {
            return Bet.RAISE;
        } else if (isSameSuitFirstHand(hand)) {
            return Bet.RAISE;
        } else if (hand[0].getRankAsNumber() >= 13 || hand[1].getRankAsNumber() >= 13) {
            return Bet.RAISE;
        } else if (areConnectingCards(hand) && (hand[0].getRankAsNumber() >= 8 || hand[1].getRankAsNumber() >= 8)) {
            return Bet.MATCH_TO_ONE_THIRD;
        } else if (firstHandContainsHighCard(hand)) {
            return Bet.MATCH;
        } else {
            return Bet.FOLD;
        }
    }

    private static boolean firstHandContainsHighCard(Card[] hand) {
        return hand[0].getRankAsNumber() >= 10 || hand[1].getRankAsNumber() >= 10;
    }

    private static boolean isPairFirstHand(Card[] hand) {
        return hand[0].getRankAsNumber() == hand[1].getRankAsNumber();
    }

    private static boolean isSameSuitFirstHand(Card[] hand) {
        return hand[0].getSuit().equals(hand[1].getSuit());
    }

    private static boolean areConnectingCards(Card[] hand) {
        return Math.abs(hand[0].getRankAsNumber() - hand[1].getRankAsNumber()) == 1;
    }

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

    public static boolean isGoodHand(Card[] hand) {
        boolean isPair = hand[0].getRankAsNumber() == hand[1].getRankAsNumber();
        if (isPair && hand[0].getRankAsNumber() > 8 && hand[1].getRankAsNumber() > 8) {
            return true;
        }
        return hand[0].getRankAsNumber() > 9 && hand[1].getRankAsNumber() > 9;
    }

    private static boolean isOneThirdHand(Card[] hand) {
        boolean containsHighCard = hand[0].getRankAsNumber() > 9 || hand[1].getRankAsNumber() > 9;
        boolean sameSuit = hand[0].getSuit().equals(hand[1].getSuit());
        boolean firstCardIs78Or9 = hand[0].getRankAsNumber() == 7 || hand[0].getRankAsNumber() == 8 || hand[0].getRankAsNumber() == 9;
        boolean secondCardIs78Or9 = hand[1].getRankAsNumber() == 7 || hand[1].getRankAsNumber() == 8 || hand[1].getRankAsNumber() == 9;
        if (containsHighCard && sameSuit && (firstCardIs78Or9 || secondCardIs78Or9)) {
            return true;
        }

        return isSameSuitFirstHand(hand) && firstHandContainsHighCard(hand);
    }

    public static PokerHandRanking evaluateAllCombinations(Card[] cards) {
        List<Card[]> combinations = generateCombinations(cards);
        PokerHandRanking bestRanking = PokerHandRanking.HIGH_CARD;
        for (Card[] combination : combinations) {
            PokerHandRanking ranking = evaluateHand(combination);
            if (ranking.ordinal() > bestRanking.ordinal()) {
                bestRanking = ranking;
            }
        }
        return bestRanking;
    }
    public static List<Card[]> generateCombinations(Card[] cards) {
        List<Card[]> combinations = new ArrayList<>();
        generateCombinations(cards, 5, 0, new Card[5], combinations);
        return combinations;
    }

    private static void generateCombinations(Card[] cards, int len, int startPosition, Card[] result, List<Card[]> combinations) {
        if (len == 0) {
            combinations.add(result.clone());
            return;
        }
        for (int i = startPosition; i <= cards.length - len; i++) {
            result[result.length - len] = cards[i];
            generateCombinations(cards, len - 1, i + 1, result, combinations);
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