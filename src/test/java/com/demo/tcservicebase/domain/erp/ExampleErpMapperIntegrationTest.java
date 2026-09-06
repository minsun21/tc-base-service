package com.demo.tcservicebase.domain.erp;

import com.demo.tcservicebase.domain.erp.dto.MaterialStockDetail;
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
            statement.execute("CREATE TABLE material_stock ("
                    + "material_code VARCHAR(50) PRIMARY KEY, stock_qty INT, warehouse_code VARCHAR(50))");
            statement.execute("INSERT INTO material_stock (material_code, stock_qty, warehouse_code) "
                    + "VALUES ('ITEM-0001', 150, 'WH-01')");
        }

        Configuration configuration = new Configuration(new Environment("test", new JdbcTransactionFactory(), dataSource));
        // 실제 ErpDataSourceConfig와 동일하게 스네이크→카멜 자동 매핑을 켜서 findStockDetailByItemCode(resultType DTO) 매핑을 검증
        configuration.setMapUnderscoreToCamelCase(true);
        // 실제 ErpDataSourceConfig의 setTypeAliasesPackage와 동일하게 등록해서 resultType="MaterialStockDetail"(단순명)이 풀리도록 함
        configuration.getTypeAliasRegistry().registerAliases("com.demo.tcservicebase.domain.erp.dto");
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

    @Test
    void findStockDetailByItemCode는_컬럼들을_DTO_필드에_자동매핑해서_반환한다() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ExampleErpMapper mapper = session.getMapper(ExampleErpMapper.class);

            MaterialStockDetail detail = mapper.findStockDetailByItemCode("ITEM-0001");

            assertThat(detail.getMaterialCode()).isEqualTo("ITEM-0001");
            assertThat(detail.getStockQty()).isEqualTo(150);
            assertThat(detail.getWarehouseCode()).isEqualTo("WH-01");
        }
    }

    @Test
    void findStockDetailByItemCode는_존재하지_않으면_null을_반환한다() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ExampleErpMapper mapper = session.getMapper(ExampleErpMapper.class);

            assertThat(mapper.findStockDetailByItemCode("NO-SUCH-ITEM")).isNull();
        }
    }
}
