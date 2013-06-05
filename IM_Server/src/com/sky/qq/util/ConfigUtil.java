package com.sky.qq.util;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.sky.qq.annotation.Annotate;
import com.sky.qq.annotation.Attribute;
import com.sky.qq.annotation.XmlName;
import com.sky.qq.entity.Config;

/**
 * 配置文件处理类
 * @author sky
 *
 */
public class ConfigUtil {
	
	/**
	 * 写入配置文件模板
	 * @throws IllegalAccessException 
	 * @throws Exception 
	 */
	public void writeTemplet(Object object) throws Exception{
		Document document=DocumentHelper.createDocument();
		Class<?> clz=object.getClass();
		Element root=document.addElement(clz.getSimpleName());
		XmlName name=clz.getAnnotation(XmlName.class);
		String xmlName=(name==null)?(clz.getSimpleName()+".xml"):(name.value());
		for(Field field : clz.getDeclaredFields()){
			field.setAccessible(true);
			Attribute attribute=field.getAnnotation(Attribute.class);
			String attributeName=(attribute==null)?(field.getName()):(attribute.value());
			Annotate annotate=field.getAnnotation(Annotate.class);
			if(annotate!=null){
				root.addComment(annotate.value());
			}
			Element attribuElement=root.addElement(attributeName);
		    attribuElement.setText(field.get(object).toString());
			OutputFormat format=OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			XMLWriter writer=new XMLWriter(new FileWriter(new File(ClasspathLoader.getProgramDirectory()+"/"+xmlName)),format);
			writer.write(document);
			writer.flush();
			writer.close();
		}
	}
	
	/**
	 * 读取配置文件 返回T类型
	 * @param <T>
	 * @param T
	 * @return
	 * @throws IllegalAccessException 
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public <T> T readConfigReturnT(Class<T> clz) throws Exception{
		T result=null;
		result=clz.newInstance();
		Map<String, Field> fields=new HashMap<String, Field>();
		for(Field field : clz.getDeclaredFields()){
			field.setAccessible(true);
			Attribute attribute=field.getAnnotation(Attribute.class);
			fields.put((attribute==null)?(field.getName()):(attribute.value()), field);
		}
		XmlName xmlNameAnnotation=clz.getAnnotation(XmlName.class);
		String xmlName=(xmlNameAnnotation==null)?(clz.getSimpleName()+".xml"):(xmlNameAnnotation.value());
		String xmlFilePath=ClasspathLoader.getProgramDirectory()+"/"+xmlName;
		if(!new File(xmlFilePath).exists()){
			writeTemplet(new Config());
		}
		SAXReader reader=new SAXReader();
		Document document=reader.read(new File(xmlFilePath));
		for(Element element : (List<Element>)(document.getRootElement().elements())){
			Object attributeValue=element.getText();
			Class<?> typeClass=fields.get(element.getName()).getType();
			if(typeClass.getName().equals("java.lang.Double")){
				attributeValue=Double.parseDouble(attributeValue.toString());
			}else if(typeClass.getName().equals("java.lang.Float")){
				attributeValue=Float.parseFloat(attributeValue.toString());
			}else if(typeClass.getName().equals("java.lang.Long")){
				attributeValue=Long.parseLong(attributeValue.toString());
			}else if(typeClass.getName().equals("java.lang.Boolean")){
				String tempValue=attributeValue.toString();
				attributeValue=(tempValue.equals("1"))?(true):(false);
			}else if(typeClass.getName().equals("java.lang.Short")){
				attributeValue=Short.parseShort(attributeValue.toString());
			}else if(typeClass.getName().equals("java.lang.Integer")){
				attributeValue=Integer.parseInt(attributeValue.toString());
			}else if(typeClass.getName().equals("java.lang.Byte")){
				attributeValue=Byte.parseByte(attributeValue.toString());
			}
			fields.get(element.getName()).set(result, attributeValue);
		}
		return result;
	}
	
}
