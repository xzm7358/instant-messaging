package com.sky.qq.biz;

import java.lang.reflect.Type;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sky.qq.entity.EOwnState;

/**
 * EState枚举序列表接口
 * @author Administrator
 *
 */
public class EOwnStateSerializer implements JsonSerializer<EOwnState>, JsonDeserializer<EOwnState> ,IJsonConvertSerialize<EOwnState>{
 
	@Override 
	public EOwnState deserialize(JsonElement json, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		 if (json.getAsInt() < EOwnState.values().length)  
	            return EOwnState.values()[json.getAsInt()];  
	        return null; 
	}

	@Override
	public JsonElement serialize(EOwnState state, Type arg1,
			JsonSerializationContext arg2) {
		 return new JsonPrimitive(state.ordinal());  
	}

	@Override
	public Class<EOwnState> getType() {
		return EOwnState.class;
	}



}
