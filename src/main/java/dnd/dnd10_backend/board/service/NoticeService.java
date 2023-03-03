package dnd.dnd10_backend.board.service;

import dnd.dnd10_backend.board.domain.Notice;
import dnd.dnd10_backend.board.domain.Post;
import dnd.dnd10_backend.board.dto.response.NoticeResponseDto;
import dnd.dnd10_backend.board.dto.response.PostListResponseDto;
import dnd.dnd10_backend.board.repository.NoticeRepository;
import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 패키지명 dnd.dnd10_backend.board.service
 * 클래스명 NoticeService
 * 클래스설명
 * 작성일 2023-03-03
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-03-03] 알림 생성 기능 추가 - 이우진
 */

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createNotice(Post post, User user) {

        List<User> users = userRepository.findByStore(user.getStore());
        for(User u : users) {
            if(u == user) {
                continue;
            }
            Notice notice = Notice.builder()
                    .postId(post.getId())
                    .category(post.getCategory())
                    .title(post.getTitle())
                    .checked(false)
                    .user(u)
                    .type("post")
                    .build();
            noticeRepository.save(notice);
        }
    }

    @Transactional
    public void createCommentNotice(User user, String email, Long postId, String content) {
        User addressee = userRepository.findByKakaoEmail(email);
        Notice notice = Notice.builder()
                .postId(postId)
                .category(user.getUsername())
                .title(content)
                .checked(false)
                .user(addressee)
                .type("comment")
                .build();
        noticeRepository.save(notice);
    }

    public List<NoticeResponseDto> getNotice(User user) {
        List<Notice> notices = noticeRepository.findByUser(user);
        List<NoticeResponseDto> noticeList = notices.stream()
                .map(n -> new NoticeResponseDto(n.getId(), n.getPostId(), n.getCategory(), n.getTitle(), n.isChecked(), n.getUser().getRole(), n.getUser().getUsername(), n.getCreateDate(), n.getType()))
                .collect(Collectors.toList());

        return noticeList;
    }

    //알림 읽음 처리
    @Transactional
    public void read(User user) {
        List<Notice> notices = noticeRepository.findByUser(user);
        notices.forEach(Notice::setChecked);
    }

    public boolean check(User user) {
        boolean checked = false;
        return noticeRepository.existsByUserAndChecked(user, checked);
    }
}
