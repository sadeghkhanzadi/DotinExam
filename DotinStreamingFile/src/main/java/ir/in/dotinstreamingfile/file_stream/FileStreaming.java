package ir.in.dotinstreamingfile.file_stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileStreaming {
    private final HttpServletRequest request;

    @Autowired
    public FileStreaming(HttpServletRequest request) {
        this.request = request;
    }

    //input file
    public String getFile(MultipartFile file) throws IOException {
        if (!file.isEmpty()){
            try {
                String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss-"));
                String fileName = date + file.getOriginalFilename();
                String realPathtoUploads = this.request.getServletContext().getRealPath("/");
                if (! new File(realPathtoUploads).exists()){
                    new File(realPathtoUploads).mkdir();
                }
                //folderPath
                String orgName = file.getOriginalFilename();
                String filePath = realPathtoUploads + orgName;
                File dest = new File(filePath);
                file.transferTo(dest);
                Resource FF = new FileSystemResource(filePath);
                return filePath;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    } //End Method

    //Save file to the disk
    public String saveFile(List<TextModel> text){
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss-"));
        String filename = "result" + date + "txt";
        String realPathtoUploads = this.request.getServletContext().getRealPath("/");
        String filePath = realPathtoUploads + filename;
        Path output = Paths.get(filename);
        try {
            PrintWriter pwr = new PrintWriter(new FileOutputStream(filePath));
            for (TextModel textModel : text){
                pwr.println(textModel);
            }
            pwr.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return filePath;
    }
}
