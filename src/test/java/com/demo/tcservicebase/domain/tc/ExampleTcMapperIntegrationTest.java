package com.demo.tcservicebase.domain.tc;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * resources/mapper/tc/ExampleTcMapper.xml의 SQL이 실제로 유효하게 동작하는지
 * H2 인메모리 DB로 검증하는 통합테스트.
 * (기존 ItemQueryServiceTest는 Mapper를 Mock으로 대체하므로 SQL 자체는 검증하지 않음)
 */
class ExampleTcMapperIntegrationTest {

    private SqlSessionFactory sqlSessionFactory;

    @BeforeEach
    void setUp() throws Exception {
        DataSource dataSource = new UnpooledDataSource(
                "org.h2.Driver", "jdbc:h2:mem:tc-mapper-test;DB_CLOSE_DELAY=-1", "sa", "");

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS item");
            statement.execute("CREATE TABLE item (item_id VARCHAR(50) PRIMARY KEY, item_name VARCHAR(100))");
            statement.execute("INSERT INTO item (item_id, item_name) VALUES ('ITEM-0001', '볼트 M8')");
        }

        Configuration configuration = new Configuration(new Environment("test", new JdbcTransactionFactory(), dataSource));
        try (InputStream is = Resources.getResourceAsStream("mapper/tc/ExampleTcMapper.xml")) {
            new XMLMapperBuilder(is, configuration, "mapper/tc/ExampleTcMapper.xml", configuration.getSqlFragments()).parse();
        }

        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }

    @Test
    void findItemNameById는_존재하는_품목의_이름을_반환한다() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ExampleTcMapper mapper = session.getMapper(ExampleTcMapper.class);

            assertThat(mapper.findItemNameById("ITEM-0001")).isEqualTo("볼트 M8");
        }
    }

    @Test
    void findItemNameById는_존재하지_않으면_null을_반환한다() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ExampleTcMapper mapper = session.getMapper(ExampleTcMapper.class);

            assertThat(mapper.findItemNameById("NO-SUCH-ITEM")).isNull();
        }
    }
}
