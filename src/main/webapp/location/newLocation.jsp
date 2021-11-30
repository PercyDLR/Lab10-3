<%@ page import="com.example.lab103.Beans.Country" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<jsp:useBean id="listaPaises" scope="request" type="java.util.ArrayList<com.example.lab103.Beans.Country>"/>
<!DOCTYPE html>
<html>
    <head>
        <title>Crear una ubicación</title>
        <jsp:include page="../includes/headCss.jsp"/>
    </head>
    <body>
        <div class='container'>
            <jsp:include page="../includes/navbar.jsp">
                <jsp:param name="currentPage" value="loc"/>
            </jsp:include>
            <div class="row justify-content-center mb-4">
                <div class="col-md-6">
                    <h1 class='mb-3'>Crear una ubicación</h1>
                    <form method="POST" action="<%=request.getContextPath()%>/LocationServlet?action=crear">
                        <div class="mb-3">
                            <label class="form-label" for="id">Location ID</label>
                            <input type="text" class="form-control" name="id" id="id" required="required"/>
                        </div>
                        <div class="mb-3">
                            <label class="form-label" for="streetAddress">Street Address</label>
                            <input type="text" class="form-control" name="streetAddress" id="streetAddress" required="required"/>
                        </div>
                        <div class="mb-3">
                            <label class="form-label" for="postalCode">Postal Code</label>
                            <input type="text" class="form-control" name="postalCode" id="postalCode" required="required"/>
                        </div>
                        <div class="mb-3">
                            <label class="form-label" for="city">City</label>
                            <input type="text" class="form-control" name="city" id="city" required="required"/>
                        </div>
                        <div class="mb-3">
                            <label class="form-label" for="stateProvince">State Province</label>
                            <input type="text" class="form-control" name="stateProvince" id="stateProvince" required="required"/>
                        </div>
                        <div class="mb-3">
                            <label class="form-label" for="countryId">Country</label>
                            <select name="countryId" id="countryId" class="form-control" required>
                                <% for (Country country : listaPaises) {%>
                                <option value="<%=country.getCountryId()%>"><%=country.getCountryName()%>
                                </option>
                                <% }%>
                            </select>
                        </div>
                        <a href="<%= request.getContextPath()%>/LocationServlet" class="btn btn-danger">Cancelar</a>
                        <%
                            if(null != request.getParameter("buttonPressed")){
                                if(null == request.getParameter("id") &&
                                        null == request.getParameter("streetAddress") &&
                                        null == request.getParameter("postalCode") &&
                                        null == request.getParameter("city") &&
                                        null == request.getParameter("stateProvince")
                                )
                                {
                                    request.setAttribute("errorMessage", "Error al crear o editar el location");
                                }
                            }
                        %>
                        <button type="submit" name="buttonPressed" class="btn btn-primary">Submit</button>
                    </form>
                </div>
            </div>
        </div>
        <jsp:include page="../includes/footer.jsp"/>
    </body>
</html>
