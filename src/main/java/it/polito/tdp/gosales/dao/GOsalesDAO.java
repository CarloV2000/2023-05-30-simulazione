package it.polito.tdp.gosales.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.gosales.model.DailySale;
import it.polito.tdp.gosales.model.Products;
import it.polito.tdp.gosales.model.Retailers;

public class GOsalesDAO {
	
	
	/**
	 * Metodo per leggere la lista di tutti i rivenditori dal database
	 * @return
	 */

	public List<Retailers> getAllRetailers(){
		String query = "SELECT * from go_retailers";
		List<Retailers> result = new ArrayList<Retailers>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Retailers(rs.getInt("Retailer_code"), 
						rs.getString("Retailer_name"),
						rs.getString("Type"), 
						rs.getString("Country")));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	
	
	/**
	 * Metodo per leggere la lista di tutti i prodotti dal database
	 * @return
	 */
	public List<Products> getAllProducts(){
		String query = "SELECT * from go_products";
		List<Products> result = new ArrayList<Products>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Products(rs.getInt("Product_number"), 
						rs.getString("Product_line"), 
						rs.getString("Product_type"), 
						rs.getString("Product"), 
						rs.getString("Product_brand"), 
						rs.getString("Product_color"),
						rs.getDouble("Unit_cost"), 
						rs.getDouble("Unit_price")));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}

	
	/**
	 * Metodo per leggere la lista di tutte le vendite nel database
	 * @return
	 */
	public List<DailySale> getAllSales(){
		String query = "SELECT * from go_daily_sales";
		List<DailySale> result = new ArrayList<DailySale>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new DailySale(rs.getInt("retailer_code"),
				rs.getInt("product_number"),
				rs.getInt("order_method_code"),
				rs.getTimestamp("date").toLocalDateTime().toLocalDate(),
				rs.getInt("quantity"),
				rs.getDouble("unit_price"),
				rs.getDouble("unit_sale_price")  ));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}


	public List<String> getAllCountries() {
		String query = "SELECT DISTINCT r.Country "
				+ "FROM go_retailers r ";
		List<String> result = new ArrayList<String>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				String s = rs.getString("Country");
				result.add(s);
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}


	public List<Retailers> getAllRetailers(String nazione) {
		String query = "SELECT DISTINCT r.* "
				+ "FROM go_retailers r "
				+ "WHERE r.Country = ? ";
		List<Retailers> result = new ArrayList<Retailers>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, nazione);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Retailers(rs.getInt("Retailer_code"), 
						rs.getString("Retailer_name"),
						rs.getString("Type"), 
						rs.getString("Country")));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}


	public int getPeso(Retailers x, Retailers y, int anno) {
		String query = "SELECT COUNT(DISTINCT d1.Product_number)AS peso "
				+ "FROM go_retailers r1, go_retailers r2, go_daily_sales d1, go_daily_sales d2 "
				+ "WHERE r1.Retailer_code = ? "
				+ "AND r2.Retailer_code = ? "
				+ "AND YEAR(d1.Date) = ? "
				+ "AND YEAR(d2.Date) = ? "
				+ "AND d1.Retailer_code = r1.Retailer_code "
				+ "AND d2.Retailer_code = r2.Retailer_code "
				+ "AND d1.Product_number = d2.Product_number ";
		int peso = 0;
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			st.setInt(1, x.getCode());
			st.setInt(2, y.getCode());
			st.setInt(3, anno);
			st.setInt(4, anno);
			ResultSet rs = st.executeQuery();

			if (rs.first()) {
				peso = rs.getInt("peso");
			}
			conn.close();
			return peso;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}


	public List<Products> getAllProducts(Retailers r) {
		String query = "SELECT DISTINCT p.* "
				+ "FROM go_products p, go_daily_sales s "
				+ "WHERE s.Product_number = p.Product_number "
				+ "AND s.Retailer_code = ? ";
		List<Products> result = new ArrayList<Products>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			st.setInt(1, r.getCode());
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Products(rs.getInt("Product_number"), 
						rs.getString("Product_line"), 
						rs.getString("Product_type"), 
						rs.getString("Product"), 
						rs.getString("Product_brand"), 
						rs.getString("Product_color"),
						rs.getDouble("Unit_cost"), 
						rs.getDouble("Unit_price")));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}


	public List<DailySale> getAllVendite(Products p, Retailers r, int anno) {
		String query = "SELECT s.*, SUM(Quantity) AS QuantityTOT "
				+ "FROM go_products p, go_daily_sales s "
				+ "WHERE s.Product_number = p.Product_number "
				+ "AND p.Product_number = ? "
				+ "AND Retailer_code = ? "
				+ "GROUP BY DATE "
				+ "HAVING YEAR(DATE)= ? ";
		List<DailySale> result = new ArrayList<DailySale>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			st.setInt(1, p.getNumber());
			st.setInt(2, r.getCode());
			st.setInt(3, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new DailySale(rs.getInt("retailer_code"),
				rs.getInt("product_number"),
				rs.getInt("order_method_code"),
				rs.getTimestamp("date").toLocalDateTime().toLocalDate(),
				rs.getInt("quantityTOT"),
				rs.getDouble("unit_price"),
				rs.getDouble("unit_sale_price")  ));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}


	public int getAvgD(Retailers r, Products p, int anno){
		String query = "SELECT s.Retailer_code, 12*30/COUNT(*) AS avgD "
				+ "	FROM go_daily_sales s "
				+ "	WHERE s.Retailer_code = ? AND s.Product_number = ? "
				+ "	AND YEAR(s.Date) = ?";
		int result = 0;

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			st.setInt(1, r.getCode());
			st.setInt(2, p.getNumber());
			st.setInt(3, anno);
			ResultSet rs = st.executeQuery();

			if (rs.first()) {
				result = (int)rs.getDouble("avgD");
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	
	
	
	/**
	 * Metodo che legge dal database la quantità media
	 * venduta dal Retailer r in un anno, per quanto riguarda il prodotto p
	 * @param r
	 * @param p
	 * @param anno
	 * @return
	 */
	public int getAvgQ(Retailers r, Products p, int anno){
		String query = "SELECT s.Retailer_code, SUM(s.Quantity)/COUNT(*) AS avgQ "
				+ "	FROM go_daily_sales s "
				+ "	WHERE s.Retailer_code = ? AND s.Product_number = ? "
				+ "	AND YEAR(s.Date) = ?";
		int result = 0;

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			st.setInt(1, r.getCode());
			st.setInt(2, p.getNumber());
			st.setInt(3, anno);
			ResultSet rs = st.executeQuery();

			if (rs.first()) {
				result = (int)rs.getDouble("avgQ");
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	
}
