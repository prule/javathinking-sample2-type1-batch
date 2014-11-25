package com.javathinking.sample2.type1.batch.input;

import com.javathinking.sample2.common.file.input.FileParser;
import com.javathinking.sample2.common.file.input.beanio.BeanIoFileReader;

import java.io.IOException;

/**
 * Date: 12/03/2014
 */
public class Type1BeanIoFileFormatFactory {

    public FileParser create() throws IOException {
        return new BeanIoFileReader("transactionFile", this.getClass().getResourceAsStream("/transactionFile.xml"));
    }
}
