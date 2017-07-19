package test

import org.springframework.beans.BeansException
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.boot.context.embedded.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.session.ExpiringSession
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession
import org.springframework.session.web.http.SessionRepositoryFilter
import redis.clients.jedis.Protocol
import redis.embedded.RedisServer

@Configuration
@EnableRedisHttpSession
class SessionConfig {

    @Bean
    JedisConnectionFactory connectionFactory() {
        def factory = new JedisConnectionFactory()
        if (isRedisServerAvailable()) {
            // get env vars
            factory.setHostName(redisHost())
            factory.setPort(redisPort())
        }
        return factory
    }

    @Bean
    RedisMessageListenerContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer()
        container.setConnectionFactory(connectionFactory())
        container
    }

    @Bean
    RedisServerBean redisServer() {
        new RedisServerBean()
    }

    @Bean
    FilterRegistrationBean springSessionFilter(SessionRepositoryFilter<? extends ExpiringSession> filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean()
        registrationBean.setFilter(filter)
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 10)
        registrationBean
    }

    @Bean
    FilterRegistrationBean sessionSynchronizerFilter(HttpSessionSynchronizer filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean()
        registrationBean.setFilter(filter)
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 11)
        registrationBean
    }

    protected String redisHost() {
        System.getenv('REDIS_SERVICE_HOST')
    }

    protected Integer redisPort() {
        Integer.valueOf(System.getenv('REDIS_SERVICE_PORT'))
    }

    static boolean isRedisServerAvailable() {
        System.getenv('REDIS_SERVICE_HOST') != null
    }

    static class RedisServerBean implements InitializingBean, DisposableBean, BeanDefinitionRegistryPostProcessor {
        private static RedisServer redisServer

        @Override
        void afterPropertiesSet() throws Exception {
            // only start when there is no redis host set
            if (!isRedisServerAvailable() && redisServer == null) {
                redisServer = new RedisServer(Protocol.DEFAULT_PORT)
                redisServer.start()
            }
        }

        @Override
        void destroy() throws Exception {
            if (redisServer != null) {
                redisServer.stop()
                redisServer = null
            }
        }

        @Override
        void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        }

        @Override
        void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        }

        protected String redisHost() {
            System.getenv('REDIS_SERVICE_HOST')
        }

    }
}
