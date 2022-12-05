package hello.proxy.pureproxy.proxy;

import hello.proxy.pureproxy.proxy.code.CacheProxy;
import hello.proxy.pureproxy.proxy.code.ProxyPatternClient;
import hello.proxy.pureproxy.proxy.code.RealSubject;
import org.junit.jupiter.api.Test;

public class ProxyPatternTest {


    @Test
    void noProxyTest(){
        RealSubject realSubject = new RealSubject();
        // 직접적으로 진짜 객체를 주입 받는 상황
        ProxyPatternClient client = new ProxyPatternClient(realSubject);
        client.execute();
        client.execute();
        client.execute();
    }

    @Test
    void cacheProxyTest(){
        RealSubject realSubject = new RealSubject();
        // 진짜 객체를 의존 하게 만든다. 의존관계 주입!
        CacheProxy cacheProxy = new CacheProxy(realSubject);
        // 클라이언트는 캐시 프록시를 의존관계로 주입 받는다.
        // 클라이언트 -> 프록시 -> 진짜 객체
        // 프록시는 인터페이스를 주입받기 때문에 실제 객체가 무엇인지 관심이 없다
        ProxyPatternClient client = new ProxyPatternClient(cacheProxy);
        client.execute();
        client.execute();
        client.execute();
        // 위에 거랑 다르게 굉장히 빠르게 처리한다.
        // 프록시가 처음에는 실제 객체에서 데이터를 요청하고
        // 두 번째부터는 프록시가 저장한 데이터를 보고 보내준다.
    }
}
