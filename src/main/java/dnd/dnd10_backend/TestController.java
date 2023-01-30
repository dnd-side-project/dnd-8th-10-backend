package dnd.dnd10_backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 패키지명 dnd.dnd10_backend
 * 클래스명 TestController
 * 클래스설명
 * 작성일 2023-01-30
 *
 * @author 이우진
 * @version 1.0
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "success";
    }
}
