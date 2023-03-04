package dnd.dnd10_backend.board.service;

import dnd.dnd10_backend.board.domain.Image;
import dnd.dnd10_backend.board.domain.Post;
import dnd.dnd10_backend.board.repository.ImageRepository;
import dnd.dnd10_backend.board.repository.PostRepository;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.Date;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * 패키지명 dnd.dnd10_backend.board.service
 * 클래스명 ImageService
 * 클래스설명
 * 작성일 2023-03-02
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final PostRepository postRepository;

    private String serverPath = "/home/dnd/upload/";

    /**
     * 이미지를 저장하는 메소드
     * @param postId
     * @param multipartFiles
     * @return
     * @throws Exception
     */
    @Transactional
    public List<Image> parseFileInfo( Long postId,
            List<MultipartFile> multipartFiles) throws Exception {

        // 반환을 할 파일 리스트
        List<Image> fileList = new ArrayList<>();

        // 파일이 빈 것이 들어오면 빈 것을 반환
        if (multipartFiles.isEmpty()) {
            return fileList;
        }

        // 파일 이름을 업로드 한 날짜로 바꾸어서 저장할 것이다
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String currentDate = simpleDateFormat.format(new Date());

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_POST));

        String path = serverPath+"images/" + currentDate;

        File file = new File(path);
        // 저장할 위치의 디렉토리가 존지하지 않을 경우
        if (!file.exists()) {
            // mkdir() 함수와 다른 점은 상위 디렉토리가 존재하지 않을 때 그것까지 생성
            file.mkdirs();
        }

        // 파일들을 이제 만져볼 것이다
        for (MultipartFile multipartFile : multipartFiles) {
            // 이미 DB에 게시글과 함께 존재하는 파일인지 확인
            boolean isEmpty = imageRepository.findAllByPostAndOriginalFileName(post, multipartFile.getOriginalFilename())
                    .isEmpty();
            //이미 있으면 기존의 파일을 삭제
            if(!isEmpty){
                deleteImage(postId, multipartFile.getOriginalFilename());
            }

            if (!multipartFile.isEmpty()) {
                // jpeg, png, gif 파일들만 받아서 처리할 예정
                String contentType = multipartFile.getContentType();
                String originalFileExtension;
                // 확장자 명이 없으면 이 파일은 잘 못 된 것이다
                if (ObjectUtils.isEmpty(contentType)) {
                    break;
                } else {
                    if (contentType.contains("image/jpeg")) {
                        originalFileExtension = ".jpg";
                    } else if (contentType.contains("image/png")) {
                        originalFileExtension = ".png";
                    } else if (contentType.contains("image/gif")) {
                        originalFileExtension = ".gif";
                    }
                    // 다른 파일 명이면 아무 일 하지 않는다
                    else {
                        break;
                    }
                }
                // 각 이름은 겹치면 안되므로 나노 초까지 동원하여 지정
                String newFileName = Long.toString(System.nanoTime());
                // 생성 후 리스트에 추가
                Image image = Image.builder()
                        .post(post)
                        .originalFileName(multipartFile.getOriginalFilename())
                        .savedFileName(newFileName)
                        .extension(originalFileExtension)
                        .storedFilePath(path + "/" + newFileName)
                        .fileSize(multipartFile.getSize())
                        .build();

                imageRepository.save(image);

                fileList.add(image);

                String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;

                // 저장된 파일로 변경하여 이를 보여주기 위함
                file = new File( path + File.separator + newFileName+originalFileExtension);

                try {
                    multipartFile.transferTo(file);
                } catch (IOException e) {
                    throw new CustomerNotFoundException(CodeStatus.FAIL);
                }

                file.setWritable(true); //쓰기가능설정
                file.setReadable(true);	//읽기가능설정


            }
        }

        return fileList;
    }

    /**
     * 이미지 삭제하는 메소드
     * @param postId 삭제하려는 이미지가 있는 게시글의 id
     * @return
     * @throws IOException
     */
    public void deleteImage(Long postId, String fileName) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_POST));
        Image image = imageRepository.findAllByPostAndOriginalFileName(post, fileName)
                .orElseThrow(() -> new CustomerNotFoundException(CodeStatus.FAIL));

        try{
            String srcFileName = URLDecoder.decode(fileName,"UTF-8");
            File file = new File(image.getStoredFilePath()+image.getExtension());
            boolean result = file.delete();

        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        imageRepository.delete(image);
    }

    /**
     * post 삭제 시 image 서버에서 삭제하는 메소드
     * @param postId 삭제하려는 게시글
     */
    public void deleteImageByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_POST));
        List<Image> imageList = imageRepository.findAllByPost(post);

        for(Image image: imageList){
            try{
                String srcFileName = URLDecoder.decode(image.getOriginalFileName(),"UTF-8");
                File file = new File(image.getStoredFilePath()+image.getExtension());
                boolean result = file.delete();

            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }

            imageRepository.delete(image);
        }
    }


    public List<byte[]> getImage(Long postId) throws IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_POST));

        List<Image> imageList = imageRepository.findAllByPost(post);
        List<byte[]> printImages = new ArrayList<>();

        for(Image image: imageList){
            FileInputStream fis = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            String fileDir = image.getStoredFilePath()+image.getExtension(); // 파일경로
            System.out.println(fileDir);
            try{
                fis = new FileInputStream(fileDir);
            } catch(FileNotFoundException e){
                e.printStackTrace();
            }

            int readCount = 0;
            byte[] buffer = new byte[1024];
            byte[] fileArray = null;

            try{
                while((readCount = fis.read(buffer)) != -1){
                    baos.write(buffer, 0, readCount);
                }
                fileArray = baos.toByteArray();
                fis.close();
                baos.close();
            } catch(IOException e){
                throw new CustomerNotFoundException(CodeStatus.FAIL);
            }
            printImages.add(fileArray);
        }

        return printImages;
    }
}
