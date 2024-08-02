<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, java.util.*, javax.sql.*, javax.naming.*" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>View Transactions</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
    
</head>
<body>
    <div class="container">
        <h1>Last 10 Transactions</h1>
        <table>
            <tr>
                <th>Date</th>
                <th>Type</th>
                <th>Amount</th>
            </tr>
            <% 
                HttpSession session1 = request.getSession(false);
                if (session1 == null || session1.getAttribute("accountNo") == null) {
                    response.sendRedirect("customer_login.html");
                } else {
                    String accountNo = (String) session1.getAttribute("accountNo");
                    Connection conn = null;
                    PreparedStatement stmt = null;
                    ResultSet rs = null;

                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db", "root", "password");

                        stmt = conn.prepareStatement("SELECT * FROM Transaction WHERE account_no=? ORDER BY transaction_date DESC LIMIT 10");
                        stmt.setString(1, accountNo);
                        rs = stmt.executeQuery();

                        while (rs.next()) {
                            String date = rs.getString("transaction_date");
                            String type = rs.getString("transaction_type");
                            String amount = rs.getString("amount");
                            %>
                            <tr>
                                <td><%= date %></td>
                                <td><%= type %></td>
                                <td><%= amount %></td>
                            </tr>
                            <%
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (rs != null) try { rs.close(); } catch (SQLException e) {}
                        if (stmt != null) try { stmt.close(); } catch (SQLException e) {}
                        if (conn != null) try { conn.close(); } catch (SQLException e) {}
                    }
                }
            %>
        </table>
        <div class="button">
            <a href="customerDashboard.jsp">
                <button type="button">Back to Dashboard</button>
            </a>
        </div>
    </div>
</body>
</html>
