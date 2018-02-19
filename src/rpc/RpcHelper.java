package rpc;

import entity.Item;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by septem on 11/18/17.
 */
public class RpcHelper {
    //Writes a JSONObject to http response.
    public static void writeJsonObject(HttpServletResponse response, JSONObject obj) {

        try {
            response.setContentType("application/json");
            response.addHeader("Access-Control-Allow-Origin", "*");
            PrintWriter out = response.getWriter();
            out.print(obj);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Writes a JSONArray to http respone.
    public static void writeJsonArray(HttpServletResponse response, JSONArray array) {

        try {
            response.setContentType("application/json");
            response.addHeader("Access-Control-Allow-Origin", "*");
            PrintWriter out = response.getWriter();
            out.print(array);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject readJsonObject(HttpServletRequest request) {

        StringBuffer sb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            return new JSONObject(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Converts a list of Item objects to JSONArray.
    public static JSONArray getJSONArray(List<Item> items) {
        JSONArray result = new JSONArray();
        try {
            for (Item item : items) {
                result.put(item.toJSONObject());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
