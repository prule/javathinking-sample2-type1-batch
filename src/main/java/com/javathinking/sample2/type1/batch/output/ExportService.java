package com.javathinking.sample2.type1.batch.output;

import com.javathinking.commons.validation.Validator;
import com.javathinking.sample2.common.file.input.FileParser;
import com.javathinking.sample2.common.pipeline.PipelineRunner;
import com.javathinking.sample2.common.pipeline.PipelineTask;
import com.javathinking.sample2.type1.batch.input.Type1BeanIoStreamingWriter;
import com.javathinking.sample2.type1.batch.output.ExportPipelineContext;
import com.javathinking.sample2.type1.transaction.QTransaction;
import com.javathinking.sample2.type1.transaction.TransactionRepository;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.H2Templates;
import com.mysema.query.sql.SQLQuery;
import org.beanio.StreamFactory;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date: 12/03/2014
 */
@Service
public class ExportService {
    private static final Logger log = LoggerFactory.getLogger(ExportService.class);

    @Autowired
    FileParser fileFormat;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    DataSource dataSource;
    @Autowired
    PlatformTransactionManager platformTransactionManager;
    @Autowired
    Validator validator;


    public void     doExport() throws IOException {
        // now process it
        // note, this pipeline could be constructed in the spring context if desired
//        PipelineRunner<ImportPipelineContext> pipelineRunner = new PipelineRunner(null);
        PipelineRunner<ExportPipelineContext> pipelineRunner = new PipelineRunner(platformTransactionManager);

        pipelineRunner.add(new PipelineTask<ExportPipelineContext>() {
            @Override
            public boolean execute(ExportPipelineContext context, Map data) throws Exception {
                final DateTime date1 = DateTime.now().withTimeAtStartOfDay();
                final DateTime date2 = date1.plusDays(1);
                StreamFactory factory = StreamFactory.newInstance();
                factory.loadResource("transactionFile.xml");
                final List<String> clients = transactionRepository.findAllDistinctClientsByDateBetween(date1, date2);

//                final Type1BeanIoWriter writer = new Type1BeanIoWriter(transactionRepository);
                {
                    final Type1BeanIoStreamingWriter writer = new Type1BeanIoStreamingWriter();
                    for (String client : clients) {
                        SQLQuery query = new SQLQuery(dataSource.getConnection(), new Configuration(new H2Templates()));
                        final QTransaction transaction = QTransaction.transaction;
                        final ResultSet resultSet = query.from(transaction)
                                .where(transaction.client.eq(client)
                                        .and(transaction.date.goe(date1)
                                                .and(transaction.date.lt(date2)))
                                )
                                .getResults(transaction.account, transaction.amount, transaction.date, transaction.client);

                        writer.write(new File(context.getOutputDirectory(), client + ".txt"), client, resultSet);
                    }
                }

                {
                    final Type1BeanIoStreamingWriter writer = new Type1BeanIoStreamingWriter();
                    for (String client : clients) {
                        SQLQuery query = new SQLQuery(dataSource.getConnection(), new Configuration(new H2Templates()));
                        final QTransaction transaction = QTransaction.transaction;
                        final ResultSet resultSet = query.from(transaction)
                                .groupBy(transaction.client)
                                .where(
                                        transaction.date.goe(date1)
                                                .and(transaction.date.lt(date2))
                                ).orderBy(transaction.client.asc())
                                .getResults(transaction.client.as("account"), transaction.amount.sum().as("amount"), transaction.date.min().as("date"), transaction.client);

                        writer.write(new File(context.getOutputDirectory(), "summary.txt"), client, resultSet);
                    }
                }

                return true;
            }

            @Override
            public String getName() {
                return "Export by client";
            }
        });


        final File outputDirectory = new File("/tmp/out");
        outputDirectory.mkdirs();
        ExportPipelineContext context = new ExportPipelineContext(outputDirectory);
        pipelineRunner.run(context, new HashMap());

        // check we have items in the database
        final long count = transactionRepository.count();
        log.debug("Found " + count + " transactions in database");
    }


}
