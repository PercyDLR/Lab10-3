package com.example.lab103.Daos;

import com.example.lab103.Beans.Country;
import com.example.lab103.Beans.Region;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class CountryDao extends DaoBase {

    public ArrayList<Country> listar() {

        ArrayList<Country> lista = new ArrayList<>();

        try (Connection conn = this.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM countries")) {

            while (rs.next()) {
                Country country = new Country();
                country.setCountryId(rs.getString(1));
                country.setCountryName(rs.getString(2));
                country.setRegionId(rs.getBigDecimal(3));
                lista.add(country);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public void crear(String countryId, String countryName, BigDecimal regionId) {
        try {
            try (Connection conn = this.getConnection();) {
                System.out.println(countryName);
                String sql = "INSERT INTO countries (`country_id`, `country_name`, `region_id`) "
                        + "VALUES (?,?,?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, countryId);

                    if (regionId == null) {
                        pstmt.setNull(3, Types.BIGINT);
                    } else {
                        pstmt.setBigDecimal(3, regionId);
                    }

                    if (countryId == null) {
                        pstmt.setNull(2, Types.VARCHAR);
                    } else {
                        pstmt.setString(2, countryName);
                    }

                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Country obtener(String countryId) {

        Country country = null;
        try {
            String sql = "SELECT * FROM countries WHERE country_id = ?";
            try (Connection conn = this.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(sql);) {
                pstmt.setString(1, countryId);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        country = new Country();
                        country.setCountryId(rs.getString(1));
                        country.setCountryName(rs.getString(2));
                        country.setRegionId(rs.getBigDecimal(3));
                    }
                }

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return country;
    }

    public void actualizar(String countryId, String countryName, BigDecimal regionId) {
        try {
            try (Connection conn = this.getConnection();) {
                String sql = "UPDATE countries SET country_name = ?, region_id = ? "
                        + "WHERE country_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(3, countryId);

                    if (regionId == null) {
                        pstmt.setNull(2, Types.BIGINT);
                    } else {
                        pstmt.setBigDecimal(2, regionId);
                    }

                    if (countryId == null) {
                        pstmt.setNull(1, Types.VARCHAR);
                    } else {
                        pstmt.setString(1, countryName);
                    }
                    pstmt.executeUpdate();
                }

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void borrar(String countryId) {
        try {
            try (Connection conn = this.getConnection();) {
                String sql = "DELETE FROM countries WHERE country_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, countryId);
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Region> listarRegiones() {

        ArrayList<Region> listaRegiones = new ArrayList<>();
        String sql = "SELECT * FROM hr.regions;";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql);) {

            while (rs.next()) {
                Region region = new Region();
                region.setId(rs.getInt(1));
                region.setNombre(rs.getString(2));
                listaRegiones.add(region);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listaRegiones;

    }

}
