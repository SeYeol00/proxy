package hello.proxy.app.v1;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// 컨트롤러를 인터페이스를 만들어서 처리하는 경우는 거의 없다.
// 여기에서는 리퀘스트 매핑과 리스폰스 바디 어노테이션을 이용해서
// REST Controller처럼 사용할 것이다.
// 스프링을 @Controller 또는 @RequestMapping 어노테이션이 있어야
// 스프링 컨트롤러로 인식
@RequestMapping
@ResponseBody
public interface OrderControllerV1 {

    @GetMapping("/v1/request")
    String request(@RequestParam("itemId") String itemId);

    @GetMapping("/v1/no-log")
    String noLog();
}
