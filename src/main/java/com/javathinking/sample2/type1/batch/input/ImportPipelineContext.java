package com.javathinking.sample2.type1.batch.input;

import com.javathinking.commons.validation.Errors;
import com.javathinking.sample2.common.file.input.FileFormatContext;
import com.javathinking.sample2.common.pipeline.PipelineContext;

import java.io.File;
import java.util.UUID;

/**
 * Date: 14/03/2014
 */
public class ImportPipelineContext extends PipelineContext {
    FileFormatContext fileFormatContext;
    File inputFile;
    Errors validationErrors;
    String fileRef;

    public ImportPipelineContext(File inputFile) {
        this.inputFile = inputFile;
        this.fileRef = UUID.randomUUID().toString();
    }

    public FileFormatContext getFileFormatContext() {
        return fileFormatContext;
    }

    public void setFileFormatContext(FileFormatContext fileFormatContext) {
        this.fileFormatContext = fileFormatContext;
    }

    public File getInputFile() {
        return inputFile;
    }

    public Errors getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Errors validationErrors) {
        this.validationErrors = validationErrors;

    }

    public String getFileRef() {
        return fileRef;
    }
}
