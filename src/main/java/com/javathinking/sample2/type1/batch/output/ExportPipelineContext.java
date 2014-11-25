package com.javathinking.sample2.type1.batch.output;

import com.javathinking.commons.validation.Errors;
import com.javathinking.sample2.common.file.input.FileFormatContext;
import com.javathinking.sample2.common.pipeline.PipelineContext;

import java.io.File;
import java.util.UUID;

/**
 * Date: 14/03/2014
 */
public class ExportPipelineContext extends PipelineContext {
    FileFormatContext fileFormatContext;
    File outputDirectory;

    public ExportPipelineContext(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public FileFormatContext getFileFormatContext() {
        return fileFormatContext;
    }

    public void setFileFormatContext(FileFormatContext fileFormatContext) {
        this.fileFormatContext = fileFormatContext;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

}
