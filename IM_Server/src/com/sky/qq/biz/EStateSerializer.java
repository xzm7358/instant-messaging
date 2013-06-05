package com.sky.qq.biz;

import java.lang.reflect.Type;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sky.qq.entity.EState;

/**
 * EState枚举序列表接口
 * @author Administrator
 *
 */
public class EStateSerializer implements JsonSerializer<EState>, JsonDeserializer<EState> ,IJsonConvertSerialize<EState>{

	@Override
	public EState deserialize(JsonElement json, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		 if (json.getAsInt() < EState.values().length)  
	            return EState.values()[json.getAsInt()];  
	        return null; 
	}

	@Override
	public JsonElement serialize(EState state, Type arg1,
			JsonSerializationContext arg2) {
		 return new JsonPrimitive(state.ordinal());  
	}

	@Override
	public Class<EState> getType() {
		return EState.class;
	}

}
