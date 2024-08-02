<%@ page import="java.sql.*, javax.sql.*" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Customer Dashboard</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
    <style>
        .logout-btn {
            position: absolute;
            top: 20px;
            right: 20px;
            background-color: #F4CE14; /* Light olive */
            color: black;
            border: none;
            padding: 10px 20px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 14px;
            cursor: pointer;
            border-radius: 5px;
        }

        .logout-btn:hover {
            background-color: #66635B; /* Dark olive */
        }
    </style>
    <script>
        function validateAmount(input) {
            if (input.value < 0) {
                alert("Transaction Amount Should not be Negative");
                input.value = '';
            }
        }
    </script>
</head>
<body>
    <div class="container">
        <% 
            String accountNo = (String) session.getAttribute("customer");
            if (accountNo == null) {
                response.sendRedirect("customer_login.html");
                return;
            }

            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db", "root", "admin123");

                stmt = conn.prepareStatement("SELECT * FROM Customer WHERE account_no = ?");
                stmt.setString(1, accountNo);
                rs = stmt.executeQuery();

                if (rs.next()) {
        %>
                <h2>Tek Bank</h2>
                <h2>Customer Dashboard</h2>
                <p>Account No: <%= rs.getString("account_no") %></p>
                <p>Full Name: <%= rs.getString("full_name") %></p>
                <p>Address: <%= rs.getString("address") %></p>
                <p>Mobile No: <%= rs.getString("mobile_no") %></p>
                <p>Email ID: <%= rs.getString("email_id") %></p>
                <p>Account Type: <%= rs.getString("account_type") %></p>
                <p>Initial Balance: <%= rs.getBigDecimal("initial_balance") %></p>
                <form action="deposit" method="post">
                    <label for="amount">Deposit Amount:</label>
                    <input type="number" id="amount" name="amount" step="0.01" required oninput="validateAmount(this)"><br>
                    <input type="submit" value="Deposit">
                </form>
                <form action="withdraw" method="post">
                    <label for="amount">Withdraw Amount:</label>
                    <input type="number" id="amount" name="amount" step="0.01" required oninput="validateAmount(this)"><br>
                    <input type="submit" value="Withdraw">
                </form><br>
                <a href="generatePDF"><b>Download Last 10 Transactions</b></a>
                <a class="logout-btn" href="customer_login.html"><b>Logout</b></a>
        <%
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (rs != null) try { rs.close(); } catch (SQLException e) {}
                if (stmt != null) try { stmt.close(); } catch (SQLException e) {}
                if (conn != null) try { conn.close(); } catch (SQLException e) {}
            }
        %>
    </div>
</body>
</html>
