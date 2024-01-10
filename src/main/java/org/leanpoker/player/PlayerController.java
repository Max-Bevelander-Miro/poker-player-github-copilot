package org.leanpoker.player;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import java.util.Map;

@Controller()
public class PlayerController {

    private ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    @Get(produces = MediaType.TEXT_PLAIN)
    public String doGet() {
        return "Java player is running";
    }

    @Post(produces = MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String doPost(@Body Map<String, String> body) throws JsonProcessingException {
        ObjectMapper mapper = getMapper();
        String action = body.get("action");
        String gameState = body.get("game_state");
        if ("bet_request".equals(action)) {
            return String.valueOf(getBetRequest(gameState, mapper));
        }
        if ("showdown".equals(action)) {
            Player.showdown(mapper.readTree(gameState));
        }
        if ("version".equals(action)) {
            return Player.VERSION;
        }
        return "";
    }

    private int getBetRequest(String gameState, ObjectMapper mapper) throws JsonProcessingException {
        try {
            GameState gameStateObj = mapper.readValue(gameState, GameState.class);
            return Player.betRequest(gameStateObj);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return Player.betRequest(mapper.readTree(gameState));
        }
    }
}
