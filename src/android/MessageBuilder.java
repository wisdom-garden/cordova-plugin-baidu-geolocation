package com.eteng.geolocation.baidu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.eteng.geolocation.w3.Coordinates;
import com.eteng.geolocation.w3.Position;

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

    JSONObject json = result.toJSON();
    try {
      if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
          json.put("speed", location.getSpeed());
          json.put("satellite", location.getSatelliteNumber());
          json.put("height", location.getAltitude());
          json.put("direction", location.getDirection());
          json.put("addr", location.getAddrStr());
          json.put("describe", "gps定位成功");
      } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
          json.put("addr", location.getAddrStr());
          json.put("operationers", location.getOperators());
          json.put("describe", "网络定位成功");
      } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
          json.put("describe", "离线定位成功，离线定位结果也是有效的");
      } else if (location.getLocType() == BDLocation.TypeServerError) {
          json.put("describe", "服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
      } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
          json.put("describe", "网络不同导致定位失败，请检查网络是否通畅");
      } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
          json.put("describe", "无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    JSONArray message = new JSONArray();
    message.put(json);

    return message;
  }

}
