package hello.proxy.config.v4_postProcessor;


import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.config.v4_postProcessor.postprocessor.PackageLogTracePostProcessor;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
// 여기서 임포트를 쓸 수 있다.
// 지금의 경우 v1, v2가 수동 빈 등록 설정을 가지고 있기 때문에
// 스프링 컨테이너에 넣기 위해서 이렇게 한다.
// 여기서 임포트로 받아서 생성할 것이다.
// v3는 컴포넌트 스캔의 대상이 되기 때문에 안 가져와도 된다.
@Import({AppV1Config.class, AppV2Config.class})
public class BeanPostProcessorConfig {

    @Bean
    public PackageLogTracePostProcessor logTracePostProcessor(LogTrace logTrace){
        return new PackageLogTracePostProcessor("hello.proxy.app",getAdvisor(logTrace));
    }

    private Advisor getAdvisor(LogTrace logTrace) {
        // pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*","order*","save*");
        //advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        // 어드바이저는 하나의 포인트컷과 하나의 어드바이스를 가진다.
        return new DefaultPointcutAdvisor(pointcut,advice);
    }
}
