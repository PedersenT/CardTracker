package no.tracker;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

//lage en egen klasse for scraper? kalle på metodene derfra når nødvendig
public class CardDAO {

//	add card manually to database. Used in addCard() to add cards with url, price_mine, condition
	public static void addCardManually(String name, String cardSet, String condition, double price_mine, double price_trend, String url) {
		Connection conn=null;
		String sql = "INSERT INTO card (name, cardSet, condition, price_mine, price_trend, url) VALUES(?,?,?,?,?,?)";
		try {
			conn = Database.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql); 
            pstmt.setString(1, name);
            pstmt.setString(2, cardSet);
            pstmt.setString(3, condition);
            pstmt.setDouble(4, price_mine);
            pstmt.setDouble(5, price_trend);
            pstmt.setString(6, url);
            pstmt.executeUpdate();
        } 
		catch (SQLException e) {
            System.out.println(e.getMessage());
        }
      finally {
    	  try {
    		  if (conn != null) {
              conn.close();
          }
      } 
          	  catch (SQLException ex) {
          		  System.out.println(ex.getMessage());
      }
      }
	}
//	Badly optimised. should have the code from each scrape directly in this method
	public static void addCard(String pageUrl, double myPrice, String cardCondition) {
		addCardManually(scrapeName(pageUrl), scrapeSet(pageUrl), cardCondition, myPrice, scrapePrice_trend(pageUrl), pageUrl);
	}
	
	private static String scrapeSet(String pageUrl) {
		String url = pageUrl;
		String set=null;
		try {
			final Document doc = Jsoup.connect(url).get();
			set = doc.select("dd.col-xl-7.col-6:nth-of-type(3)").text();
		}
		catch(IOException e) {
			System.out.println(e);
		}
		return(set);
	}
	
	public static double scrapePrice_trend(String pageUrl) {
		String url = pageUrl;
		double price_trend=0;
		try {
			final Document doc = Jsoup.connect(url).get();
			final String input_trend = doc.select("dd.col-xl-7.col-6:nth-of-type(10)").text();
			final String temp_trend1 = input_trend.replace(",", ".");
			final String temp_trend2 = temp_trend1.replace("€", "");
			price_trend = Double.parseDouble(temp_trend2);
		}
		catch(IOException e) {
			System.out.println(e);
		}
		return(price_trend); 
	}
	
//	output er navn og sett. hente fra url og fjerne bindestreker
//	(vil da også fjerne bindestreker i navnet)? burde få til fra
//	tittel på siden
//	kun "" rundt kortnavnet. bruke " nr to som end på det vil scraper?
	public static String scrapeName(String pageUrl) {
		String url = pageUrl;
		String name=null;
		try {
			final Document doc = Jsoup.connect(url).get();
			name = doc.select("h1#text").text();
		}
		catch(IOException e) {
			System.out.println(e);
		}
		System.out.println(name);
		return(name);
	}
	
	public static void updatePrice_trend(String pageUrl) {
		Connection conn=null;
		String sql = "UPDATE card SET price_trend=? WHERE url=?";
		try {
			conn = Database.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql); 
            pstmt.setDouble(1, scrapePrice_trend(pageUrl));
            pstmt.setString(2, pageUrl);
            pstmt.executeUpdate();
		}
		catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		finally {
	    	  try {
	    		  if (conn != null) {
	              conn.close();
	    		  }
	    	  }	    	  
	          	  catch (SQLException ex) {
	          		  System.out.println(ex.getMessage());
	    }
		}
	}

	public static void database_update() {
		
		double diff_percent=0.05;
		Connection conn=null;
		String query_url = "SELECT url, price_mine, name FROM card";
		String query_update = "UPDATE card SET price_trend=? WHERE url=?";
		
		try {
			conn=Database.connect();
			Statement stmt = conn.createStatement(); 
			ResultSet rs = stmt.executeQuery(query_url);
			
			System.out.println(" new /old  | diff| name");
			
			while(rs.next()) {
				String url = rs.getString(1);
				Double price_mine = rs.getDouble(2);
				String name = rs.getString(3);
				Double newTrend=scrapePrice_trend(url);
				PreparedStatement pstmt = conn.prepareStatement(query_update); 
	            pstmt.setDouble(1, newTrend);
	            pstmt.setString(2, url);
	            pstmt.executeUpdate();
	            
	            if (price_mine!=newTrend) {
//	    			checking the difference in %
	    			double diff = (Math.abs(price_mine-newTrend))/((price_mine+newTrend)/2);
	    			
	    			if(diff>diff_percent) {
	    				System.out.println(newTrend + "/" + price_mine + " | " + String.format("%.0f", diff*100) + "% | " + name);
	    				
//	    			    do you want to update price_mine with newTrend? 
//	    			    user input -> y/n	
//	    			    if (user input=y){	
//	    			    update price mine with price_trend	
	    			}
	    		}
			}
		}
		catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		finally {
			try {
				if (conn != null) {
					conn.close();
          }
      } 
			catch (SQLException ex) {
				System.out.println(ex.getMessage());
      }
      }
	}


	
	public void printAllCards() {
		
	}
	
	public void removeCard(String name) {
		
		
	}
	
	public void updateName(String url) {
		
	}
	
	public void updatePrice_mine(String pageUrl) {
		
	}
	

	public void updateUrl(String url) {
		
	}

}
