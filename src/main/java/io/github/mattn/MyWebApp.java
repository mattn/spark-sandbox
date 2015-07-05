package io.github.mattn;

import java.util.Map;;
import java.util.HashMap;
import com.google.gson.Gson;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.get;
import static spark.Spark.staticFileLocation;
import spark.servlet.SparkApplication;

public class MyWebApp implements SparkApplication {
	static final Logger LOG = LoggerFactory.getLogger(MyWebApp.class);

	@Override
	public void init() {
		Gson gson = new Gson();

		staticFileLocation("/public");

		get("/", (req, res) -> {
			Map<String, Object> attr = new HashMap<>();
			attr.put("message", "Hello ワールド");
			return new ModelAndView(attr, "index.ftl");
		}, new FreeMarkerEngine());

		get("/api", (req, res) -> {
			Map<String, Object> attr = new HashMap<>();
			attr.put("message", "Hello World!");
			res.type("application/json");
			return gson.toJson(attr);
		});
	}

	public static void main(String[] args) {
		new MyWebApp().init();
	}
}
