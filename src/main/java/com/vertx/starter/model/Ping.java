package com.vertx.starter.model;

import java.util.Objects;

public class Ping {

    private String message;
    private boolean enabled;

    public Ping() {
    }

    public Ping(String message, boolean enabled) {
        this.message = message;
        this.enabled = enabled;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "Ping{" +
                "message='" + message + '\'' +
                ", enabled=" + enabled +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ping ping = (Ping) o;
        return enabled == ping.enabled && Objects.equals(message, ping.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, enabled);
    }
}
