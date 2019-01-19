package frc.lightning.http;

import static spark.Spark.*;
import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.neuland.jade4j.Jade4J;
import de.neuland.jade4j.template.JadeTemplate;
import edu.wpi.first.wpilibj.Filesystem;
import frc.lightning.ConstantBase;
import frc.lightning.logging.DataLogger;
import frc.lightning.util.FaultCode;
import spark.Request;
import spark.utils.urldecoding.UrlDecode;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import frc.lightning.util.MotorConfig;

public class LightningServer {
    public static ConstantBase constants;

    private static String requestInfoToString(Request request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.requestMethod());
        sb.append(" " + request.url());
        sb.append(" " + request.body());
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public static void start_server() {
        System.out.println("Start the server");
        port(8620);
        staticFiles.externalLocation(Filesystem.getDeployDirectory().getAbsolutePath());
        staticFiles.registerMimeType("log", "text/comma-separated-values");
        webSocket("/datalog", DataLoggingWebSocket.class);

        before((request, response) -> {
            System.out.println(requestInfoToString(request));
        });

        get("/constants", (req, res)-> {
            if (req.contentType() == "application/json") {
                res.type("application/json");
                return constants.toJSONString();
            } 

            String fname = Filesystem.getDeployDirectory().getAbsolutePath() + "/constants.jade";
            JadeTemplate template = Jade4J.getTemplate(fname);
            return Jade4J.render(template, constants.getModel());
        });

        get("/faults", (req, res)-> {
            String fname = Filesystem.getDeployDirectory().getAbsolutePath() + "/faults.jade";
            JadeTemplate template = Jade4J.getTemplate(fname);
            return Jade4J.render(template, FaultCode.getModel());
        });

        get("/faultlist", (req, res)-> {
            res.type("application/json");
            return FaultCode.toJSONString();
        });

        get("/faultlog", (req, res)-> {
            String faults = new String(Files.readAllBytes(Paths.get(
                Filesystem.getDeployDirectory().getAbsoluteFile() + "/../faults.log")), 
                StandardCharsets.UTF_8);
            return faults;
        });

        get("/constant/:fld", (req, res)-> {
            // res.type("application/json");
            return constants.toJSONString(req.params(":fld"));
        });

        get("/motor/model", (req, res) -> {
            return MotorConfig.configModel().toJSONString();
        });

        delete("/motor/config/:file/:name", (req, res) -> {
            MotorConfig config = MotorConfig.get(req.params(":file"));
            config.delete(req.params(":name"));
            return "";
        });

        put("/motor/config/:file/:name/:value", (req, res) -> {
            MotorConfig config = MotorConfig.get(req.params(":file"));

            String param = req.params(":value");
            double value;
            if (param.equals("on") || param.equals("true")) {
                value = 1;
            } else if (param.equals("off") || param.equals("false")) {
                value = 0;
            } else {
                value = Double.parseDouble(param);
            }

            config.updateParameter(req.params(":name"), value);
            return "";
        });

        post("/update_motor/:file", (req, res) -> {
            String[] values = req.body().split("&");
            String name = UrlDecode.path(values[1].split("=")[1].replace("+", " "));
            String param = UrlDecode.path(values[0].split("=")[1].replace("+", " "));            

            double value;
            if (param.equals("on") || param.equals("true")) {
                value = 1;
            } else if (param.equals("off") || param.equals("false")) {
                value = 0;
            } else {
                value = Double.parseDouble(param);
            }

            MotorConfig config = MotorConfig.get(req.params(":file"));
            config.updateParameter(name, value);

            return param;
        });

        get("/motor/config", (req, res) -> {
            File motorDir = new File("/home/lvuser/motors");
            motorDir.mkdir();
            JSONArray result = new JSONArray();
            for (File f : motorDir.listFiles()) {
                result.add(f.getName());
            }

            JSONObject model = new JSONObject();
            model.put("motors",  result);

            String fname = Filesystem.getDeployDirectory().getAbsolutePath() + "/motor.jade";
            JadeTemplate template = Jade4J.getTemplate(fname);
            return Jade4J.render(template, model);
        });

        get("/motor/:name", (req, res) -> {
            Path motor = Paths.get("/home/lvuser/motors/" + req.params(":name"));
            return new String(Files.readAllBytes(motor));
        });

        post("/motor/:name", (req, res) -> {
            String fname = "/home/lvuser/motors/" + req.params(":name");
            try (PrintWriter out = new PrintWriter(fname)) {
                out.print(req.body());
            }
            return "";
        });

        get("/motors", (req, res) -> {
            File motorDir = new File("/home/lvuser/motors");
            motorDir.mkdir();
            JSONArray result = new JSONArray();
            for (File f : motorDir.listFiles()) {
                result.add(f.getName());
            }
            return result.toJSONString();
        });

        post("/constant", (req, res)-> {
            String[] values = req.body().split("&");
            String name = UrlDecode.path(values[1].split("=")[1].replace("+", " "));
            String value = UrlDecode.path(values[0].split("=")[1].replace("+", " "));
            
            Object obj;
            JSONParser parser = new JSONParser();
            try {
                obj = parser.parse(value);
            } catch (ParseException e) {
                obj = value;
            }
            constants.updateFromObject(name, obj);
            constants.writeToFile();

            return value;
        });

        post("/constants", (req, res) -> {
            // res.type("application/json");
            constants.updateFromJSON(req.body());
            System.out.println("Updating: " + req.body());
            constants.writeToFile();

            return constants.toJSONString();
        });

        get("/header", (req,res) -> {
            return DataLogger.getLogger().getJSONHeader();
        });
        
        // get("/log/:file", (req, res)-> {
        //     // res.type("application/json");
        //     File log = new File("/u/log/" + req.params(":file"));
        //     var stream = res.raw().getOutputStream();
        //     res.
        //     stream.write(Files.readAllBytes(log.toPath()));

        //     return res;
        // });

        get("/logs", (req, res) -> {
            JSONArray json = new JSONArray();

            File logDir = new File("/u/log/");
            for (var f : logDir.list()) {
                json.add(f);
            }

            return json.toJSONString();
        });
    }

    public static void stop_server() {
        System.out.println("Stop the server");
        stop();
    }
}