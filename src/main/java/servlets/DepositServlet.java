package servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.math.BigDecimal;

@SuppressWarnings("serial")
public class DepositServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("customer") == null) {
            response.sendRedirect("customer_login.html");
            return;
        }

        String accountNo = (String) session.getAttribute("customer");
        BigDecimal amount = new BigDecimal(request.getParameter("amount"));

        Connection conn = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db", "root", "admin123");

            stmt1 = conn.prepareStatement("UPDATE Customer SET initial_balance = initial_balance + ? WHERE account_no=?");
            stmt1.setBigDecimal(1, amount);
            stmt1.setString(2, accountNo);
            stmt1.executeUpdate();

            stmt2 = conn.prepareStatement("INSERT INTO Transaction (account_no, transaction_type, amount) VALUES (?, 'Deposit', ?)");
            stmt2.setString(1, accountNo);
            stmt2.setBigDecimal(2, amount);
            stmt2.executeUpdate();

            response.sendRedirect("customer_dashboard.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt1 != null) try { stmt1.close(); } catch (SQLException e) {}
            if (stmt2 != null) try { stmt2.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
}
