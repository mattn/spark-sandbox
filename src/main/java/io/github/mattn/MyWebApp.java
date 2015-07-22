package io.github.mattn;

import java.util.Map;;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.google.gson.Gson;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import freemarker.template.Configuration;

import static spark.Spark.get;
import static spark.Spark.staticFileLocation;
import spark.servlet.SparkApplication;

public class MyWebApp implements SparkApplication {
	static final Logger LOG = LoggerFactory.getLogger(MyWebApp.class);

	@Override
	public void init() {
		Gson gson = new Gson();

		try {
			Class.forName("org.h2.Driver");
		} catch (Throwable e) {
			e.printStackTrace();
		}

		staticFileLocation("/public");

		get("/", (req, res) -> {
			Map<String, Object> attr = new HashMap<>();
			attr.put("message", "Hello ワールド");
			return new ModelAndView(attr, "index.ftl");
		}, createFreeMarkerEngine());

		get("/api", (req, res) -> {
			Map<String, Object> attr = new HashMap<>();
			attr.put("message", "Hello ワールド");

			Connection conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
			PreparedStatement stmt = conn.prepareStatement(
					"select * from books order by id");
			ResultSet rs = stmt.executeQuery();
			List<String> names = new ArrayList<>();
			while (rs.next()) {
				names.add(rs.getString("name"));
			}
			attr.put("values", names);
			conn.close();
			res.type("application/json; charset=utf-8");
			return gson.toJson(attr);
		});
	}

	private FreeMarkerEngine createFreeMarkerEngine() {
		Configuration configuration = new Configuration();
		configuration.setClassForTemplateLoading(FreeMarkerEngine.class, "");
		configuration.setDefaultEncoding("UTF-8");
		return new FreeMarkerEngine(configuration);
	}

	public static void main(String[] args) {
		new MyWebApp().init();
	}
}
