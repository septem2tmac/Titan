package rpc;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by septem on 11/18/17.
 */
@WebServlet(urlPatterns = "/search")
public class SearchItem extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // allow access only if session exists
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(403);
            return;
        }
        String userId = session.getAttribute("user_id").toString();

        double lat = Double.parseDouble(request.getParameter("lat"));
        double lon = Double.parseDouble(request.getParameter("lon"));
        // Term can be empty or null.
        String term = request.getParameter("term");
        //String userId = request.getParameter("user_id");

        DBConnection connection = DBConnectionFactory.getDBConnection();
        List<Item> items = connection.searchItems(lat, lon, term);

        List<JSONObject> list = new ArrayList<>();

        Set<String> favorite = connection.getFavoriteItemIds(userId);
        try {
            for (Item item : items) {
                // Add a thin version of item object
                JSONObject obj = item.toJSONObject();

                // Check if this is a favorite one.
                // This field is required by frontend to correctly display favorite items.
                obj.put("favorite", favorite.contains(item.getItemId()));
                list.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONArray array = new JSONArray(list);
        RpcHelper.writeJsonArray(response, array);
    }
}
