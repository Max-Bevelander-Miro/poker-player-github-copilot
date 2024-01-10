package org.leanpoker.player;

import com.fasterxml.jackson.databind.JsonNode;

public class Player {

    static final String VERSION = "Default Java folding player";

    public static int betRequest(JsonNode request) {
        return 0;
    }

    public static int betRequest(GameState gameState) {

        PlayerObj player = gameState.getPlayers().stream().filter(p -> p.getHole_cards().length > 0).findFirst().orElse(null);
        if(player == null){
            return 0;
        }
        //is opening hand
        if (gameState.getCommunity_cards().isEmpty()){

        } else{

        }

        return 0;
    }

    public static void showdown(JsonNode game) {
    }
}
