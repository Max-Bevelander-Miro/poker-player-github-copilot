package org.leanpoker.player;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import org.apache.commons.lang3.exception.ExceptionUtils;

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
        try {
            ObjectMapper mapper = getMapper();
            String action = body.get("action");
            String gameState = body.get("game_state");
            if ("bet_request".equals(action)) {
                GameState gameStateObj = mapper.readValue(gameState, GameState.class);
                return String.valueOf(Player.betRequest(gameStateObj));
            }
            if ("showdown".equals(action)) {
                Player.showdown(mapper.readTree(gameState));
            }
            if ("version".equals(action)) {
                return Player.VERSION;
            }
            return "";
        } catch (Exception ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
            throw ex;
        }
    }
}
