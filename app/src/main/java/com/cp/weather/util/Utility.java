package com.cp.weather.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.cp.weather.db.City;
import com.cp.weather.db.ConcernedCity;
import com.cp.weather.db.County;
import com.cp.weather.db.Province;
import com.cp.weather.gson.SearchCity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * JSON工具类
 * 用于解析和处理JSON格式的数据
 */
public class Utility {
    /**
     * 处理服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response){
        if (!TextUtils.isEmpty(response)){      //如果返回的数据非空，则对其进行解析
            try {
                //将字符串对象解析成JSON数组
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {
                    //遍历数组，获取单个JSON对象
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    //从解析的JSON数据中通过key值获取对应的value值
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    //将数据保存进数据库
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 处理服务器返回的市级数据
     */
    public static boolean handleCityResponse(String response,int provinceId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++) {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     *处理服务器返回的县级数据
     */
    public static boolean handleCountyResponse(String response,int cityId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public static SearchCity handleSearchCityResponse(String response){
        try{
            JSONObject jsonObject =new JSONObject(response);
            Gson g=new Gson();
            SearchCity searchCity =g.fromJson(jsonObject.toString(),SearchCity.class);
            return searchCity;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
