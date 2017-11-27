package  study.mmp.common.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 리얼에서 Pool Size는 개별 프로젝트의 study.mmp.jdbc.minimumIdle, study.mmp.jdbc.maximumPoolSize 프로퍼티로 조절해주어야 합니다.

 */
@Configuration
@EnableTransactionManagement(mode = AdviceMode.PROXY, order = 0)
@ComponentScan(basePackages = {"study.mmp.common.domain.**.dao", "study.mmp.common.domain.**.bo"}, 
	includeFilters = @Filter(value = {Service.class, Repository.class}), 
	useDefaultFilters = false
)
public class MainDbConfig {
	
    @Value("${main.jdbc.defaultMinimumIdle}") int defaultMinimumIdleMembership;
    @Value("${main.jdbc.defaultMaximumPoolSize}") int defaultMaximumPoolSizeMembership;
	
    @Value("${main.jdbc.minimumIdle:0}") int minimumIdleMembership;
    @Value("${main.jdbc.maximumPoolSize:0}") int maximumPoolSizeMembership;
  
	
    @Bean(destroyMethod = "shutdown")
    public HikariDataSource membershipDbHikariDataSource() {
        HikariConfig config = new HikariConfig();
        config.setMinimumIdle(minimumIdleMembership == 0 ? defaultMinimumIdleMembership : minimumIdleMembership);
        config.setMaximumPoolSize(maximumPoolSizeMembership == 0 ? defaultMaximumPoolSizeMembership : maximumPoolSizeMembership);

        config.setConnectionTimeout(300000); 
        config.setConnectionTestQuery("SELECT 1 FROM dual");

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "50");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.setDriverClassName("com.mysql.jdbc.Driver");

        config.addDataSourceProperty("user", "");
        config.addDataSourceProperty("password", "");
        config.setJdbcUrl("");

        return new HikariDataSource(config);
    }
    @Bean
    public DataSource membershipDataSource(HikariDataSource membershipDbHikariDataSource) {
        return new LazyConnectionDataSourceProxy(membershipDbHikariDataSource);
    }


   @Bean
    public DataSourceTransactionManager membershipTransactionManager(DataSource membershipDataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(membershipDataSource);
        return dataSourceTransactionManager;
    }
	

    @Bean
    public SqlSessionFactory membershipSqlSessionFactory(DataSource membershipDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(membershipDataSource);
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

        sqlSessionFactory.setConfigLocation(resourcePatternResolver.getResource("classpath:config/mybatis-config.xml"));
        sqlSessionFactory.setMapperLocations(resourcePatternResolver.getResources("classpath*:mapper/**/*.xml"));

        TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactory.getObject().getConfiguration().getTypeHandlerRegistry();
        typeHandlerRegistry.register(java.sql.Timestamp.class, org.apache.ibatis.type.DateTypeHandler.class);
        typeHandlerRegistry.register(java.sql.Time.class, org.apache.ibatis.type.DateTypeHandler.class);
        typeHandlerRegistry.register(java.sql.Date.class, org.apache.ibatis.type.DateTypeHandler.class);

        return sqlSessionFactory.getObject();
    }
	   
    
    @Bean(destroyMethod = "clearCache")
    public SqlSessionTemplate membershipSqlSession(SqlSessionFactory membershipSqlSessionFactory) {
        SqlSessionTemplate sessionTemplate = new SqlSessionTemplate(membershipSqlSessionFactory);
        return sessionTemplate;
    }
}
