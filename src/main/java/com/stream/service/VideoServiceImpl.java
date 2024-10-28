package com.stream.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;

import com.stream.dao.VideoRepo;
import com.stream.entity.video;

import java.nio.file.Files;
import java.nio.file.Path; 
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption; 



@Service
public class VideoServiceImpl implements VideoService{

    @Value("${files.video}")
    String dir;

    @Autowired
    private VideoRepo videoRepo;





    @PostConstruct
     public void init(){
            File file=new File(dir);

            if(!file.exists()){
                file.mkdir();
                System.out.println("Folder created");
            }
            else{
                System.out.println("folder already created");
            }
    }

    

    @Override
    public video getVideo(String vidId) {
       video vid = videoRepo.findById(vidId).orElseThrow(() -> new RuntimeException("No such video"));
       return vid;
    }

    @Override
    public List<video> getAllVideo() {
        return videoRepo.findAll();
    }

    @Override
    public video saveVideo(video vids, MultipartFile File) {
        

        try {

            //original filename
//         String fileName= File.getOriginalFilename();

//         String contentType=File.getContentType();

//         InputStream inputStream = File.getInputStream();
// String cleanFileName = StringUtils.cleanPath(fileName);

// String cleanFolder = StringUtils.cleanPath(dir);
// Path path = (Path)Paths.get(cleanFolder,cleanFileName); 
// System.out.println("Path is " + path);

//filename dincha elle
String originalFilename = File.getOriginalFilename();
String contentType = File.getContentType();

InputStream inputStream = File.getInputStream();

String cleanedFilename = StringUtils.cleanPath(originalFilename);


String cleanDir = StringUtils.cleanPath(dir);

Path path = Paths.get(cleanDir, cleanedFilename);

System.out.println(path);

Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);


vids.setContentType(contentType);;
vids.setFilePath(path.toString());

return videoRepo.save(vids);

        
        
            
        } catch (IOException e) {
          e.printStackTrace();
        }
        

return null;
       

        
    }

    @Override
    public video getVidsByTitle(String vidID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getVidsByTitle'");
    }



}
