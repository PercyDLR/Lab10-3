<%@page import="com.example.lab103.Beans.Country" %>
<%@ page import="com.example.lab103.Beans.Region" %>
<%@ page import="java.math.BigDecimal" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<jsp:useBean id="country" scope="request" type="com.example.lab103.Beans.Country"/>
<jsp:useBean id="listaRegiones" scope="request" type="java.util.ArrayList<com.example.lab103.Beans.Region>"/>
<!DOCTYPE html>
<html>
    <head>
        <title>Editar un Pais</title>
        <jsp:include page="../includes/headCss.jsp"/>
    </head>
    <body>
        <div class='container'>
            <jsp:include page="../includes/navbar.jsp">
                <jsp:param name="currentPage" value="cou"/>
            </jsp:include>
            <div class="row justify-content-center mb-4">
                <div class="col-md-6">
                    <h1 class='mb-3'>Editar un Pais</h1>
                    <jsp:include page="../includes/infoMsgs.jsp"/>
                    <form method="POST" action="<%=request.getContextPath()%>/CountryServlet?action=crear">
                        <input type="hidden" class="form-control" name="id" value="<%=country.getCountryId()%>">
                        <div class="mb-3">
                            <label class="form-label" for="countryName">Country name</label>
                            <input type="text" class="form-control" name="countryName" id="countryName"
                                   value="<%=country.getCountryName() == null ? "" : country.getCountryName()%>" />
                        </div>
                        <div class="mb-3">
                            <label class="form-label" for="regionId">Region</label>
                            <select class="form-select" name="regionId" id="regionId">
                                <option value="" <%=country.getRegionId() == null ? "selected" : ""%>>-- Seleccione una region --</option>
                                <%for (Region r : listaRegiones) { %>
                                <option value="<%=r.getId()%>" <%=country.getRegionId() != null ? (country.getRegionId().compareTo(new BigDecimal(r.getId())) == 0 ? "selected" : "") : ""%>  ><%=r.getNombre()%></option>
                                <%}%>
                            </select>
                        </div>
                        <a href="<%= request.getContextPath()%>/CountryServlet" class="btn btn-danger">Cancelar</a>
                        <button type="submit" class="btn btn-primary">Submit</button>
                    </form>
                </div>
            </div>
        </div>
        <jsp:include page="../includes/footer.jsp"/>
    </body>
</html>
