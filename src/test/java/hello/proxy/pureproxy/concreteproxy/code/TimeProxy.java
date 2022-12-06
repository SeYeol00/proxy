package hello.proxy.pureproxy.concreteproxy.code;


import lombok.extern.slf4j.Slf4j;

@Slf4j // 인터페이스를 안 만들고 상속을 하면 부모 타입이 자식한테 할당되기 때문에 주입 가능하다.
public class TimeProxy extends ConcreteLogic{

    private ConcreteLogic concreteLogic;


    public TimeProxy(ConcreteLogic concreteLogic) {
        this.concreteLogic = concreteLogic;
    }

    @Override
    public String operation(){
        log.info("TimeDecorator 실행");
        long startTime = System.currentTimeMillis();
        // 여기서 다음 프록시를 호출한다.
        String result = concreteLogic.operation();
        // 즉 의존관계 주입을 한 다음 프록시가 여기서 비즈니스 로직이 된다.
        // DFS를 생각하면 좀 편할듯?
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeDecorator 종료 resultTime = {}ms", resultTime);

        return result;
    }


}
