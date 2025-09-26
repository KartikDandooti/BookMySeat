<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome to Our Platform</title>
    <style>
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
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .header {
            background-color: #4CAF50;
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
    <h2>Welcome to BookMySeat, ${user.firstName}!</h2>
    <p>We are thrilled to have you join us. Your registration has been successfully processed.</p>
    <p>You can now access our platform using the following credentials:</p>
    <ul>
        <li><strong>Email:</strong> ${user.email}</li>
        <li><strong>Password:</strong> <p>Your password will be in the following format:</p>
    <p>First 3 letters of your first name + First 3 letters of your last name + "@" + Last 4 numbers of your phone number</p>
    <p>Please make a note of this format for future reference.</p> </li>
    </ul>
    <p>If you have any questions or need assistance, feel free to contact admin team.</p>
    <p>Best regards,<br>The BookMySeat Team</p></div>
</body>
</html>
