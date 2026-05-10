package com.signup;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        PrintWriter out = response.getWriter();

        if(username == null || password == null || email == null ||
           username.isEmpty() || password.isEmpty() || email.isEmpty()) {

            out.println("Fields cannot be empty");

        } else {

            out.println("<html>");
out.println("<head>");

out.println("<style>");

out.println("body{");
out.println("font-family:Arial;");
out.println("background:#f2f2f2;");
out.println("display:flex;");
out.println("justify-content:center;");
out.println("align-items:center;");
out.println("height:100vh;");
out.println("}");

out.println(".box{");
out.println("background:white;");
out.println("padding:40px;");
out.println("border-radius:10px;");
out.println("box-shadow:0 0 10px gray;");
out.println("text-align:center;");
out.println("}");

out.println("h1{ color:green; }");

out.println("</style>");

out.println("</head>");

out.println("<body>");

out.println("<div class='box'>");

out.println("<h1>Signup Successful</h1>");

out.println("<p>Welcome " + username + "</p>");

out.println("</div>");

out.println("</body>");
out.println("</html>");

        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }
}