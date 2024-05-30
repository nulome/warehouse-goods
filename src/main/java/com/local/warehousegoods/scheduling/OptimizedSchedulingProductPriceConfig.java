package com.local.warehousegoods.scheduling;

import com.local.warehousegoods.model.Product;
import com.local.warehousegoods.related.CustomExceptionValid;
import com.local.warehousegoods.related.TrackingExecutionTimer;
import com.local.warehousegoods.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Optional;

@Configuration
@EnableScheduling
@ConditionalOnProperty(
        value = "app.scheduling.enabled",
        havingValue = "true",
        matchIfMissing = false)
@ConditionalOnExpression(
        "${app.scheduling.optimization:true} and !'${spring.profiles.active}'.equals('local')")
public class OptimizedSchedulingProductPriceConfig {

    @Autowired
    private ProductRepository productRepository;
    final double priceIncreasePercentage;
    private final String saveFile = "db/SaveFileProducts.csv";
    final double PERCENT_100 = 100;
    private final String url;
    private final String username;
    private final String password;
//    private final static String SQL_SELECT_ALL = "BEGIN WORK; LOCK TABLE products IN ACCESS SHARE MODE; " +
//            "SELECT id, article, title, description, price, quantity FROM products ORDER BY article; COMMIT WORK ";

    private final static String SQL_SELECT_ALL = "SELECT id, article, title, description, price, quantity FROM products ORDER BY article ";
    private final static String SQL_UPDATE_STMT = "UPDATE products SET title = ?, description = ?, price = ?, quantity = ? WHERE article = ? ";

    public OptimizedSchedulingProductPriceConfig(@Value("${app.scheduling.priceIncreasePercentage:10000}") double priceIncreasePercentage,
                                                 @Value("${spring.datasource.url}") String url,
                                                 @Value("${spring.datasource.username}") String username,
                                                 @Value("${spring.datasource.password}") String password
    ) {
        this.priceIncreasePercentage = priceIncreasePercentage;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Scheduled(initialDelay = 1)
    public void print() {
        System.out.println("****    Run Optimized Scheduling    ****");
    }

    @TrackingExecutionTimer
    @Transactional
    @Scheduled(fixedDelayString = "${app.scheduling.period:10000}", initialDelay = 5000)
    public void schedulePriceProductIncrease() throws SQLException {
        System.out.println("Start Increase Price With Model Product.");
        Connection conn = DriverManager.getConnection(url, username, password);
        Statement statement = conn.createStatement();

        Optional<Product> product = productRepository.findByArticle(1);
        product.ifPresent(value -> System.out.println("Start Price: " + value.getPrice()));

        ResultSet results = statement.executeQuery(SQL_SELECT_ALL);
        try (Writer fileWriter = new FileWriter(saveFile)) {
            fileWriter.write("id,article,title,description,price,quantity\n");
            int count = 0;
            try (PreparedStatement prepareStmt = conn.prepareStatement(SQL_UPDATE_STMT)) {
                for (int i = 0; i <= count; i++) {
                    if (results.next()) {
                        rsSaveInFileAndBatch(fileWriter, results, prepareStmt);
                        count++;
                    }
                    if(count % 100000 == 0){
                        prepareStmt.executeBatch();
                        System.out.println("executeBatch " + count);
                    }
                }

                prepareStmt.executeBatch();
                System.out.println("executeBatch " + count % 100000);
            }
        } catch (IOException e) {
            throw new CustomExceptionValid("Не удается сохранить в файл" + Paths.get(saveFile).getFileName());
        }
        product = productRepository.findByArticle(3);
        product = productRepository.findByArticle(1);
        product.ifPresent(value -> System.out.println("End Price: " + value.getPrice()));
        System.out.println("Finish Increase Price Base Scheduling.");
    }

    protected void rsSaveInFileAndBatch(Writer fileWriter, ResultSet rs, PreparedStatement prepareStmt) throws IOException, SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append(rs.getString("id")).append(",");
        sb.append(rs.getInt("article")).append(",");
        sb.append(rs.getString("title")).append(",");
        prepareStmt.setString(1, rs.getString("title"));
        sb.append(rs.getString("description")).append(",");
        prepareStmt.setString(2, rs.getString("description"));

        double newPrice = updPrice(rs.getDouble("price"));
        sb.append(newPrice).append(",");
        prepareStmt.setDouble(3, newPrice);

        sb.append(rs.getInt("quantity")).append(",");
        prepareStmt.setInt(4, rs.getInt("quantity"));
        prepareStmt.setInt(5, rs.getInt("article"));
        fileWriter.write(sb.toString() + "\n");
        prepareStmt.addBatch();
    }

    private double updPrice(Double price) {
        int incPrice = (int) (price * priceIncreasePercentage * PERCENT_100);
        return ((double) incPrice) / PERCENT_100 / PERCENT_100 + price;
    }
}
