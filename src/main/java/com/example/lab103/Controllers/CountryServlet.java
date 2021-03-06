package com.example.lab103.Controllers;

import com.example.lab103.Beans.Country;
import com.example.lab103.Beans.Employee;
import com.example.lab103.Daos.CountryDao;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

@WebServlet(name = "CountryServlet", urlPatterns = {"/CountryServlet"})
public class CountryServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        Employee em = (Employee) session.getAttribute("employeeSession");
        if (em == null) {
            response.sendRedirect(request.getContextPath());
        } else {

            RequestDispatcher view;
            String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");

            int top = (Integer) session.getAttribute("top");

            if(top == 2 && (action.equals("editar"))){
                action = "lista";

            } else if(top == 3 && (action.equals("borrar") || action.equals("formCrear"))){
                action = "lista";

            } else if(top == 4 && (!action.equals("lista"))){
                action = "lista";
            }

            CountryDao countryDao = new CountryDao();
            Country country;
            String countryId;

            switch (action) {
                case "formCrear":
                    request.setAttribute("listaRegiones", countryDao.listarRegiones());
                    view = request.getRequestDispatcher("country/newCountry.jsp");
                    view.forward(request, response);
                    break;

                case "crear":
                    countryId = request.getParameter("id");

                    String countryName = null;
                    if (!request.getParameter("countryName").equals("")) {
                        countryName = request.getParameter("countryName");
                    }
                    //System.out.println(countryName);
                    BigDecimal regionId = null;
                    if (!request.getParameter("regionId").equals("")) {
                        regionId = new BigDecimal(request.getParameter("regionId"));
                    }

                    country = countryDao.obtener(countryId);

                    if (country == null && top < 3) {
                        countryDao.crear(countryId, countryName, regionId);

                    } else if (country != null && (top ==1 || top == 3)) {
                        countryDao.actualizar(countryId, countryName, regionId);
                    }

                    response.sendRedirect(request.getContextPath() + "/CountryServlet");
                    break;

                case "lista":
                    ArrayList<Country> lista = countryDao.listar();

                    request.setAttribute("lista", lista);

                    view = request.getRequestDispatcher("country/listaCountry.jsp");
                    view.forward(request, response);
                    break;

                case "editar":
                    countryId = request.getParameter("id");
                    country = countryDao.obtener(countryId);
                    if (country == null) {
                        response.sendRedirect(request.getContextPath() + "/CountryServlet");
                    } else {
                        request.setAttribute("listaRegiones", countryDao.listarRegiones());
                        request.setAttribute("country", country);
                        view = request.getRequestDispatcher("country/updateCountry.jsp");
                        view.forward(request, response);
                    }
                    break;

                case "borrar":
                    countryId = request.getParameter("id");
                    if (countryDao.obtener(countryId) != null) {
                        countryDao.borrar(countryId);
                    }
                    response.sendRedirect(request.getContextPath() + "/CountryServlet");
                    break;
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
