package frc.lightning.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import edu.wpi.first.wpilibj.Filesystem;
import frc.lightning.ConstantBase;

public class MotorConfig {
    static Map<String,MotorConfig> motorConfigCache = new LinkedHashMap<>();

    String fname;
    List<BaseMotorController> motors = new LinkedList<>();
    Map<String, JSONObject> model = new LinkedHashMap<>();
    HashMap<String, Double> settings = new LinkedHashMap<>();

    private MotorConfig(String fname) {
        this.fname = Filesystem.getDeployDirectory().getAbsoluteFile() + "/../motors/" + fname;

        JSONParser parser = new JSONParser();
        JSONObject json;
        try {
            String jsonValues = new String(Files.readAllBytes(Paths.get(
                                               Filesystem.getDeployDirectory().getAbsoluteFile() + "/../motors/" + fname)),
                                           StandardCharsets.UTF_8);

            json = (JSONObject) parser.parse(jsonValues);
            JSONObject baseModel = MotorConfig.configModel();
            for (Object k : json.keySet()) {
                String key = (String) k;
                settings.put(key, ((Number) json.get(key)).doubleValue());
                model.put(key, (JSONObject) baseModel.get(key));
            }
        } catch (ParseException | IOException e) {
            json = null;
        }
    }

    static public MotorConfig get(String fname) {
        if (!motorConfigCache.containsKey(fname)) {
            motorConfigCache.put(fname, new MotorConfig(fname));
        }
        return motorConfigCache.get(fname);
    }

    public void registerMotor(BaseMotorController motor) {
        motors.add(motor);
        for (String param : settings.keySet()) {
            System.out.println("Set " + param + " with " + settings.get(param));
            set(motor, param, settings.get(param));
        }
    }

    public void resetMotor(BaseMotorController motor) {
        if (motors.contains(motor)) {
            for (String param : settings.keySet()) {
                System.out.println("Set " + param + " with " + settings.get(param));
                set(motor, param, settings.get(param));
            }
        }
    }

    public void updateParameter(String param, double value) {
        settings.put(param, value);
        for (var motor : motors) {
            set(motor, param, value);
        }

        // write to file
        try (PrintWriter out = new PrintWriter(fname)) {
            out.println((new JSONObject(settings)).toJSONString());
        } catch (FileNotFoundException e) {
            System.err.println("Unable to write to " + fname + ": " + e);
        }
    }

    private void set(BaseMotorController motor, String param, double value) {
        JSONObject config = model.get(param);
        if (config != null) {
            boolean hasSlot = (Boolean) config.get("hasSlot");
            boolean hasTimeout = (Boolean) config.get("hasTimeout");
            String type = (String) config.get("parameterType");
            int paramCount = (Integer) config.get("parameterCount");

            Object[] params = new Object[paramCount];
            try {
                Class<?>[] args = new Class[paramCount];
                int argPos = 0;
                if (hasSlot) {
                    args[argPos] = Integer.class;
                    params[argPos] = 0;
                    argPos += 1;
                }

                if (type.equals("int")) {
                    args[argPos] = Integer.class;
                    params[argPos] = (int) Math.round(value);
                    argPos += 1;
                } else if (type.equals("double")) {
                    args[argPos] = Double.class;
                    params[argPos] = value;
                    argPos += 1;
                } else if (type.equals("boolean")) {
                    args[argPos] = Boolean.class;
                    params[argPos] = (value == 0);
                    argPos += 1;
                }

                if (hasTimeout) {
                    args[argPos] = Integer.class;
                    params[argPos] = ConstantBase.CANTimeout;
                    argPos += 1;
                }
                Method meth = motor.getClass().getMethod(param, args);
                meth.invoke(motor, params);
            } catch (NoSuchMethodException err) {
                // This is generally expected if we have a mix of talons and victors
                // and want to support setting talon specific options
            } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                System.err.println("Error setting motor config: " + e);
                e.printStackTrace();
            }
        }
    }

    static public JSONObject configModel() {
        HashMap<String,Object> model = new HashMap<>();

        Arrays.stream(TalonSRX.class.getMethods()).forEach(
        (m) -> {
            String name = m.getName();
            if (name.startsWith("config") || name.startsWith("set")) {

                if (m.getParameterCount() >= 1 && m.getParameterCount() <= 3) {
                    boolean hasTimeout = false;
                    boolean hasSlot = false;
                    boolean skip = false;

                    HashMap<String,Object> obj = new HashMap<>();

                    if (model.containsKey(name)) {
                        JSONObject prevMethod = (JSONObject) model.get(name);
                        Integer prevParamCount = (Integer) prevMethod.get("parameterCount");
                        if (prevParamCount < m.getParameterCount()) {
                            model.remove(name);
                        }
                    }

                    Parameter[] parameters = m.getParameters();
                    if (parameters.length > 1 && parameters[parameters.length - 1].getType().toGenericString().equals("int")) {
                        hasTimeout = true;
                    }

                    if (hasTimeout && parameters.length > 2 && parameters[0].getType().toGenericString().equals("int")) {
                        hasSlot = true;
                    }

                    if (!hasTimeout && parameters.length > 1 && parameters[0].getType().toGenericString().equals("int")) {
                        hasSlot = true;
                    }

                    String parameterType = parameters[hasSlot ? 1 : 0].getType().toGenericString();

                    if (parameterType.contains(" ")) {
                        skip = true;
                    }

                    if (!skip) {
                        obj.put("name", name);
                        obj.put("parameterCount", m.getParameterCount());
                        obj.put("parameterType", parameterType);
                        obj.put("hasSlot", hasSlot);
                        obj.put("hasTimeout", hasTimeout);
                        model.put(m.getName(), new JSONObject(obj));
                    }
                }
            }
        });

        return new JSONObject(model);
    }

    public void delete(String params) {
        settings.remove(params);

        // write to file
        try (PrintWriter out = new PrintWriter(fname)) {
            out.print((new JSONObject(settings)).toJSONString());
        } catch (FileNotFoundException e) {
            System.err.println("Unable to write to " + fname + ": " + e);
        }
    }
}