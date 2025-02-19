package com.sparta.oishitable.global.aop.aspect;

import com.sparta.oishitable.global.aop.annotation.DistributedLock;
import com.sparta.oishitable.global.exception.DistributedLockException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
public class RedissonLockAspect {

    private final RedissonClient redissonClient;

    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        String key = getLockKey(joinPoint, distributedLock.key());
        long waitTime = distributedLock.waitTime();
        long leaseTime = distributedLock.leaseTime();
        TimeUnit timeUnit = distributedLock.unit();

        RLock lock = redissonClient.getLock(key);

        try {
            boolean available = lock.tryLock(waitTime, leaseTime, timeUnit);

            if (!available) {
                throw new DistributedLockException(ErrorCode.LOCK_ACQUISITION_FAILED);
            }

            return joinPoint.proceed();
        } finally {
            lock.unlock();
        }
    }

    private String getLockKey(ProceedingJoinPoint joinPoint, String keyExpression) {
        // 메서드 인자 가져오기
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 파라미터 이름 가져오기
        String[] parameterNames = signature.getParameterNames();

        // Spring Expression Language 로 파싱하여 읽어옴
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < args.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        // SpEL로 request.date 가져오기
        LocalDateTime date = parser.parseExpression("#request.date").getValue(context, LocalDateTime.class);

        if (date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
            String formattedDate = date.format(formatter);
            context.setVariable("formattedDate", formattedDate);
        }

        return parser.parseExpression(keyExpression).getValue(context, String.class);
    }
}
