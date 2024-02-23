<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Approval Email</title>
    <style>
        .container {
            border: 1px solid black;
            width: 1000px;
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
           width:auto;

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
        <h3><b></b>Hello ${userModifyBooking.userName}</b></h3>
        <p> <span class="firstLine"> This message is to inform you that your seat has been changed due to management needs in the <span style="color: rgb(40, 233, 220);"> BookMySeat </span> application and has been assigned to the mentioned seat below.</span><br/><br/>
            Below given are your credentials for the application.</p>
            <table class="Details">
                <thead >
                    <tr class="row">
                        <th>User ID</th>
                        <th>User Name</th>
                        <th>User Email</th>
                        <th>Seat Number</th>
                        <th>Floor Name</th>
                        <th>Shift</th>
                        <th>Start Date</th>
                        <th>End Date</th>
                    </tr>
                </thead>
                <tbody>
                    <tr class="row">
                    <td>${userId}</td>
                     <td>${userModifyBooking.userName}</td>
                     <td>${userModifyBooking.userEmail}</td>
                     <td>${userModifyBooking.seatNumber}</td>
                     <td>${userModifyBooking.floorName}</td>
                     <td>${userModifyBooking.shiftName}</td>
                     <td>${userModifyBooking.bookingStartDate}</td>
                     <td>${userModifyBooking.bookingEndDate}</td>
                    </tr>
                </tbody>
            </table>
            <p> Click on the given link to <a class="loginLink" href="http://localhost:3000/bookmyseat/login">login</a> for the <span style="color: rgb(40, 233, 220);"> BookMySeat </span> Application.</p>
            <p> Sorry for the inconvenience. Reach </p>
            <p>Best Regards,<br/>
            <b>Team BookMySeat</b></p>
    </div>
</body>
</html>

