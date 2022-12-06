package hello.proxy.pureproxy.decorator.code;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeDecorator implements Component{

    private Component component;

    public TimeDecorator(Component component) {
        this.component = component;
    }

    @Override
    public String operation() {
        log.info("TimeDecorator 실행");
        long startTime = System.currentTimeMillis();
        // 여기서 다음 프록시를 호출한다.
        String result = component.operation();
        // 즉 의존관계 주입을 한 다음 프록시가 여기서 비즈니스 로직이 된다.
        // DFS를 생각하면 좀 편할듯?
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeDecorator 종료 resultTime = {}ms", resultTime);

        return result;
    }
}
