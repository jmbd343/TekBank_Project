package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class CustomerDashboardServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("customer") == null) {
            response.sendRedirect("customer_login.html");
            return;
        }

        String amountStr = request.getParameter("amount");
        double amount = Double.parseDouble(amountStr);

        if (amount < 0) {
            request.setAttribute("errorMessage", "Transaction Amount Should not be Negative or NULL");
            request.getRequestDispatcher("customer_dashboard.jsp").forward(request, response);
            return;
        }

        // Perform deposit operation

        response.sendRedirect("customer_dashboard.jsp");
    }
}
