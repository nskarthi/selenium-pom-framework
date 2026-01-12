package org.selenium.pom.api.actions;

import static io.restassured.RestAssured.given;

import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.selenium.pom.model.User;
import org.selenium.pom.utils.ConfigLoader;

import io.restassured.http.Cookies;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;

public class SignUpApi {
	private Cookies cookies;

	public Cookies getCookies() {
		return cookies;
	}

	private String fetchRegisterNonceValueUsingJsoup() {
		Response response = getAccount();

		// Use asString() for a raw, clean string for the parser (Jsoup, JsonPath,
		// XMLPath)
		// Use prettyPrint() only when you want to "peek" at the logs during manual
		// debugging due to below reason.
		// During prettyPrint() or asPrettyString(), Rest Assured internally modifies the HTML to add 
		// indentations and newlines for human readability. This process can sometimes break the HTML 
		// structure (especially with sensitive tags or scripts), leading Jsoup to fail its parsing logic and
		// resulting in an empty <body>
		String rawHtml = response.body().asString();

		// Parse the raw HTML
		Document doc = Jsoup.parse(rawHtml);

		// Debug: Print the WHOLE document to see where your data is located
		// System.out.println("FULL DOC:\n" + doc.outerHtml());

		// Use selectFirst directly on the 'doc' object instead of 'doc.body()'
		Element element = doc.selectFirst("#woocommerce-register-nonce");

		if (element == null) {
			throw new RuntimeException("Nonce element not found! Check if the page is JS-heavy.");
		}
		System.out.println("Register nonce: " + element.attr("value"));
		return element.attr("value");
	}

	public Response register(User user) {
		Cookies cookies = new Cookies();
		Header header = new Header("content-type", "application/x-www-form-urlencoded");
		Headers headers = new Headers(header);
		HashMap<String, String> formParams = new HashMap<>();
		formParams.put("username", user.getUsername());
		formParams.put("email", user.getEmail());
		formParams.put("password", user.getPassword());
		formParams.put("woocommerce-register-nonce", fetchRegisterNonceValueUsingJsoup());
		formParams.put("register", "Register");
		
		Response response = given().
				baseUri(ConfigLoader.getInstance().getBaseUrl()).
				headers(headers).
				formParams(formParams).
				cookies(cookies).
				log().all().
		when().
				post("/account").then().
				log().all().
				extract().
				response();

		if (response.getStatusCode() != 302)
			throw new RuntimeException("Failed to register the account, HTTP Status Code: " + response.getStatusCode());

		this.cookies = response.getDetailedCookies();
		return response;
	}
	
	private Response getAccount() {
		Cookies cookies = new Cookies();
		Response response = given().
				baseUri(ConfigLoader.getInstance().getBaseUrl()).
				cookies(cookies).
				//log().all().
		when().get("/account").then().
				//log().all().
				extract().
				response();

		if (response.getStatusCode() != 200)
			throw new RuntimeException("Failed to fetch the account, HTTP Status Code: " + response.getStatusCode());

		return response;
	}
}
