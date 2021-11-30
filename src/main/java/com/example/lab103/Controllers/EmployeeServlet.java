package com.example.lab103.Controllers;

import com.example.lab103.Beans.Department;
import com.example.lab103.Beans.Employee;
import com.example.lab103.Beans.Job;
import com.example.lab103.Beans.JobHistory;
import com.example.lab103.Daos.DepartmentDao;
import com.example.lab103.Daos.EmployeeDao;
import com.example.lab103.Daos.JobDao;
import com.example.lab103.Daos.JobHistoryDao;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@WebServlet(name = "EmployeeServlet", urlPatterns = {"/EmployeeServlet"})
public class EmployeeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Employee em = (Employee) session.getAttribute("employeeSession");

        if (em == null) {
            response.sendRedirect(request.getContextPath());
        } else {
            String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");

            RequestDispatcher view;
            int top = (Integer) session.getAttribute("top");

            if(top == 2 && (action.equals("editar"))){
                action = "lista";

            } else if(top == 3 && (action.equals("borrar") || action.equals("agregar"))){
                action = "lista";

            } else if(top == 4 && !(action.equals("lista") || action.equals("est"))){
                action = "lista";
            }

            EmployeeDao employeeDao = new EmployeeDao();
            JobDao jobDao = new JobDao();
            DepartmentDao departmentDao = new DepartmentDao();
            JobHistoryDao jobHistoryDao = new JobHistoryDao();

            switch (action) {
                case "lista":
                    request.setAttribute("listaEmpleados", employeeDao.listarEmpleados());
                    view = request.getRequestDispatcher("employees/lista.jsp");
                    view.forward(request, response);
                    break;
                case "agregar":
                    request.setAttribute("listaTrabajos", jobDao.listarTrabajos());
                    request.setAttribute("listaDepartamentos", departmentDao.listaDepartamentos());
                    request.setAttribute("listaJefes", employeeDao.listarEmpleados());

                    view = request.getRequestDispatcher("employees/formularioNuevo.jsp");
                    view.forward(request, response);
                    break;
                case "editar":
                    if (request.getParameter("id") != null) {
                        String employeeIdString = request.getParameter("id");
                        int employeeId = 0;
                        try {
                            employeeId = Integer.parseInt(employeeIdString);
                        } catch (NumberFormatException ex) {
                            response.sendRedirect("EmployeeServlet");
                        }

                        Employee emp = employeeDao.obtenerEmpleado(employeeId);

                        if (emp != null) {
                            request.setAttribute("empleado", emp);
                            request.setAttribute("listaTrabajos", jobDao.listarTrabajos());
                            request.setAttribute("listaDepartamentos", departmentDao.listaDepartamentos());
                            request.setAttribute("listaJefes", employeeDao.listarEmpleados());
                            request.setAttribute("listaHistorial", jobHistoryDao.listarHistorial());
                            view = request.getRequestDispatcher("employees/formularioEditar.jsp");
                            view.forward(request, response);
                        } else {
                            response.sendRedirect("EmployeeServlet");
                        }

                    } else {
                        response.sendRedirect("EmployeeServlet");
                    }

                    break;
                case "borrar":
                    if (request.getParameter("id") != null) {
                        String employeeIdString = request.getParameter("id");
                        int employeeId = 0;
                        try {
                            employeeId = Integer.parseInt(employeeIdString);
                        } catch (NumberFormatException ex) {
                            response.sendRedirect("EmployeeServlet");
                        }

                        Employee emp = employeeDao.obtenerEmpleado(employeeId);

                        if (emp != null) {
                            try {
                                employeeDao.borrarEmpleado(employeeId);
                                request.getSession().setAttribute("err", "Empleado borrado exitosamente");
                            } catch (SQLException e) {
                                request.getSession().setAttribute("err", "Error al borrar el empleado");
                                e.printStackTrace();
                            }
                            response.sendRedirect(request.getContextPath() + "/EmployeeServlet");
                        }
                    } else {
                        response.sendRedirect("EmployeeServlet");
                    }
                    break;

                case "est":
                    request.setAttribute("listaSalarioPorDepa", departmentDao.listaSalarioPorDepartamento());
                    request.setAttribute("listaEmpleadPorRegion", employeeDao.listaEmpleadosPorRegion());
                    view = request.getRequestDispatcher("employees/estadisticas.jsp");
                    view.forward(request, response);
                    break;

                default:
                    response.sendRedirect("EmployeeServlet");
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Employee em = (Employee) session.getAttribute("employeeSession");


        if (em == null) {
            response.sendRedirect(request.getContextPath());
        } else {

            int top = (Integer) session.getAttribute("top");

            Employee e = new Employee();
            e.setFirstName(request.getParameter("first_name"));
            e.setLastName(request.getParameter("last_name"));
            e.setEmail(request.getParameter("email"));
            e.setPhoneNumber(request.getParameter("phone"));
            e.setHireDate(request.getParameter("hire_date"));
            e.setSalary(new BigDecimal(request.getParameter("salary")));
            e.setCommissionPct(request.getParameter("commission").equals("") ? null : new BigDecimal(request.getParameter("commission")));

            String jobId = request.getParameter("job_id");

            Job job = new Job(jobId);
            e.setJob(job);

            String managerId = request.getParameter("manager_id");
            if (!managerId.equals("sin-jefe")) {
                Employee manager = new Employee(Integer.parseInt(managerId));
                e.setManager(manager);
            }

            String departmentId = request.getParameter("department_id");
            Department department = new Department(Integer.parseInt(departmentId));
            e.setDepartment(department);

            EmployeeDao employeeDao = new EmployeeDao();

            if (request.getParameter("employee_id") == null && top < 3) {

                try {
                    employeeDao.guardarEmpleado(e);
                    session.setAttribute("msg", "Empleado creado exitosamente");
                    response.sendRedirect(request.getContextPath() + "/EmployeeServlet");
                } catch (SQLException exc) {
                    session.setAttribute("err", "Error al crear el empleado");
                    response.sendRedirect(request.getContextPath() + "/EmployeeServlet?action=agregar");
                }
            } else if (request.getParameter("employee_id") != null && (top == 1 || top == 3)) {

                Employee employee = employeeDao.obtenerEmpleado(Integer.parseInt(request.getParameter("employee_id")));
                e.setEmployeeId(Integer.parseInt(request.getParameter("employee_id")));
                try {
                    employeeDao.actualizarEmpleado(e);

                    if(!jobId.equals(employee.getJob().getJobId())){
                        JobHistoryDao jhd = new JobHistoryDao();
                        JobHistory jh = new JobHistory();
                        jh.setEmployee(employee);
                        jh.setJob(employee.getJob());
                        jh.setDepartment(employee.getDepartment());

                        boolean empleadoTieneHistorial = false;
                        for(JobHistory hist : jhd.listarHistorial()){
                            if (hist.getEmployee().getEmployeeId() == jh.getEmployee().getEmployeeId()) {
                                empleadoTieneHistorial = true;
                            }
                        }

                        if (empleadoTieneHistorial) {
                            int i = 0;
                            for (JobHistory hist : jhd.listarHistorial()) {
                                if (hist.getEmployee().getEmployeeId() == jh.getEmployee().getEmployeeId()) {
                                    System.out.println(i);
                                    if (i == 0) {
                                        jh.setStartDate(hist.getEndDate());
                                        System.out.println(jh.getStartDate());
                                    } else {

                                        String[] splitJH = jh.getStartDate().split("\\s+");
                                        String[] splitHIST = hist.getEndDate().split("\\s+");

                                        String[] dmaJH = splitJH[0].split("-");
                                        String[] hmmJH = splitJH[1].split(":");

                                        String[] dmaHIST = splitHIST[0].split("-");
                                        String[] hmmHIST = splitHIST[1].split(":");

                                        LocalDateTime dateJH = LocalDateTime.of(Integer.parseInt(dmaJH[0]), Integer.parseInt(dmaJH[1]), Integer.parseInt(dmaJH[2]), Integer.parseInt(hmmJH[0]), Integer.parseInt(hmmJH[1]), Integer.parseInt(hmmJH[2]));
                                        LocalDateTime dateHIST = LocalDateTime.of(Integer.parseInt(dmaHIST[0]), Integer.parseInt(dmaHIST[1]), Integer.parseInt(dmaHIST[2]), Integer.parseInt(hmmHIST[0]), Integer.parseInt(hmmHIST[1]), Integer.parseInt(hmmHIST[2]));

                                        if (dateJH.isBefore(dateHIST)) {
                                            jh.setStartDate(hist.getEndDate());
                                        }
                                    }
                                    i++;
                                }
                            }

                        } else {
                            jh.setStartDate(jh.getEmployee().getHireDate());
                        }
                        jhd.agregarRegistro(jh);

                    }
                    session.setAttribute("msg", "Empleado actualizado exitosamente");
                    response.sendRedirect(request.getContextPath() + "/EmployeeServlet");
                } catch (SQLException ex) {
                    session.setAttribute("err", "Error al actualizar el empleado");
                    response.sendRedirect(request.getContextPath() + "/EmployeeServlet?action=editar");
                }
            } else {
                session.setAttribute("err", "Usted NO Tiene Permisos para Realizar esta Operación");
                response.sendRedirect(request.getContextPath() + "/EmployeeServlet");
            }
        }
    }

}
