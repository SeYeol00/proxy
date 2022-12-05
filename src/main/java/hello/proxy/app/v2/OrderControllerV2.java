package hello.proxy.app.v2;

import hello.proxy.app.v1.OrderServiceV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequiredArgsConstructor
@RequestMapping // @Controller를 안 쓰는 이유는 그걸 쓰면 컴포넌트 스캔의 대상이 되기 때문에 수동 빈 등록이 불가능하기 때문이다.
@ResponseBody
public class OrderControllerV2 {


    private final OrderServiceV2 orderService;

    @GetMapping("/v2/request")
    public String request(@RequestParam("itemId") String itemId) {
        orderService.orderItem(itemId);
        return "ok";
    }

    @GetMapping("/v2/no-log")
    public String noLog() {
        return "ok";
    }
}
