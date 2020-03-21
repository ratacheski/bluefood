package br.com.ratacheski.bluefood.infrastructure.web.controller;

import br.com.ratacheski.bluefood.application.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ImageController {

    @Autowired
    private ImageService imageService;

    @ResponseBody
    @GetMapping(path = "/images/{type}/{imgName}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getBytes(
            @PathVariable(name = "type") String type,
            @PathVariable(name = "imgName") String imgName) {
        return imageService.getBytes(type, imgName);
    }
}
