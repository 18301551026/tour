package com.lxs.oa.protal.action;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.stereotype.Controller;

@Controller
@Namespace("/protal")
@Actions({ @Action(value = "protal", className = "protalAction", results = {
		@Result(name = "download", type = "stream", params = {
				"inputName", "file", "contentDisposition",
				"attachment;filename=\"${fileName}\"" }),	
})
})
public class ProtalAction {
	private String tempFileName;
	
	public String getFileName() throws Exception{
		String fileName="";
		if (tempFileName.equals("fireFox")) {
			fileName="firefox-29.0a1.en-US.win64-x86_64.zip";
		}
		fileName=(new String(fileName.getBytes("UTF-8"),
				"ISO8859-1"));
		return fileName;
	}
	public InputStream getFile() throws Exception {
		InputStream in = null;
		String fileName="";
		if (tempFileName.equals("fireFox")) {
			fileName="firefox-29.0a1.en-US.win64-x86_64.zip";
		}
		fileName=(new String(fileName.getBytes("UTF-8"),
				"ISO8859-1"));
		in = new FileInputStream(new java.io.File(ServletActionContext
				.getServletContext().getRealPath("/")
				+ "tools/"
				+ fileName));
//		in = new ByteArrayInputStream(att.getContent());
		return in;
	}
	public String download(){
		
		return "download";
	}
	public String getTempFileName() {
		return tempFileName;
	}
	public void setTempFileName(String tempFileName) {
		this.tempFileName = tempFileName;
	}
}
