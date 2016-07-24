package org.xiaotuitui.testframework;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

public class ProxyUtil {
    /**
     * if the given bean is a "aop proxy" bean, then return the target bean from
     * given bean. if not, return itself.
     * 
     * @param given bean
     * @return Object -- target bean from given bean or given bean itself
     * @throws Exception 
     * */
    public static final Object unwrapProxy(Object bean){
        if (AopUtils.isAopProxy(bean) && bean instanceof Advised) {
            Advised advised = (Advised) bean;
            try {
				bean = advised.getTargetSource().getTarget();
			} catch (Exception e) {
				e.printStackTrace();
				throw new TestException(e);
			}
        }
        return bean;
    }

}