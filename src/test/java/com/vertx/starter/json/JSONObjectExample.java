package com.vertx.starter.json;


import com.vertx.starter.model.Person;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class JSONObjectExample {

    private final static Logger log = LoggerFactory.getLogger(JSONObjectExample.class);

    @Test
    void canJSONObjectMapped(){
        final var jsonObject = new JsonObject();
        jsonObject.put("id",1);
        jsonObject.put("name","Nathalie");
        jsonObject.put("enabled", true);

        final String jsonEncoded=jsonObject.encode();
        Assert.assertEquals("{\"id\":1,\"name\":\"Nathalie\",\"enabled\":true}" +
                "",jsonEncoded);

        final JsonObject jsonDecoded= new JsonObject(jsonEncoded);
        Assert.assertEquals(jsonObject,jsonDecoded);
    }

    @Test
    void canJSONObjectCreatedByMap(){
        final Map<String,Object> map = new HashMap<>();
        map.put("id",1);
        map.put("name","Nathalie");
        map.put("enabled", true);

        var objectFromMap = new JsonObject(map);
        Assert.assertEquals(map,objectFromMap.getMap());
        Assert.assertEquals(1,map.get("id"));
        Assert.assertEquals("Nathalie",map.get("name"));
        Assert.assertEquals(true,map.get("enabled"));
    }

    @Test
    void canJSONArrayMapped() {
        final JsonArray jsonArray= new JsonArray();
        jsonArray.add(new JsonObject().put("id",1));
        jsonArray.add(new JsonObject().put("id",2));
        jsonArray.add(new JsonObject().put("id",3));

        final var arrayEncoded= jsonArray.encode();
        Assert.assertEquals("[{\"id\":1},{\"id\":2},{\"id\":3}]",arrayEncoded);
    }

    @Test
    void canMapJavaObjects(){
        final Person person = new Person(1, "Nathalie", true);
        final JsonObject jsonPerson = JsonObject.mapFrom(person);
        Assert.assertEquals(person.getId(), jsonPerson.getInteger("id"));
        Assert.assertEquals(person.getName(), jsonPerson.getString("name"));
        Assert.assertEquals(person.getEnabled(), jsonPerson.getBoolean("enabled"));

        final Person person2 = jsonPerson.mapTo(Person.class);
        Assert.assertEquals(person.getId(), person2.getId());
        Assert.assertEquals(person.getName(), person2.getName());
        Assert.assertEquals(person.getEnabled(), person2.getEnabled());
    }

}
