package hello.proxy.config.v4_postProcessor.postprocessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

// 빈 후처리기 모든 클래스에 한꺼번에 프록시팩토리를 적용시킬 것이다.
// 이러면 빈 Configuration에서 중복된 등록 코드를 안 써도 된다.
@Slf4j
@RequiredArgsConstructor
public class PackageLogTracePostProcessor implements BeanPostProcessor {

    // 특정 패키지 하위에 있는 항목들만 적용시킬 예정이다.
    private final String basePackage;
    // 어드바이저가 있어야 프록시 객체에 어드바이스와 포인트컷을 넣어줄 수 있다.
    private final Advisor advisor;


    // 빈의 초기화가 다 끝나고 나서 적용할 것이기 때문에
    // 포스트 프로세스 에프터 이니셜라이제이션을 쓴다.
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.info("param beanName = {}, bean = {}", beanName, bean.getClass());

        // 프록시 적용 대상 여부 체크
        // 프록시 적용 대상이 아니면 원본을 그대로 전달
        String packageName = bean.getClass().getPackageName();
        // 프록시를 적용 안 할 클래스들 먼저 거르자
        // 프록시가 아닌 원본을 그대로 전달할 것이다.
        if(!packageName.startsWith(basePackage)){
            return bean;
        }

        // 프록시 대상이면 프록시를 만들어서 반환
        // 여기서 빈 => 타겟
        ProxyFactory proxyFactory = new ProxyFactory(bean);
        proxyFactory.addAdvisor(advisor);
        // 여러 클래스들에 한꺼번에 적용할 것이기에 가장 상위 타입인 오브젝트를 쓰자.
        Object proxy = proxyFactory.getProxy();
        log.info("create proxy: target = {}, proxy = {}",bean.getClass(),proxy.getClass());
        return proxy;
    }
}
