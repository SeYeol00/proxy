package hello.proxy.pureproxy.proxy.code;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CacheProxy implements Subject{

    // 프록시 객체가 실제 객체에 접근하기 위해서는 의존관계 주입을 받아야한다.
    private final Subject target;

    // 캐싱하는 벨류값을 가지고 있기
    // final을 넣으면 null이 올 수 없기에 안 넣고 아래에서 처리해주자.
    private String cacheValue;

    // 런타입 객체 의존 관계가
    // 클라이언트 -> 프록시 -> 진짜 객체
    // 이런 순으로 진행이 되어야한다.

    @Override
    public String operation() {
        log.info("프록시 호출");
        if(cacheValue == null){
            cacheValue = target.operation();
        }
        return cacheValue;
    }
}
