<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Password Reset OTP</title>
    <style>
        body {
            font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
            background-color: #f4f4f4;
            color: #333;
            margin: 0;
            padding: 20px;
        }

        .msgContainer {
            max-width: 600px;
            margin: 0 auto;
        }

        #content {
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        p {
            margin-bottom: 15px;
        }
    </style>
</head>

<body>
    <div class="msgContainer">
        <div id="content">
            <p><b>Hello ${user.firstName},</b></p>
            <p>Here is your One Time Password for resetting your password : <b>${otpValue}</b></p>
            <p>This code will expire in 2 minutes. If you did not request to reset your password, it is safe to disregard this message.</p>
            <p><b>Thank you,</b></p>
            <p>BOOKMYSEAT</p>
        </div>
    </div>
</body>

</html>
