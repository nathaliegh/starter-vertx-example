package com.vertx.starter.model;

import java.util.Objects;

public class Pong {
    private int id;

    public Pong() {
    }

    public Pong(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Pong{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pong pong = (Pong) o;
        return id == pong.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
