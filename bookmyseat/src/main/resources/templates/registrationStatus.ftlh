<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registration</title>
    <style>
        .container {
            border: 1px solid black;
            width: 600px;
            height: 500px;
            background-color: black;
            color: white;
            margin:10px;
        }
        .firstLine {
            margin-left: 50px;
        }
        table, th, td {
          border: 1px solid rgb(236, 226, 226);
           border-collapse: collapse;
           padding: 5px;

         }
        table {
            margin: 20px;
            margin-left: 40px;
            text-align: center;     
           }
           .loginLink{
            color: rgb(95, 131, 249);
           }
    </style>
</head>
<body>
    <div class="container">
        <h3><b>Hello Admin,</h3>
        <p> <span class="firstLine"> There is a new registration request for the <span style="color: rgb(40, 233, 220);"> BookMySeat </span> application. Please verify the request.</span><br/><br/>
            Below are the user details.</p>
            <table class="Details">
                <thead>
                    <tr class="row">
                        <th>User ID</th>
                        <th>User Name</th>
                        <th>User Email</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <tr class="row">
                        <td>${userId}</td>
						<td>${user.firstName} ${user.lastName}</td>
						<td>${user.emailId}</td>
						<td>PENDING</td>
                    </tr>
                </tbody>
            </table>
            <p> Click on the given <a class="loginLink" href="http://localhost:3000/bookmyseat/login">link</a> to view the registration request.</p>
            <p>Best Regards,<br/>
            <b>Team BookMySeat</b></p>
    </div>
</body>
</html>

