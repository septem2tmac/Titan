package rpc;

import algorithm.GeoRecommendation;
import entity.Item;
import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by septem on 11/18/17.
 */
@WebServlet(urlPatterns = "/recommendation")
public class RecommendationItem extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userId = request.getParameter("user_id");
        double lat = Double.parseDouble(request.getParameter("lat"));
        double lon = Double.parseDouble(request.getParameter("lon"));
        GeoRecommendation recommendation = new GeoRecommendation();
        List<Item> items = recommendation.recommendItems(userId, lat, lon);

        JSONArray result = new JSONArray();
        try {
            for (Item item : items) {
                result.put(item.toJSONObject());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        RpcHelper.writeJsonArray(response, result);
    }
}