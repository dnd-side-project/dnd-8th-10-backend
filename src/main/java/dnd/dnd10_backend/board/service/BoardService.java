package dnd.dnd10_backend.board.service;

import dnd.dnd10_backend.board.domain.Notice;
import dnd.dnd10_backend.board.domain.Post;
import dnd.dnd10_backend.board.domain.PostCheck;
import dnd.dnd10_backend.board.dto.request.PostCreateDto;
import dnd.dnd10_backend.board.dto.request.PostUpdateDto;
import dnd.dnd10_backend.board.dto.response.*;
import dnd.dnd10_backend.board.repository.NoticeRepository;
import dnd.dnd10_backend.board.repository.PostCheckRepository;
import dnd.dnd10_backend.board.repository.PostRepository;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.exception.CustomerNotFoundException;
import dnd.dnd10_backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 패키지명 dnd.dnd10_backend.board.service
 * 클래스명 BoardService
 * 클래스설명
 * 작성일 2023-02-28
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-28] 게시글 작성, 조회, 삭제 개발 - 이우진
 * [2023-03-01] 게시글 체크, 수정 기능 개발 - 이우진
 * [2023-03-01] 카테고리 별 게시글 리스트 조회 기능 개발 - 이우진
 * [2023-03-03] 알림 생성 기능 추가 - 이우진
 */

@Service
@RequiredArgsConstructor
public class BoardService {

    private final PostRepository postRepository;
    private final PostCheckRepository postCheckRepository;

    @Transactional
    public Post write(PostCreateDto postCreateDto, User user) {
        Post post = postCreateDto.toEntity(user);
        postRepository.save(post);
        return post;
    }

    @Transactional
    public void update(Long postId, PostUpdateDto dto, User user) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_POST));

        if(!user.getUserCode().equals(post.getUserCode())) {
            throw new CustomerNotFoundException(CodeStatus.UNAUTHORIZED_UPDATED_USER);
        }
        post.update(dto.getTitle(), dto.getContent(), dto.getCategory());
        postRepository.save(post);
    }

    @Transactional
    public void delete(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_POST));
        postRepository.delete(post);
    }

    @Transactional
    public void updateView(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_POST));
        post.updateView(post.getViewCount());
        postRepository.save(post);
    }

    public PostResponseDto get(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_POST));
        boolean check = postCheckRepository.existsByPostAndUser(post, user);


        return PostResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .checkCount(post.getCheckCount())
                .category(post.getCategory())
                .userCode(post.getUserCode())
                .userName(post.getUserName())
                .role(post.getRole())
                .createDate(post.getCreateDate())
                .modifiedDate(post.getModifiedDate())
                .comments(post.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList()))
                .check(check)
                .build();
    }

    //게시글 체크
    public boolean findCheck(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_POST));

        return postCheckRepository.existsByPostAndUser(post, user);
    }

    @Transactional
    public CheckResponseDto checkPost(Long postId, User user) {
        //체크를 누르지 않은 상태의 경우
        if(!findCheck(postId, user)) {
            Post post = postRepository.findById(postId).orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_POST));
            post.plusCheck(post.getCheckCount());
            postRepository.save(post);
            PostCheck postCheck = PostCheck.builder()
                    .post(post)
                    .user(user)
                    .build();
            postCheckRepository.save(postCheck);

            return CheckResponseDto.builder()
                    .checkCount(post.getCheckCount())
                    .status(true)
                    .build();
        } else {
            //이미 체크 버튼을 눌렀었던 경우
            Post post = postRepository.findById(postId).orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_POST));
            postCheckRepository.deleteByPostAndUser(post, user);
            post.minusCheck(post.getCheckCount());
            postRepository.save(post);

            return CheckResponseDto.builder()
                    .checkCount(post.getCheckCount()) // 값이 반영되는지 확인해야함
                    .status(false)
                    .build();
        }
    }

    public List<CheckUserResponseDto> getCheckUserList(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_POST));
        List<PostCheck> postChecks = postCheckRepository.findByPost(post);
        List<CheckUserResponseDto> checkUserDtos = postChecks.stream()
                .map(p -> new CheckUserResponseDto(p.getUser().getUserCode(), p.getUser().getUserProfileCode(), p.getUser().getUsername(), p.getUser().getKakaoEmail()))
                .collect(Collectors.toList());

        return checkUserDtos;
    }

    public List<PostListResponseDto> getPostList(String category, User user) {

        if(category.equals("all")) {
            List<Post> posts = postRepository.findByStore(user.getStore());
            List<PostListResponseDto> postList = posts.stream()
                    .map(p -> new PostListResponseDto(p.getId(), p.getTitle(), p.getCategory(), p.getCheckCount(), p.getUserName(), p.getRole(), p.getCreateDate(), p.getModifiedDate()))
                    .collect(Collectors.toList());

            return postList;
        } else {
            List<Post> posts = postRepository.findByCategoryAndStore(category, user.getStore());
            List<PostListResponseDto> postList = posts.stream()
                    .map(p -> new PostListResponseDto(p.getId(), p.getTitle(), p.getCategory(), p.getCheckCount(), p.getUserName(), p.getRole(), p.getCreateDate(), p.getModifiedDate()))
                    .collect(Collectors.toList());

            return postList;
        }
    }

    public List<PostListResponseDto> postSearch(String keyword, User user) {
        List<Post> posts = postRepository.search(keyword, user.getStore());
        List<PostListResponseDto> postList = posts.stream()
                .map(p -> new PostListResponseDto(p.getId(), p.getTitle(), p.getCategory(), p.getCheckCount(), p.getUserName(), p.getRole(), p.getCreateDate(), p.getModifiedDate()))
                .collect(Collectors.toList());

        return postList;
    }

    public List<PostListResponseDto> getMyPost(User user) {
        List<Post> posts = postRepository.findByUserCode(user.getUserCode());
        List<PostListResponseDto> postList = posts.stream()
                .map(p -> new PostListResponseDto(p.getId(), p.getTitle(), p.getCategory(), p.getCheckCount(), p.getUserName(), p.getRole(), p.getCreateDate(), p.getModifiedDate()))
                .collect(Collectors.toList());

        return postList;
    }
}