package frc.lightning;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// May be better implemented with GSON

public class ConstantBase {
    public static final int CANTimeout = 100;
    public static double settleTime = 15;

    protected String getFileName() {
        return "~/config.yaml";
    }

    private String getResolvedFileName() {
        return getFileName().replaceFirst("^~", System.getProperty("user.home"));
    }

    private void withEachField(Consumer<Field> func) {
        for (Field field : ConstantBase.class.getDeclaredFields()) {
            if (!java.lang.reflect.Modifier.isFinal(field.getModifiers()))
                func.accept(field);
        }

        for (Field field : this.getClass().getDeclaredFields()) {
            if (!java.lang.reflect.Modifier.isFinal(field.getModifiers()))
                func.accept(field);
        }
    }

    private void withEachStaticField(Consumer<Field> func) {
        withEachField((Field f) -> {
            if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
                func.accept(f);
            }
        });
    }

    public String toJSONString(String fldName) {
        try {
            Field f = this.getClass().getField(fldName);
            return fldAsValue(f).toString();
        } catch (NoSuchFieldException | SecurityException e) {
            System.err.println("Unable to access constant field " + fldName);
            e.printStackTrace();
        }
        return "";
    }

    @SuppressWarnings("unchecked")
    private Object fldAsValue(Field f) {
        try {
            f.setAccessible(true);
            Class<?> type = f.getType();
            if (type.isArray()) {
                JSONArray list = new JSONArray();
                Class<?> ctype = type.getComponentType();

                if (!ctype.isPrimitive()) {
                    Object[] value;
                    value = (Object[]) f.get(this);
                    for (var v : value) {
                        list.add(v);
                    }
                } else if (ctype.equals(long.class)) {
                    long[] value = (long[]) f.get(this);
                    for (var v : value) {
                        list.add(v);
                    }
                } else if (ctype.equals(int.class)) {
                    int[] value = (int[]) f.get(this);
                    for (var v : value) {
                        list.add(v);
                    }
                } else if (ctype.equals(double.class)) {
                    double[] value = (double[]) f.get(this);
                    for (var v : value) {
                        list.add(v);
                    }
                }
                return list;
            } else {
                return f.get(this);
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            System.err.println("Unexpected error accessing constant: " + e);
            e.printStackTrace();
        }
        return null;
    }

    public Map<String,Object> getModel() {
        Set<String> keys = new LinkedHashSet<>();
        Map<String,Object> result = new HashMap<>();
        Map<String,Object> constants = new HashMap<>();

        withEachStaticField((Field f) -> {
            try {
                constants.put(f.getName(), fldAsValue(f));
                keys.add(f.getName());
            } catch (IllegalArgumentException e) {
                System.err.println("Unexpected error accessing constant: " + e);
                e.printStackTrace();
            }
        });

        result.put("keys", keys);
        result.put("constants", constants);
        return result;
    }

    @SuppressWarnings("unchecked")
    public String toJSONString() {
        JSONObject json = new JSONObject();

        withEachStaticField((Field f) -> {
            try {
                json.put(f.getName(), fldAsValue(f));
            } catch (IllegalArgumentException e) {
                System.err.println("Unexpected error accessing constant: " + e);
                e.printStackTrace();
            }
        });

        return json.toJSONString();
    }

    public void writeToFile() {
        OutputStream output = null;

        try {
            output = new FileOutputStream(getResolvedFileName());
            Writer writer = new OutputStreamWriter(output);
            writer.append(toJSONString());
            writer.close();
        } catch (IOException io) {
            System.err.println("Unexpected error writing to " + getResolvedFileName() + ": " + io);
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    System.err.println("Unexpected closing " + getResolvedFileName() + ": " + e);
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateFromJSON(String jsonString) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject json = (JSONObject) parser.parse(jsonString);
            updateFromJSON(json);
        } catch (ParseException e) {
            System.err.println("Error parsing JSON: " + e);
            e.printStackTrace();
        }
    }

    public void updateFromObject(String name, Object object) {
        try {
            Field f = this.getClass().getField(name);
            f.setAccessible(true);

            Class<?> klass = f.getType();
            if (klass.isArray()) {
                ArrayList<?> list = (ArrayList<?>) object;
                Class<?> ctype = klass.getComponentType();
                if (!ctype.isPrimitive()) {
                    f.set(this, list.toArray());
                } else if (ctype.equals(long.class)) {
                    Object array = Array.newInstance(ctype, list.size());
                    for (int i = 0; i < list.size(); ++i) {
                        Array.set(array, i, ((Long) list.get(i)).longValue());
                    }
                    f.set(this, array);
                } else if (ctype.equals(int.class)) {
                    Object array = Array.newInstance(ctype, list.size());
                    for (int i = 0; i < list.size(); ++i) {
                        Array.set(array, i, ((Integer) list.get(i)).intValue());
                    }
                    f.set(this, array);
                } else if (ctype.equals(double.class)) {
                    Object array = Array.newInstance(ctype, list.size());
                    for (int i = 0; i < list.size(); ++i) {
                        Array.set(array, i, ((Double) list.get(i)).doubleValue());
                    }
                    f.set(this, array);
                }
            } else {
                f.set(this, object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateFromJSON(JSONObject json) {
        withEachStaticField((Field f) -> {
            String name = f.getName();
            if (json.containsKey(name)) {
                updateFromObject(name, json.get(name));
            }
        });
    }

    public void readFromFile() {
        InputStream input = null;

        try {
            input = new FileInputStream(getResolvedFileName());
            JSONParser parser = new JSONParser();
            String text = new String(input.readAllBytes());
            JSONObject json = (JSONObject) parser.parse(text);
            updateFromJSON(json);
        } catch (FileNotFoundException err) {
            System.err.println("Unable to read file " + getResolvedFileName());
        } catch (Exception err) {
            System.err.println("Unexpected err: " + err);
            err.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println("Error closing file in readFromFile: " + e);
                    e.printStackTrace();
                }
            }
        }
    }
}
