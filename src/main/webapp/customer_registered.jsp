<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Customer Registered</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
    
</head>
<body>
<div class="container">
 <h1>Customer Registration Successful</h1>
    <p class="details">Customer has been successfully registered with the following details:</p>
    <p><strong>Account Number:</strong> <%= request.getAttribute("accountNo") %></p>
    <p><strong>Temporary Password:</strong> <%= request.getAttribute("tempPassword") %></p>
    <div class="button">
        <a href="register_customer.html">
            <button type="button">Back to Dashboard</button>
        </a>
    </div>
    </div>
</body>
</html>