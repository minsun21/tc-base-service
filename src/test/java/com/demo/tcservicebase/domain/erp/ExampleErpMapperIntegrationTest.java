package com.demo.tcservicebase.domain.erp;

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
 * resources/mapper/erp/Exampleerpmapper.xml의 SQL이 실제로 유효하게 동작하는지
 * H2 인메모리 DB로 검증하는 통합테스트.
 */
class ExampleErpMapperIntegrationTest {

    private SqlSessionFactory sqlSessionFactory;

    @BeforeEach
    void setUp() throws Exception {
        DataSource dataSource = new UnpooledDataSource(
                "org.h2.Driver", "jdbc:h2:mem:erp-mapper-test;DB_CLOSE_DELAY=-1", "sa", "");

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS material_stock");
            statement.execute("CREATE TABLE material_stock (material_code VARCHAR(50) PRIMARY KEY, stock_qty INT)");
            statement.execute("INSERT INTO material_stock (material_code, stock_qty) VALUES ('ITEM-0001', 150)");
        }

        Configuration configuration = new Configuration(new Environment("test", new JdbcTransactionFactory(), dataSource));
        try (InputStream is = Resources.getResourceAsStream("mapper/erp/Exampleerpmapper.xml")) {
            new XMLMapperBuilder(is, configuration, "mapper/erp/Exampleerpmapper.xml", configuration.getSqlFragments()).parse();
        }

        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }

    @Test
    void findStockQuantityByItemCode는_존재하는_재고수량을_반환한다() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ExampleErpMapper mapper = session.getMapper(ExampleErpMapper.class);

            assertThat(mapper.findStockQuantityByItemCode("ITEM-0001")).isEqualTo(150);
        }
    }

    @Test
    void findStockQuantityByItemCode는_존재하지_않으면_null을_반환한다() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ExampleErpMapper mapper = session.getMapper(ExampleErpMapper.class);

            assertThat(mapper.findStockQuantityByItemCode("NO-SUCH-ITEM")).isNull();
        }
    }
}
