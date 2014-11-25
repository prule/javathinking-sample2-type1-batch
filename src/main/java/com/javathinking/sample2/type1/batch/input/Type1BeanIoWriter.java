package com.javathinking.sample2.type1.batch.input;

import com.javathinking.sample2.type1.batch.input.model.*;
import com.javathinking.sample2.type1.transaction.Transaction;
import com.javathinking.sample2.type1.transaction.TransactionRepository;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.joda.time.DateTime;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;

public class Type1BeanIoWriter {
    private StreamFactory factory;
    private TransactionRepository transactionRepository;

    public Type1BeanIoWriter(TransactionRepository transactionRepository) {
        factory = StreamFactory.newInstance();
        factory.loadResource("transactionFile.xml");
        this.transactionRepository = transactionRepository;
    }

    public void write(File file, String client, DateTime date1, DateTime date2) throws IOException {
        Writer out = new BufferedWriter(new FileWriter(file));
        BeanWriter beanWriter = factory.createWriter("transactionFile", out);
        beanWriter.write(new Header(new DateTime()));
        beanWriter.write(new GroupHeader(client));
        final long count = transactionRepository.countByClientAndDateBetween(client, date1, date2);
        Pageable pageRequest = new PageRequest(0, 1000);
        do {
            final List<Transaction> transactions = transactionRepository.findAllByClientAndDateBetween(client, date1, date2, pageRequest);
            for (Transaction transaction : transactions) {
                beanWriter.write(new TransactionLine(transaction.getAccount(), transaction.getDate(), transaction.getAmount()));
            }
            pageRequest = pageRequest.next();
        } while (pageRequest.getOffset() < count);
        beanWriter.write(new GroupFooter(new BigDecimal(count)));
        beanWriter.write(new Footer(BigDecimal.ONE));
        beanWriter.close();
        out.close();
    }
}
