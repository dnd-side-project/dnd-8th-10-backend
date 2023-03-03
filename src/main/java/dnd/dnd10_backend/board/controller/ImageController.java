package dnd.dnd10_backend.board.controller;

import dnd.dnd10_backend.board.domain.Image;
import dnd.dnd10_backend.board.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageController {
    private final ImageService imageService;

    @PostMapping(value = "/image/upload/{postId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity uploadImages(@RequestPart List<MultipartFile> files,
                                      @RequestParam Long postId) throws Exception{
        List<Image> list = imageService.parseFileInfo(postId, files);
        return ResponseEntity.ok().body("저장 완료");
    }

    @GetMapping("/image/view")
    public ResponseEntity viewImages(@RequestParam Long postId) throws Exception{
        List<byte[]> images = imageService.getImage(postId);
        return ResponseEntity.ok().body(images);
    }
}
