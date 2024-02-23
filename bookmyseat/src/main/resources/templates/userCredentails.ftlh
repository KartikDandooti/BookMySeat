<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Email Content</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      color: #333;
      background-color: #f4f4f4;
      margin: 0;
      padding: 20px;
    }

    div {
      margin-bottom: 15px;
    }

    h4 {
      color: #007BFF;
    }

    table {
      border-collapse: collapse;
      width: 100%;
    }

    th, td {
      border: 1px solid #ddd;
      padding: 8px;
      text-align: left;
    }

    b {
      color: #555;
    }
    
    .remainder{
        color: red;
    }
  </style>
</head>
<body>
  <div>
    <div>
      <div>Hello,
        <h4 class="inline-heading">${user.firstName}</h4>
      </div>
      <div>Your Profile has been created Succesfully ..</div>
    </div>

    <br/>

    <div>
      Your temporary password for login is <b>first 4 digits of your first name</b> (first letter should be capital) and <b>last 4 digits of your phone number</b> along with the special character(@) in between them.
    </div>
    <br/>

    <div>
      <b>Example:</b>
      <br/>
      <br/>
      <b>First Name:</b> username
      <br/>
      <b>Phone No.</b> 1234567890
      <br/>
      <b>Password</b> User@1234
      <br/>
      <br/>
    </div>

    <br/>

    <div class="remainder">
      Please reset your password as soon as you login...
    </div>
    <br/>

    <h4>Best regards,</h4>
    <h4>BookMySeat</h4>
  </div>
  <br/>
  <div><a href="http://localhost:3000/login" class="login-link">Login Here</a></div> 
</body>
</html>
