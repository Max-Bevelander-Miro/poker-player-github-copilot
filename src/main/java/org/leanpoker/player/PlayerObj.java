package org.leanpoker.player;

public class PlayerObj {
    private int id;
    private String name;
    private String status;
    private String version;
    private int stack;
    private int bet;

    public PlayerObj() {}

    public PlayerObj(int id, String name, String status, String version, int stack, int bet) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.version = version;
        this.stack = stack;
        this.bet = bet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getStack() {
        return stack;
    }

    public void setStack(int stack) {
        this.stack = stack;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }
}