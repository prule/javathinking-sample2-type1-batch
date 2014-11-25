package com.javathinking.sample2.type1.batch.input;

import com.javathinking.commons.validation.Errors;
import com.javathinking.commons.validation.ValidationError;
import com.javathinking.commons.validation.ValidationRule;
import com.javathinking.sample2.type1.batch.input.ImportPipelineContext;
import com.javathinking.sample2.type1.transaction.TransactionRepository;

import java.math.BigDecimal;

public class MinimumAmountRule implements ValidationRule<ImportPipelineContext> {
    private TransactionRepository transactionRepository;
    private BigDecimal minimumAmount;

    public MinimumAmountRule(TransactionRepository transactionRepository, BigDecimal minimumAmount) {
        this.transactionRepository = transactionRepository;
        this.minimumAmount = minimumAmount;
    }

    @Override
    public Errors validate(ImportPipelineContext context) {
        Errors errors = new Errors();
        long amount = transactionRepository.countByFileRefAndAmountLessThan(context.getFileRef(), minimumAmount);
        if (amount > 0) {
            errors.add(new ValidationError("Amount less than " + minimumAmount, amount + " transactions have an amount less than " + minimumAmount));
        }
        return errors;
    }

}
