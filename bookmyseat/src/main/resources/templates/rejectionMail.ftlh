<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Rejection Email</title>
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
           .registerLink{
            color: rgb(95, 131, 249);
           }
    </style>
</head>
<body>
    <div class="container">
        <h3><b></b>Hello ${user.firstName} ${user.lastName},</b></h3>
        <p> <span class="firstLine"> We are regret to inform you that your registration to the <span style="color: rgb(40, 233, 220);"> BookMySeat </span> application has been Rejected.</p>
            <table class="Details">
                <thead >
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
						<td>${user.approvalStatus}</td>
                    </tr>
                </tbody>
            </table>
            <p>To register again please follow link given <a class="registerLink" href="http://localhost:3000/bookmyseat/login">register</a> for the <span style="color: rgb(40, 233, 220);"> BookMySeat </span> Application.</p>
            <p>Best Regards,<br/>
            <b>Team BookMySeat</b></p>
    </div>
</body>
</html>


