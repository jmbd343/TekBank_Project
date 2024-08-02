package servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

@SuppressWarnings("serial")
public class GeneratePDFServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("customer") == null) {
            response.sendRedirect("customer_login.html");
            return;
        }

        String accountNo = (String) session.getAttribute("customer");

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=transactions.pdf");

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(25, 750);
            contentStream.showText("Last 10 Transactions");
            contentStream.newLine();
            contentStream.newLine();

            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db", "root", "admin123");

                stmt = conn.prepareStatement("SELECT * FROM Transaction WHERE account_no=? ORDER BY transaction_date DESC LIMIT 10");
                stmt.setString(1, accountNo);
                rs = stmt.executeQuery();

                while (rs.next()) {
                	contentStream.showText("Account: " + rs.getString("account_no"));
                    contentStream.newLine();
                    contentStream.showText("Date: " + rs.getString("transaction_date"));
                    contentStream.newLine();
                    contentStream.showText("Type: " + rs.getString("transaction_type"));
                    contentStream.newLine();
                    contentStream.showText("Amount: " + rs.getBigDecimal("amount").toString());
                    contentStream.newLine();
                    contentStream.newLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (rs != null) try { rs.close(); } catch (SQLException e) {}
                if (stmt != null) try { stmt.close(); } catch (SQLException e) {}
                if (conn != null) try { conn.close(); } catch (SQLException e) {}
            }
            
            contentStream.endText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        document.save(response.getOutputStream());
        document.close();
    }
}
