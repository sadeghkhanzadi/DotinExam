package ir.in.dotinstreamingfile.file_stream;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RestController
public class FileController {
    private FileStreaming fileStreaming;

    @Autowired
    public FileController(FileStreaming fileStreaming) {
        this.fileStreaming = fileStreaming;
    }

    @RequestMapping(value = "/file/reader" , method = RequestMethod.POST)
    private Object get_File(@RequestParam(value = "file") MultipartFile file){
        if (!file.isEmpty()){
            try{
                String pathh = this.fileStreaming.getFile(file);
                if (pathh != null){
                    Path path = Paths.get(pathh);
                    BufferedReader br = Files.newBufferedReader(path , StandardCharsets.UTF_8);
                    String line;
                    String []temp = null;
                    List<TextModel> textModels = new ArrayList<>();
                    TextModel textModel = new TextModel();
                    while((line = br.readLine()) != null){
                        //process the line
                        //2022-04-11 12:39:55,932 [thread-1] - starting { "amount":"262500","trx":"charge","user":"19309" }
                        //	example: 19309,charge,OK,190,308
                        try {
                            temp = line.split("(starting)+" , 2);
                            textModel = new Gson().fromJson(temp[1].trim() , TextModel.class);
                            textModels.add(textModel);
                        }catch (Exception e){
                            e.printStackTrace();
                            continue;
                        }
                    }
                    br.close();
                    String pathFile = this.fileStreaming.saveFile(textModels);
                    if (!pathFile.isEmpty()){
                        return pathFile;
                    }else {
                        return null;
                    }
                } else {
                    return null;
                }
            }catch (Exception e){
                return null;
            }
        } else {
            return null;
        }
    }
}
