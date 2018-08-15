package cn.huobi.framework.util;

import org.apache.commons.lang3.StringUtils;

import cn.huobi.framework.model.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 沙
 */
public class ResponseUtil {
	
	
	/**
	 * 下载文件
	 * @param response 输出流对象
	 * @param path 文件路径
	 * @param fileName 下载文件名称
	 */
	public static void download(HttpServletResponse response, String path,String fileName) {
		File f = new File(path);
		download(response, f,fileName);
	}

	/**
	 * 下载文件
	 * @param response 输出流对象
	 * @param file 文件
	 */
	public static void download(HttpServletResponse response, File file,String fileName) {
		BufferedInputStream br = null;
		OutputStream out = null;
		try {
			File f = file;
			if (!f.exists()) {
				response.sendError(404, "File not found!");
				return;
			}
			br = new BufferedInputStream(new FileInputStream(f));
			byte[] buf = new byte[1024];
			int len = 0;

			response.reset();
			if(StringUtils.isBlank(fileName))
				fileName=f.getName();
			fileName = new String(fileName.getBytes("gb2312"), "ISO8859-1");
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
		    response.setHeader( "Content-Disposition", "attachment;filename=" + fileName);  
			out = response.getOutputStream();
			while ((len = br.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			br.close();
			out.flush();
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public static Map<String, Object> buildResponseMap(Object obj){
		boolean result = false;
		int count = 0;
		Object data = null;
		String message = "";
		if(obj == null){
			result = true;
		}else if(obj instanceof Boolean){
			result = (Boolean) obj;
		}else if(obj instanceof String){
			result = false;
			message = obj.toString();
		}else if(obj instanceof Collection){
			data = obj;
			count = ((Collection) obj).size();
			result = true;
		}else if(obj instanceof Map){
			data = obj;
			count = ((Map) obj).size();
			result = true;
		}else if(obj instanceof Throwable){
			result = false;
			if (obj instanceof BossBaseException){
				message = ((BossBaseException) obj).getMessage();
			}else{
				message = "服务器异常, 请稍后再试.";
			}
		}else{
			data = obj;
			count = 1;
			result = true;
		}
		Map<String, Object> resultMap = new HashMap();
		resultMap.put("result", result);
		resultMap.put("count", count);
		resultMap.put("message", message);
		resultMap.put("data", data);
		return resultMap;
	}

	public static Result buildResult(Object obj){
		boolean status = false;
		int count = 0;
		Object data = null;
		String message = "";
		if(obj == null){
			status = true;
		}else if(obj instanceof Boolean){
			status = (Boolean) obj;
		}else if(obj instanceof String){
			status = false;
			message = obj.toString();
		}else if(obj instanceof Collection){
			data = obj;
			count = ((Collection) obj).size();
			status = true;
		}else if(obj instanceof Map){
			data = obj;
			count = ((Map) obj).size();
			status = true;
		}else if(obj instanceof Throwable){
			status = false;
			if (obj instanceof BossBaseException){
				message = ((BossBaseException) obj).getMessage();
			}else{
				message = "服务器异常, 请稍后再试.";
			}
		}else{
			data = obj;
			count = 1;
			status = true;
		}
		Result result = new Result();
		result.setStatus(status);
		result.setMsg(message);
		result.setData(data);
		return result;
	}

	public static Map<String, Object> buildResponseMap(Object obj, int count){
		Map<String, Object> resultMap = buildResponseMap(obj);
		resultMap.put("count", count);
		return resultMap;
	}
}
