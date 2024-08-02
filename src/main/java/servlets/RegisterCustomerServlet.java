package servlets;
import java.math.BigDecimal;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.Random;

@SuppressWarnings("serial")
public class RegisterCustomerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect("admin_login.html");
            return;
        }

        String fullName = request.getParameter("full_name");
        String address = request.getParameter("address");
        String mobileNo = request.getParameter("mobile_no");
        String emailId = request.getParameter("email_id");
        String accountType = request.getParameter("account_type");
        String dob = request.getParameter("dob");
        String idProof = request.getParameter("id_proof");

        Random rand = new Random();
        String accountNo = "ACC" + rand.nextInt(100000);
        String tempPassword = "temp" + rand.nextInt(10000);
        BigDecimal initialBalance = new BigDecimal("1000.00");

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db", "root", "admin123");

            stmt = conn.prepareStatement("INSERT INTO Customer (account_no, full_name, address, mobile_no, email_id, account_type, initial_balance, dob, id_proof, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, accountNo);
            stmt.setString(2, fullName);
            stmt.setString(3, address);
            stmt.setString(4, mobileNo);
            stmt.setString(5, emailId);
            stmt.setString(6, accountType);
            stmt.setBigDecimal(7, initialBalance);
            stmt.setString(8, dob);
            stmt.setString(9, idProof);
            stmt.setString(10, tempPassword);
            stmt.executeUpdate();

            request.setAttribute("accountNo", accountNo);
            request.setAttribute("tempPassword", tempPassword);
            RequestDispatcher rd = request.getRequestDispatcher("customer_registered.jsp");
            rd.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
}
