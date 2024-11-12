package com.stream.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    
    
    @Value("${file.video.hsl}")
    String hsl_dir;

    @Autowired
    private VideoRepo videoRepo;





    @PostConstruct
     public void init(){
            File file=new File(dir);
            
//            File file1=new File(hsl_dir);
//            
//            if(!file1.exists()) {
//            	file1.mkdir();
//            	System.out.println("Folder created");
//            }
            
            try {
				Files.createDirectories(Paths.get(hsl_dir));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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



	



//	@Override
//	public String processVid(String videoId) {
//		
//		//creating directories for storing videos in all qualities
//		video video = this.getVideo(videoId);
//		String filePath = video.getFilePath();
//		
//		Path videopath = Paths.get(filePath);
//		
//		
////		String output360p =hsl_dir+videoId+"/360p/";
////		String output720p =hsl_dir+videoId+"/720p/";
////		String output1080p =hsl_dir+videoId+"/1080p/";
//		
//		
//		try {
////			Files.createDirectories(Paths.get(output1080p));
////			Files.createDirectories(Paths.get(output720p));
////			Files.createDirectories(Paths.get(output360p));
//			
//			
//			Path outputPath= Paths.get(hsl_dir,videoId);
//			
//			Files.createDirectories(outputPath);
//			
//			
//			 String wslVideoPathCmd = String.format("wsl wslpath '%s'", videopath.toString());
//		        Process wslPathProcess = Runtime.getRuntime().exec(wslVideoPathCmd);
//		        
//		        
//		        BufferedReader reader = new BufferedReader(new InputStreamReader(wslPathProcess.getInputStream()));
//		        String wslVideoPath = reader.readLine(); // Converted WSL path
//
//		        try {
//					wslPathProcess.waitFor();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} // Ensure path conversion is complete
//
//		        // Verify conversion
//		        if (wslVideoPath == null || wslVideoPath.isEmpty()) {
//		            System.out.println("Path conversion failed for: " + videopath);
//		            return videoId;
//		        }
//
//			
//		        String[] ffmpegCmd = {
//		                "wsl",
//		                "ffmpeg",
//		                "-i", wslVideoPath,
//		                "-c:v", "libx264",
//		                "-c:a", "aac",
//		                "-strict", "-2",
//		                "-f", "hls",
//		                "-hls_time", "10",
//		                "-hls_list_size", "0",
//		                "-hls_segment_filename", outputPath.toString() + "/segment_%3d.ts",
//		                outputPath.toString() + "/master.m3u8"
//		            };
//			
//			// now running ffm command
//			 
//			 System.out.println("ffmpeg command is " + ffmpegCmd);
//			 ProcessBuilder processBuilder = new ProcessBuilder(ffmpegCmd);
//			 processBuilder.redirectErrorStream(true);
//			 
//			 processBuilder.inheritIO();
//			 Process process = processBuilder.start();
//			 
//			 BufferedReader reader1 = new BufferedReader(new InputStreamReader(process.getInputStream()));
//			 String line;
//			 while ((line = reader.readLine()) != null) {
//			     System.out.println(line);
//			 }
//			 
//			 
//			 int exit;
//			 System.out.println("Exit samma initialize bhayo");
//			try {
//				exit = process.waitFor();
//				System.out.println("This is working" + exit);
//				
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			 
//			 
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		
//		return videoId;
//	}
    
    
    
    @Override
    public String processVid(String videoId) {

        video video = this.getVideo(videoId);
        String filePath = video.getFilePath();

        //path where to store data:
        Path videoPath = Paths.get(filePath);


//        String output360p = HSL_DIR + videoId + "/360p/";
//        String output720p = HSL_DIR + videoId + "/720p/";
//        String output1080p = HSL_DIR + videoId + "/1080p/";

        try {
//            Files.createDirectories(Paths.get(output360p));
//            Files.createDirectories(Paths.get(output720p));
//            Files.createDirectories(Paths.get(output1080p));

            // ffmpeg command
            Path outputPath = Paths.get(hsl_dir, videoId);

            Files.createDirectories(outputPath);


            String ffmpegCmd = String.format(
                    "ffmpeg -i \"%s\" -c:v libx264 -c:a aac -strict -2 -f hls -hls_time 10 -hls_list_size 0 -hls_segment_filename \"%s/segment_%%3d.ts\"  \"%s/master.m3u8\" ",
                    videoPath, outputPath, outputPath
            );

//            StringBuilder ffmpegCmd = new StringBuilder();
//            ffmpegCmd.append("ffmpeg  -i ")
//                    .append(videoPath.toString())
//                    .append(" -c:v libx264 -c:a aac")
//                    .append(" ")
//                    .append("-map 0:v -map 0:a -s:v:0 640x360 -b:v:0 800k ")
//                    .append("-map 0:v -map 0:a -s:v:1 1280x720 -b:v:1 2800k ")
//                    .append("-map 0:v -map 0:a -s:v:2 1920x1080 -b:v:2 5000k ")
//                    .append("-var_stream_map \"v:0,a:0 v:1,a:0 v:2,a:0\" ")
//                    .append("-master_pl_name ").append(HSL_DIR).append(videoId).append("/master.m3u8 ")
//                    .append("-f hls -hls_time 10 -hls_list_size 0 ")
//                    .append("-hls_segment_filename \"").append(HSL_DIR).append(videoId).append("/v%v/fileSequence%d.ts\" ")
//                    .append("\"").append(HSL_DIR).append(videoId).append("/v%v/prog_index.m3u8\"");


            System.out.println(ffmpegCmd);
            //file this command
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", ffmpegCmd);
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            int exit = process.waitFor();
            if (exit != 0) {
                throw new RuntimeException("video processing failed!!");
            }

            return videoId;


        } catch (IOException ex) {
            throw new RuntimeException("Video processing fail!!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        
    }



}
