package org.leanpoker.player;

import com.fasterxml.jackson.databind.JsonNode;

public class Player {

    static final String VERSION = "Default Java folding player";

    public static int betRequest(JsonNode request) {
        return 5;
    }

    public static void showdown(JsonNode game) {
    }
}
