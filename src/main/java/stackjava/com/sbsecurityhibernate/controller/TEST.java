package stackjava.com.sbsecurityhibernate.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TEST {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			
			  String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36";

		    // Spoof the scraping by telling the page from where the request has been sent:
		      String REFERRER = "https://www.google.com";
		    
		      Document page = Jsoup.connect("https://www.amazon.com/dp/B08YCG4XNM")
		                .userAgent(USER_AGENT)
		                .referrer(REFERRER)
		                .get();
			page.outerHtml();
			//Thread.sleep(10000);
			Thread.sleep(5000);
			Element link2 = page.selectFirst("#productTitle");
			
			if(link2==null) {
				
				Thread.sleep(5000);
			}
		
			System.out.println(link2.text());
			Element link=page.select("#imgTagWrapperId").first();
			Element linkImge=link.getElementsByTag("img").first();
			
			
			 String linkimage = "https://m.media-amazon.com/images/I/" + linkImge.attr("src").split("%7C")[2];
				System.out.println(linkimage);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
