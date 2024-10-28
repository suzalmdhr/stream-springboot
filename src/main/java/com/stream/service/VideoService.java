package com.stream.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.stream.entity.video;


public interface VideoService {

   // save video

 video saveVideo(video vids,MultipartFile multipartFile); 
    

    //get video

    public video getVideo(String vidId);

    //get all video

    public List<video> getAllVideo();

    //get video by title

    public video getVidsByTitle(String vidID);


    

}
