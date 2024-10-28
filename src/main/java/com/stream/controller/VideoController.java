package com.stream.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.stream.entity.video;
import com.stream.payload.CustomMessage;
import com.stream.service.VideoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.InputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;






@CrossOrigin(origins ="http://127.0.0.1:5173")
@RestController
@RequestMapping("/api/v1/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

   

    @PostMapping
    public ResponseEntity<?> create(@RequestParam("file") MultipartFile file , @RequestParam("title") String titleString, @RequestParam("description") String description){

        video video = new video();
        video.setTitle(titleString);
        video.setDescription(description);;
        video.setVideoId(UUID.randomUUID().toString());

video savedVideo = videoService.saveVideo(video, file);

if (savedVideo != null) {

return ResponseEntity.status(HttpStatus.OK).body(video);
    
}else{
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CustomMessage.builder().message("Video not uploaded"). success(false).build());
}



    }


    //stream video api haita main mudda
    @GetMapping("/stream/{videoId}")
    public ResponseEntity<Resource> stream (@PathVariable("videoId") String videoId){

        video vid = videoService.getVideo(videoId);
        String contentType = vid.getContentType();
        String title = vid.getTitle();

        String filePath = vid.getFilePath();

        System.out.println("Filepath is " + filePath);


//filepath bata resource jhikera return gardyo
        Resource resource= new FileSystemResource(filePath);


        if(contentType == null){
            contentType ="application/octet-stream";
        }

        return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .body(resource);
    }



    //getAllVideos


    @GetMapping
    public List<video> streamAll(){

        return videoService.getAllVideo();
    }


    //stream video according to chunks or user need

    @GetMapping("/stream/range/{videoId}")
    public ResponseEntity<Resource> streamVideoRange(@PathVariable("videoId") String videoId,
    @RequestHeader(value= "Range",required = false) String range){
        System.out.println("Range header is " + range);

        video video = videoService.getVideo(videoId);
        String filePath = video.getFilePath(); 
        Path path =Paths.get(filePath);
        
     

        Resource res =new FileSystemResource(filePath);
        
        
//mp4 hoki mp3 ho nikaleko
        String contentType = video.getContentType();
        
        long fileLength = path.toFile().length();
        if(range == null) {
            return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .body(res);
        }
        
        long rangeStart;

        long rangeEnd;
        
        //bytes=1001 - 10005 ellai tukryaera start ra end bhanera chhutyauna paryo

        String[] ranges = range.replace("bytes=", "").split("-");

        //it becomes ranges[0] = 1001 and ranges[1] = 10005

        rangeStart=Long.parseLong(ranges[0]);
        
        
        if(ranges.length > 1) {
            rangeEnd=Long.parseLong(ranges[1]);
        }else{
rangeEnd=fileLength -1;
        }
        
        if(rangeEnd > fileLength -1){
            rangeEnd =fileLength -1;
        }
        
        System.out.println("Range start is  " + rangeStart);
        System.out.println("Range end is " + rangeEnd);
        
        
        InputStream inputStream;
        
        try {
        	inputStream= Files.newInputStream(path);
        	inputStream.skipNBytes(rangeStart);
        	long contentLength = rangeEnd - rangeStart + 1;
        	
        	HttpHeaders http = new HttpHeaders();
        	http.add("Content-Range", "bytes "+rangeStart+"-"+rangeEnd+"/"+fileLength);
        	http.add("Cache-Control","no-cache,no-store,must-revalidate");
        	http.add("Pragma","no-cache");
        	http.add("Expires", "0");
        	http.add("X-Content-Type-Options","nosniff");
        	
        	http.setContentLength(contentLength);
        	
        	return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
        			.headers(http)
        			.contentType(MediaType.parseMediaType(contentType))
        			.body(new InputStreamResource(inputStream));
			
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
        
        



    }


}
