package rpc;

import db.DBConnection;
import db.DBConnectionFactory;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by septem on 2/3/18.
 */
@WebServlet("/login")
public class Login extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        DBConnection conn = DBConnectionFactory.getDBConnection();
        try {
            JSONObject obj = new JSONObject();
            HttpSession session = request.getSession(false);
            if (session == null) {
                response.setStatus(403);
                obj.put("status", "Session Invalid");
            } else {
                String userId = (String) session.getAttribute("user_id");
                String name = conn.getFullName(userId);
                obj.put("status", "OK");
                obj.put("user_id", userId);
                obj.put("name", name);
            }
            RpcHelper.writeJsonObject(response, obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        DBConnection conn = DBConnectionFactory.getDBConnection();
        try {
            JSONObject input = RpcHelper.readJsonObject(request);
            String userId = input.getString("user_id");
            String pwd = input.getString("password");

            JSONObject obj = new JSONObject();

            if (conn.verifyLogin(userId, pwd)) {
                HttpSession session = request.getSession();
                session.setAttribute("user_id", userId);
                // setting session to expire in 10 minutes
                session.setMaxInactiveInterval(10 * 60);
                // Get user name
                String name = conn.getFullName(userId);
                obj.put("status", "OK");
                obj.put("user_id", userId);
                obj.put("name", name);
            } else {
                response.setStatus(401);
            }
            RpcHelper.writeJsonObject(response, obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
