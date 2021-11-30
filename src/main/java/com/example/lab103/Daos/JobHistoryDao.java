package com.example.lab103.Daos;

import com.example.lab103.Beans.JobHistory;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.*;


public class JobHistoryDao extends DaoBase{

    public ArrayList<JobHistory> listarHistorial(){
        ArrayList<JobHistory> jh = new ArrayList<>();
        EmployeeDao ed = new EmployeeDao();

        String sql = "SELECT * FROM hr.job_history";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){

            while(rs.next()) {

                JobHistory jobHistory = new JobHistory();

                jobHistory.setEmployee(ed.obtenerEmpleado(rs.getInt(1)));
                jobHistory.setStartDate(rs.getString(2));
                jobHistory.setEndDate(rs.getString(3));

                JobDao jobDao = new JobDao();
                jobHistory.setJob(jobDao.obtenerTrabajo(rs.getString(4)));

                DepartmentDao departmentDao = new DepartmentDao();
                jobHistory.setDepartment(departmentDao.obtener(rs.getInt(5)));

                jh.add(jobHistory);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jh;
    }

    public void agregarRegistro(JobHistory jh){

        String sql = "insert into job_history (employee_id, start_date, end_date, job_id, department_id)\n" +
                "values ("+jh.getEmployee().getEmployeeId()+",'"+jh.getStartDate()+"',now(),'"+jh.getJob().getJobId()+"',"+jh.getDepartment().getDepartmentId()+");";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();){

            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
