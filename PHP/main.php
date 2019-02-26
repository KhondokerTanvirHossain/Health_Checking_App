
<head>
    <title>My Page</title>
    <!-- <link rel="stylesheet" type="text/css" href="main.css"> -->
</head>
 <script src="main.js"></script> 
 <form action="insert.php" method="post" style="border:1px solid #ccc">
  <div class="container">
    <h1>Sign Up</h1>
    <p>Please fill in this form to create an account.</p>
    <hr>
    <label for="bmp"><b>bmp</b></label>
    <input type="text" placeholder="Enter bmp" name="bmp" >

    <label for="so2"><b>so2</b></label>
    <input type="text" placeholder="Enter so2" name="so2" >

    <label for="tem"><b>tem</b></label>
    <input type="text" placeholder="Enter tem" name="tem">


    <label>
      <input type="checkbox" checked="checked" name="remember" style="margin-bottom:15px"> Remember me
    </label>

    <p>By creating an account you agree to our <a href="#" style="color:dodgerblue">Terms & Privacy</a>.</p>






    <div class="clearfix">
      <button type="button" class="cancelbtn">Cancel</button>
      <button type="submit" class="signupbtn">Sign Up</button>
    </div>

    <nav class="main-nav">
  <ul>
    <li><a class="signin" href="#0">Sign in</a></li>
    <li><a class="signup" href="#0">Sign up</a></li>
  </ul>
</nav>
