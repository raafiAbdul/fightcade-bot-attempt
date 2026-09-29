package com.hftf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FightcadeHFTFClient {

    private static final String TS_SERVER_URL = "http://localhost:8080/api/last-match";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    // Parsed properties
    private String playerUsername;
    private int playerScore;
    private String opponentUsername;
    private int opponentScore;

    public FightcadeHFTFClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public boolean fetchLastHFTFMatch(String username) {
        try {
            // Target HFTF (jojoba) on local TypeScript bridge server (port 8080)
            String targetUri = String.format("%s?username=%s&gameid=jojoba", TS_SERVER_URL, username);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(targetUri))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.err.println("TypeScript Bridge returned status " + response.statusCode() + ": " + response.body());
                return false;
            }

            MatchData match = objectMapper.readValue(response.body(), MatchData.class);

            this.playerUsername = match.playerUsername;
            this.playerScore = match.playerScore;
            this.opponentUsername = match.opponentUsername;
            this.opponentScore = match.opponentScore;

            return true;
        } catch (Exception e) {
            System.err.println("Failed to query TypeScript server: " + e.getMessage());
            return false;
        }
    }

    // --- Getter Methods ---

    public String getPlayerUsername() {
        return playerUsername;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public String getOpponentUsername() {
        return opponentUsername;
    }

    public int getOpponentScore() {
        return opponentScore;
    }

    // --- Jackson Data Model ---

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class MatchData {
        public String gameid;
        public String playerUsername;
        public int playerScore;
        public String opponentUsername;
        public int opponentScore;
    }

    // --- Main Method ---

    public static void main(String[] args) {
        FightcadeHFTFClient client = new FightcadeHFTFClient();
        String username = "daigo";

        if (client.fetchLastHFTFMatch(username)) {
            System.out.println("--- Match Retrieved via TypeScript Bridge ---");
            System.out.println("Player Username: " + client.getPlayerUsername());
            System.out.println("Player Score: " + client.getPlayerScore());
            System.out.println("Opponent Username: " + client.getOpponentUsername());
            System.out.println("Opponent Score: " + client.getOpponentScore());
        }
    }
}