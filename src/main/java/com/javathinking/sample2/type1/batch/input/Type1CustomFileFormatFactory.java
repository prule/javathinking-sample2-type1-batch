package com.javathinking.sample2.type1.batch.input;

import com.javathinking.sample2.common.file.input.FileParser;
import com.javathinking.sample2.common.file.input.custom.FileFormatBuilder;
import com.javathinking.sample2.common.file.input.custom.Line;
import com.javathinking.sample2.common.file.input.custom.LineBuilder;
import com.javathinking.sample2.common.file.input.custom.LineField;
import com.javathinking.sample2.type1.batch.input.model.*;

/**
 * Date: 12/03/2014
 */
public class Type1CustomFileFormatFactory {

    public FileParser create() {
        // "HH20140101"
        Line headerline = new LineBuilder()
                .withDefaultDateFormat("yyyyMMdd")
                .startsWith("HH")
                .withField().setWidth(8).setFieldName(HeaderObjectFactory.DATE).setType(LineField.Type.Day).add()
                .withObjectFactory(new HeaderObjectFactory())
                .build();

        // "GHCLIENT1   "
        Line groupHeaderline = new LineBuilder()
                .startsWith("GH")
                .withField().setWidth(10).setFieldName(GroupHeaderObjectFactory.CLIENT).setType(LineField.Type.Text).add()
                .withObjectFactory(new GroupHeaderObjectFactory())

                .build();

        // TXacc1      201401010001
        Line transactionlineA = new LineBuilder()
                .withDefaultDateFormat("yyyyMMdd")
                .startsWith("TX")
                .withField().setWidth(10).setFieldName(TransactionObjectFactory.ACCOUNT).setType(LineField.Type.Text).add()
                .withField().setWidth(8).setFieldName(TransactionObjectFactory.DATE).setType(LineField.Type.Day).add()
                .withField().setWidth(12).setFieldName(TransactionObjectFactory.AMOUNT).setType(LineField.Type.Decimal).add()
                .withObjectFactory(new TransactionObjectFactory())
                .build();

        // TYacc2      201401010002
        Line transactionlineB = new LineBuilder()
                .withDefaultDateFormat("yyyyMMdd")
                .startsWith("TY")
                .withField().setWidth(10).setFieldName(TransactionObjectFactory.ACCOUNT).setType(LineField.Type.Text).add()
                .withField().setWidth(8).setFieldName(TransactionObjectFactory.DATE).setType(LineField.Type.Day).add()
                .withField().setWidth(12).setFieldName(TransactionObjectFactory.AMOUNT).setType(LineField.Type.Decimal).add()
                .withObjectFactory(new TransactionObjectFactory())
                .build();

        // "GF0002"
        Line groupFooterline = new LineBuilder()
                .startsWith("GF")
                .withField().setWidth(10).setFieldName(GroupFooterObjectFactory.COUNT).setType(LineField.Type.Decimal).add()
                .withObjectFactory(new GroupFooterObjectFactory())
                .build();

        // "FF00000001"
        Line footerline = new LineBuilder()
                .withDefaultDateFormat("yyyyMMdd")
                .startsWith("FF")
                .withField().setWidth(8).setFieldName(FooterObjectFactory.COUNT).setType(LineField.Type.Decimal).add()
                .withObjectFactory(new FooterObjectFactory())
                .build();

        return new FileFormatBuilder()
                .withLines(headerline, groupHeaderline, transactionlineA, transactionlineB, groupFooterline, footerline)
                .build();
    }
}
