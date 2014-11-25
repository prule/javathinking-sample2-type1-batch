package com.javathinking.sample2.type1.batch.input;

import com.javathinking.commons.validation.Validator;
import com.javathinking.sample2.common.file.input.FileParser;
import com.javathinking.sample2.common.pipeline.NativeSqlPipelineTask;
import com.javathinking.sample2.common.pipeline.PipelineRunner;
import com.javathinking.sample2.common.pipeline.ValidationTask;
import com.javathinking.sample2.type1.transaction.TransactionRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Date: 12/03/2014
 */
@Service
public class ImportService {
    private static final Logger log = LoggerFactory.getLogger(ImportService.class);
    @PersistenceContext
    private EntityManager em;
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


    public ImportPipelineContext doImport(File file1) throws IOException {
        log.debug("importing " + file1.getAbsolutePath());
        // now process it
        // note, this pipeline could be constructed in the spring context if desired
        PipelineRunner<ImportPipelineContext> pipelineRunner = new PipelineRunner(platformTransactionManager);
        pipelineRunner.add(new FileImportTask(fileFormat));
        pipelineRunner.add(new NativeSqlPipelineTask(em, "CSV import task", "insert into Transaction (FILEREF, CLIENT, ACCOUNT, AMOUNT, DATE) select fileref,client2,account,amount,parsedatetime(date,'yyyyMMdd') from CSVREAD('/tmp/out.txt','FILEREF,CLIENT2,ACCOUNT,AMOUNT,DATE',null)"));
        pipelineRunner.add(new ValidationTask(validator, true));
//        pipelineRunner.add(new NativeSqlPipelineTask(em, "Transform", "update Transaction set category = 'high' where amount > 50", "update Transaction set category = 'low' where amount <= 50"));
        pipelineRunner.add(new DataTransformTask(em));


        String fileContent = IOUtils.toString(new FileReader(file1));
        log.debug("IMPORTING FILE "+ fileContent);
        ImportPipelineContext context = new ImportPipelineContext(file1);
        log.debug("Processing file ref " + context.getFileRef());
        pipelineRunner.run(context, new HashMap());

        // print result
        log.debug("File Structure Errors = " + context.getFileFormatContext().getErrors());
        log.debug("Validation Errors = " + context.getValidationErrors());

        // check we have items in the database
        final long count = transactionRepository.count();
        log.debug("Found " + count + " transactions in database");
        return context;
    }


}
