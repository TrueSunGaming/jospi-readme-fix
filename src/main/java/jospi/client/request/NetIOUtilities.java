package jospi.client.request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jospi.client.components.Dictionary;
import jospi.enums.DescriptionEnum;
import jospi.models.records.HttpRecord;

public interface NetIOUtilities {
    /**
    * Utility method for easy conversion from a {@code Map} of Key-Value pairs
    * to a {@code String} in application/x-www-form-urlencoded encoding.
    *
    * @param params {@code Map<String, String>} of parameters that will be encoded
    * @return String containing coverted fields in application/x-www-form-urlencoded encoding
    */
    default String toFormUrl(Map<String, String> params) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            }
            else {
                result.append('&');
            }

            result.append(encode(entry.getKey()));
            result.append('=');
            result.append(encode(entry.getValue()));
        }
        return result.toString();
    }

    /**
    * Utility method for easy conversion from a {@code Dictionary} of Key-Value pairs
    * to a Query String
    *
    * @param paramFunc {@code Dictionary<String, Object>} of parameters that will be encoded
    * @return Query String of the fields in parameter {@code paramFunc}
    */
    default String toQueryString(Dictionary<String, Object> paramFunc) {
        HashMap<String, Object> map = new HashMap<>();
        paramFunc.apply(map);
        return toQueryString(map);
    }

    /**
    * Utility method for easy conversion from a {@code Map} of Key-Value pairs
    * to a Query String
    *
    * @param params {@code Map<String, Object>} of parameters that will be encoded
    * @return Query String of the fields in parameter {@code params}
    */
    default String toQueryString(Map<String, Object> params) {
        StringBuilder out = new StringBuilder("");
        params.entrySet().stream().filter(entry -> entry.getValue() != null).forEach(entry -> {
            final Object value = entry.getValue();
            if (value instanceof HttpRecord) {
                out.append(toQueryString(((HttpRecord) value).convert()));
                return;
            }
            out.append(String.format("&%s=", encode(entry.getKey())));
            if (value instanceof Enum<?>) {
                if (value instanceof DescriptionEnum) {
                    out.append(((DescriptionEnum) value).getDescription());
                } else {
                    out.append(value.toString());
                }
            } else if (value instanceof LocalDateTime) {
                out.append(((LocalDateTime) value).toString());

            } else {
                out.append(encode(value.toString()));
            }
        });
        out.deleteCharAt(0);
        return new String(out);
    }

    /**
    * Utility method for encoding a String into UTF-8 Format
    *
    * @param str String to be encoded
    * @return String that is encoded in UTF-8 Format
    */
    public static String encode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return " ";
    }
}
