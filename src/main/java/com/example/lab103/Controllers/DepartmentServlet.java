/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.lab103.Controllers;

import com.example.lab103.Beans.Department;
import com.example.lab103.Beans.Employee;
import com.example.lab103.Daos.DepartmentDao;
import com.example.lab103.Daos.EmployeeDao;
import com.example.lab103.Daos.LocationDao;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "DepartmentServlet", urlPatterns = {"/DepartmentServlet"})
public class DepartmentServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        Employee em = (Employee) session.getAttribute("employeeSession");

        if (em == null) {
            response.sendRedirect(request.getContextPath());
        } else {
            String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");

            DepartmentDao departmentDao = new DepartmentDao();
            LocationDao locationDao = new LocationDao();
            EmployeeDao employeeDao = new EmployeeDao();
            RequestDispatcher view;

            int top = (Integer) session.getAttribute("top");

            if(top == 2 && (action.equals("editar"))){
                action = "lista";

            } else if(top == 3 && (action.equals("borrar") || action.equals("formCrear"))){
                action = "lista";

            } else if(top == 4 && (!action.equals("lista"))){
                action = "lista";
            }

            Department department;
            int departmentId = 0;

            switch (action) {
                case "formCrear":
                    request.setAttribute("listaLocations",locationDao.listar());
                    request.setAttribute("listaEmpleados",employeeDao.listarEmpleados());
                    view = request.getRequestDispatcher("department/newDepartment.jsp");
                    view.forward(request, response);
                    break;
                case "crear":
                    boolean depaId_isNumero = false;
                    try{
                        departmentId = Integer.parseInt(request.getParameter("id"));
                        depaId_isNumero = true;
                    }
                    catch(NumberFormatException e){
                        e.printStackTrace();
                    }

                    String departmentName = request.getParameter("departmentName");
                    int managerId = Integer.parseInt(request.getParameter("managerId"));
                    int locationId = Integer.parseInt(request.getParameter("locationId"));

                    department = departmentDao.obtener(departmentId);

                    if (department == null && top < 3) {
                        departmentDao.crear(departmentId, departmentName, managerId, locationId);
                    } else if (department != null && (top ==1 || top == 3)) {
                        departmentDao.actualizar(departmentId, departmentName, managerId, locationId);
                    }

                    response.sendRedirect(request.getContextPath() + "/DepartmentServlet");
                    break;

                case "lista":
                    ArrayList<Department> lista = departmentDao.listaDepartamentos();

                    request.setAttribute("lista", lista);

                    view = request.getRequestDispatcher("department/listaDepartment.jsp");
                    view.forward(request, response);
                    break;

                case "editar":
                    departmentId = Integer.parseInt(request.getParameter("id"));
                    department = departmentDao.obtener(departmentId);
                    if (department == null) {
                        response.sendRedirect(request.getContextPath() + "/DepartmentServlet");
                    } else {
                        request.setAttribute("listaLocations",locationDao.listar());
                        request.setAttribute("listaEmpleados",employeeDao.listarEmpleados());
                        request.setAttribute("department", department);
                        view = request.getRequestDispatcher("department/updateDepartment.jsp");
                        view.forward(request, response);
                    }
                    break;

                case "borrar":
                    departmentId = Integer.parseInt(request.getParameter("id"));
                    if (departmentDao.obtener(departmentId) != null) {
                        departmentDao.borrar(departmentId);
                    }
                    response.sendRedirect(request.getContextPath() + "/DepartmentServlet");
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
