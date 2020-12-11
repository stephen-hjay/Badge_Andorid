package tools;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassToJson {
    public static String convert(Object obj) {
        Class c = obj.getClass();
        Class parent = c.getSuperclass();
        Set<String> fieldName = new HashSet<>();
        List<Field> fields = new ArrayList<>();
        for (Field f : c.getDeclaredFields()) {
            fields.add(f);
            fieldName.add(f.getName());
        }
        for (Field f : parent.getDeclaredFields()) {
            if (!fieldName.contains(f.getName()))
                fields.add(f);
        }

        StringBuffer sb = new StringBuffer();
        sb.append("{");
        // test case
        try {
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {//skip static fields
                    continue;
                }
                String key = field.getName();
                field.setAccessible(true);
                if (List.class.isAssignableFrom(field.getType())) {
                    sb.append("\"" + key + "\":" + "[");
                    for (Object item : (List<Object>) field.get(obj)) {
                        if(item instanceof String){
                            sb.append("\"");
                        }
                        sb.append(item);
                        if(item instanceof String){
                            sb.append("\"");
                        }
                        sb.append(",");
                    }
                    if(!((List<Object>) field.get(obj)).isEmpty()) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    sb.append("],");
                } else if (field.getType().equals(String.class)) {
                    String val = field.get(obj).toString();
                    sb.append("\"" + key + "\":\"" + val + "\",");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");

        return sb.toString();
    }
}
