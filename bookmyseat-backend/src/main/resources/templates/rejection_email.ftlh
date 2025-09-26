<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Rejection Email</title>
    <style>
        /* Styles for the email */
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .header {
            background-color: #FF5733; /* Change header color for rejection */
            color: #fff;
            padding: 10px 20px;
            border-top-left-radius: 8px;
            border-top-right-radius: 8px;
        }
        h2 {
            color: #333;
            margin-bottom: 10px;
        }
        p {
            color: #666;
            margin-bottom: 10px;
        }
        .footer {
            margin-top: 20px;
            border-top: 1px solid #ddd;
            padding-top: 10px;
            font-size: 14px;
            color: #666;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1 style="margin: 0;">BookMySeat</h1>
        </div>
        <div>
            <h2>Dear ${user.firstName},</h2>
            <hr>
            <h3 style="color: #FF5733;">We Regret to Inform You...</h3>
            <p>Your registration has been rejected by Admin. We apologize for any inconvenience caused.</p>
            <p>If you have any questions or concerns, please feel free to contact Admin Department.</p>
        </div>
        <div class="footer">
            <p>Best regards,<br/>The BookMySeat Team</p>
        </div>
    </div>
</body>
</html>