package com.javathinking.sample2.type1.batch.input;

import com.javathinking.sample2.type1.batch.input.model.*;
import com.javathinking.sample2.type1.batch.output.ExportService;
import com.javathinking.sample2.type1.transaction.TransactionRepository;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.math.BigDecimal;

/**
 * Date: 12/03/2014
 * <p/>
 * This is a sample program that either takes an input file as a parameter or manufactures a sample file
 * and then runs it through the import and export services.
 * <p/>
 * If you supply no parameters, here is what happens:
 * <p/>
 * 1. an input file is generated automatically so you have something to import - see /tmp/test1.txt - this file is a
 * fixed width format, with multiple groups of transactions. See transactionFile.xml to understand its format.
 * 2. this file is then imported - this is done with the importService, which is configured to
 *  - read the file, and generate a CSV - see /tmp/out.txt
 *  - then the csv is bulk loaded into the database (VERY FAST way to get data in a db)
 *  - some validation is performed against the data in the database (minimum amount checked)
 *  - some data transformation is performed (transactions with amount < 50 are marked as 'low', the rest as 'high')
 * 3. exports the data by client, and summarizes - see /tmp/out/*
 */
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private static final int numberOfTransactions = 10;

    public static void main(String[] args) throws IOException {

        File file1 = new File("/tmp/test1.txt");
        if (args.length > 0) {
            file1 = new File(args[0]);
        }
        else {
            // create a sample file and pretend it just got delivered
            generateFile(file1);
        }


        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("/application-context.xml");

        TransactionRepository transactionRepository = (TransactionRepository) appContext.getBean("transactionRepository");
        transactionRepository.deleteAllInBatch();

        ImportService importService = (ImportService) appContext.getBean("importService");
        importService.doImport(file1);


        ExportService exportService = (ExportService) appContext.getBean("exportService");
        exportService.doExport();
    }

    private static void generateFile(File file1) {
        log.debug("Generating file");
        file1.getParentFile().mkdirs();
        try {
            StreamFactory factory = StreamFactory.newInstance();
            factory.loadResource("transactionFile.xml");
            Writer out = new BufferedWriter(new FileWriter(file1));
            BeanWriter writer = factory.createWriter("transactionFile", out);

            writer.write(new Header(new DateTime()));
            String client = "CLIENT";
            int numOfClients = 5;
            for (int i = 0; i < numOfClients; i++) {
                writer.write(new GroupHeader(client + i));
                int count = numberOfTransactions / numOfClients;
                for (int x = 0; x < count; x++) {
                    double amount = ((int) (Math.random() * 10000)) / 100.0;
                    BigDecimal bd = new BigDecimal("" + amount);
                    writer.write(new TransactionLine(i + "ACC" + (x % 7), new DateTime(), bd));
                }
                writer.write(new GroupFooter(new BigDecimal(count)));
            }
            writer.write(new Footer(new BigDecimal(numOfClients)));
            writer.close();
            out.close();

        }
        catch (Exception e) {
            throw new RuntimeException("Could not generate file", e);
        }
        finally {
            log.debug("Finished generating file");
        }
    }

}
