<%@page import="java.util.ArrayList" %>
<%@ page import="com.example.lab103.Beans.Job" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<jsp:useBean type="java.util.ArrayList<com.example.lab103.Beans.Job>" scope="request" id="lista"/>
<jsp:useBean type="java.lang.Integer" scope="session" id="top"/>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel='stylesheet' href='resources/css/bootstrap.min.css'/>
        <title>JSP Page</title>
    </head>
    <body>
        <div class='container'>
            <jsp:include page="../includes/navbar.jsp">
                <jsp:param name="currentPage" value="job"/>
            </jsp:include>

            <%
                String crear = top < 3 ? "" : "hidden";
                String editar = top == 1 || top == 3 ? "" : "hidden";
                String borrar = top < 3 ? "" : "hidden";
            %>

            <div class="row mb-5 mt-4">
                <div class="col-md-7">
                    <h1 class=''>Lista de trabajos en hr</h1>
                </div>
                <div class="col-md-5 col-lg-4 ms-auto my-auto text-md-end">
                    <a href="<%= request.getContextPath()%>/JobServlet?action=formCrear" class="btn btn-primary" <%=crear%>>Crear
                        Trabajo</a>
                </div>
            </div>
            <% if (request.getParameter("msg") != null) {%>
                <div class="alert alert-success" role="alert"><%=request.getParameter("msg")%></div>
            <% } %>

            <% if (request.getParameter("err") != null) {%>
                <div class="alert alert-danger" role="alert"><%=request.getParameter("err")%></div>
            <% } %>

            <table class="table">
                <tr>
                    <th>#</th>
                    <th>Job ID</th>
                    <th>Job Name</th>
                    <th>Min Salary</th>
                    <th>Max Salary</th>
                    <th></th>
                    <th></th>
                </tr>
                <%
                    int i = 1;
                    for (Job job : lista) {
                %>
                <tr>
                    <td><%=i%>
                    </td>
                    <td><%=job.getJobId()%>
                    </td>
                    <td><%=job.getJobTitle()%>
                    </td>
                    <td><%=job.getMinSalary()%>
                    </td>
                    <td><%=job.getMaxSalary()%>
                    </td>
                    <td>
                        <a href="<%=request.getContextPath()%>/JobServlet?action=editar&id=<%=job.getJobId()%>" <%=editar%>>
                            Editar
                        </a>
                    </td>
                    <td>
                        <a href="<%=request.getContextPath()%>/JobServlet?action=borrar&id=<%=job.getJobId()%>" <%=borrar%>>
                            Borrar
                        </a>
                    </td>
                </tr>
                <%
                        i++;
                    }
                %>
            </table>
        </div>
        <jsp:include page="../includes/footer.jsp"/>
    </body>
</html>