package business.controller;

import business.jwt.LoginIgnore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
@RequestMapping("downloadExcel")
@Slf4j
public class DownloadExcelController {

    @RequestMapping(value = "/static/{fileName}.{prefix}")
    @ResponseBody
    @LoginIgnore
    public void downloadExcel(HttpServletResponse response, HttpServletRequest request,@PathVariable String fileName,@PathVariable String prefix) throws Exception {
        try {
            Resource resource = new ClassPathResource("static/"+fileName+"."+prefix);
            InputStream inputStream = resource.getInputStream();
            response.setContentType("application/zip");
            OutputStream out = response.getOutputStream();
            response.setHeader("Content-Disposition","attachment; filename=" + java.net.URLEncoder.encode(fileName,"UTF-8") +"."+prefix);
            int b =0;
            byte[] buffer =new byte[1024];
            while (b != -1) {
                b = inputStream.read(buffer);
                if (b != -1) out.write(buffer,0, b);
            }
            inputStream.close();
            out.close();
            out.flush();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
