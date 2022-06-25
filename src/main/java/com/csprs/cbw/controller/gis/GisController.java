package com.csprs.cbw.controller.gis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/gis")
public class GisController {
	
	private final Logger logger = LoggerFactory.getLogger("CustomOperateLog");
	
	@GetMapping("/platform")
	public String index() {
		
		return "gis/index.html";
	}
	
	// 這個其實是可以把很多的資料綁再一起組成ZIP檔，但我看算了就zip包zip吧
	// https://simplesolution.dev/spring-boot-download-multiple-files-as-zip-file/
 	/*
	@RequestMapping(value="/download_gisExZip")
 	@ResponseBody
 	public void download_gisExzip(HttpServletResponse response) throws IOException{
 		List<String> listOfFileNames = new ArrayList<>();
 		File file = ResourceUtils.getFile("classpath:E.zip");
 		listOfFileNames.add(file.getAbsolutePath());
 		response.setContentType("application/zip");
 		String filename= URLEncoder.encode("圖台上傳ZIP檔案範例.zip", "UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename="+filename);
        try(ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream())) {
            for(String fileName : listOfFileNames) {
                FileSystemResource fileSystemResource = new FileSystemResource(fileName);
                ZipEntry zipEntry = new ZipEntry(fileSystemResource.getFilename());
                zipEntry.setSize(fileSystemResource.contentLength());
                zipEntry.setTime(System.currentTimeMillis());

                zipOutputStream.putNextEntry(zipEntry);

                StreamUtils.copy(fileSystemResource.getInputStream(), zipOutputStream);
                zipOutputStream.closeEntry();
            }
            zipOutputStream.finish();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
 	}
 	*/
 	
 	@RequestMapping(value="/download_gisExZip")
 	@ResponseBody
 	public ResponseEntity<Resource> download_gisExzip(HttpServletResponse response) throws IOException {
 		String filename= URLEncoder.encode("圖台上傳ZIP檔案範例.zip", "UTF-8");
 		HttpHeaders headers = new HttpHeaders();
 		File file = ResourceUtils.getFile("classpath:E.zip");
 		headers.add("Content-Type", "application/zip");
 		headers.add("Content-Disposition", "attachment; filename="+filename);

 	    InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

 	    return ResponseEntity.ok()
 	            .headers(headers)
 	            .contentLength(file.length())
 	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
 	            .body(resource);
 	}
	
}
