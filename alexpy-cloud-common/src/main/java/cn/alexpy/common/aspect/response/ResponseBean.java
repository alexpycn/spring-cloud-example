package cn.alexpy.common.aspect.response;

import cn.alexpy.common.base.BaseResult;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor()
@AllArgsConstructor(staticName = "of")
public class ResponseBean extends BaseResult {

    public static final long serialVersionUID = 1L;

    protected int code;

    protected String status;

    protected String message;

    protected Object data;

    @Override
    public String toString() {
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                .setPrettyPrinting()
                .disableHtmlEscaping()
//                .registerTypeAdapter(Date.class, new DateSerializer()).setDateFormat("yyyy-MM-dd HH:mm:ss")
//                .registerTypeAdapter(Date.class, new DateDeserializer()).setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        if (this.data == null) {
            this.setData(new Object());
        }
        return gson.toJson(this);
    }

    public class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringAdapter();
        }
    }

    public static class StringAdapter extends TypeAdapter<String> {
        @Override
        public String read(JsonReader jsonReader) throws IOException {

            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return "";
            } else {
                return jsonReader.nextString();
            }
        }

        @Override
        public void write(JsonWriter jsonWriter, String value) throws IOException {
            if (value == null) {
                jsonWriter.value("");
                return;
            }
            jsonWriter.value(value);
        }
    }

    public static class DateSerializer implements JsonSerializer<Date> {
        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getTime());
        }
    }

    public static class DateDeserializer implements JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            return new Date(json.getAsJsonPrimitive().getAsLong());
        }
    }

    public static final TypeAdapter<BigDecimal> BIG_DECIMAL = new TypeAdapter<BigDecimal>() {
        @Override
        public BigDecimal read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            try {
                String string = in.nextString();
                if (string.length() > 0)
                    return new BigDecimal(string);
                return null;
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public void write(JsonWriter out, BigDecimal value) throws IOException {
            out.value(value);
        }
    };

}