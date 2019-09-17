package com.wisdomgarden.geolocation.baidu;

import com.baidu.location.BDLocation;
import com.wisdomgarden.geolocation.w3.Coordinates;
import com.wisdomgarden.geolocation.w3.Position;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageBuilder {

    BDLocation location;

    MessageBuilder(BDLocation location) {
        this.location = location;
    }

    public JSONArray build() {
        Position result = new Position()
                .setTimestamp(System.currentTimeMillis())
                .setCoords(new Coordinates()
                        .setLatitude(location.getLatitude())
                        .setLongitude(location.getLongitude())
                        .setAccuracy(location.getRadius())
                        .setHeading(location.getDirection())
                        .setSpeed(location.getSpeed())
                        .setAltitude(location.getAltitude())
                );

        JSONObject extra = new JSONObject();
        try {
            extra.put("locationType", String.valueOf(location.getLocType()));
            extra.put("locationTypeDescription", location.getLocTypeDescription());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray message = new JSONArray();

        message.put(result.toJSON());
        message.put(extra);

        return message;
    }
}
