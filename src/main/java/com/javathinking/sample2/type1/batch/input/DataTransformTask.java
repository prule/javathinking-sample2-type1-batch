package com.javathinking.sample2.type1.batch.input;

import com.javathinking.sample2.common.pipeline.PipelineTask;
import com.javathinking.sample2.type1.batch.input.ImportPipelineContext;
import com.javathinking.sample2.type1.transaction.QTransaction;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.Map;

/**
 * Date: 23/03/2014
 */
public class DataTransformTask implements PipelineTask<ImportPipelineContext> {
    private static final Logger log = LoggerFactory.getLogger(DataTransformTask.class);

    private EntityManager entityManager;

    public DataTransformTask(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Mutate the data for future processing using appropriate business logic
     */
    @Override
    public boolean execute(ImportPipelineContext context, Map data) throws Exception {
        final QTransaction transaction = QTransaction.transaction;


        long countLow = getJpaUpdateClause(transaction).where(transaction.amount.lt(50.0))
                .set(transaction.category, "low")
                .execute();
        log.debug("count low = " + countLow);


        long countHigh = getJpaUpdateClause(transaction)
                .where(transaction.amount.goe(50.0)
                )
                .set(transaction.category, "high")
                .execute();
        log.debug("count high = " + countHigh);

        return true;
    }

    private JPAUpdateClause getJpaUpdateClause(QTransaction transaction) {
        return new JPAUpdateClause(entityManager, QTransaction.transaction).where(
                transaction.fileRef.eq(transaction.fileRef));
    }

    @Override
    public String getName() {
        return "Transform";
    }

}
