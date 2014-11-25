package com.javathinking.sample2.type1.batch.input;

import com.javathinking.sample2.type1.batch.input.model.*;
import com.javathinking.sample2.type1.transaction.QTransaction;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.H2Templates;
import com.mysema.query.sql.HSQLDBTemplates;
import com.mysema.query.sql.SQLQuery;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.RowCallbackHandler;

import javax.sql.DataSource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Type1BeanIoStreamingWriter {
    private StreamFactory factory;

    public Type1BeanIoStreamingWriter() {
        factory = StreamFactory.newInstance();
        factory.loadResource("transactionFile.xml");
    }

    public void write(File file, String client, ResultSet resultSet) throws Exception {
        Writer out = new BufferedWriter(new FileWriter(file));
        BeanWriter beanWriter = factory.createWriter("transactionFile", out);
        beanWriter.write(new Header(new DateTime()));
        beanWriter.write(new GroupHeader(client));
        int count = 0;
        while (resultSet.next()) {
            beanWriter.write(new TransactionLine(resultSet.getString("account"), new DateTime(resultSet.getDate("date")), resultSet.getBigDecimal("amount")));
            count++;
        }
        beanWriter.write(new GroupFooter(new BigDecimal(count)));
        beanWriter.write(new Footer(BigDecimal.ONE));
        beanWriter.close();
        out.close();
    }
}
