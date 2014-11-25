package com.javathinking.sample2.type1.batch.input;

import com.javathinking.sample2.common.file.input.FileFormatContext;
import com.javathinking.sample2.common.file.input.FileParser;
import com.javathinking.sample2.common.pipeline.PipelineTask;
import com.javathinking.sample2.type1.batch.input.ImportPipelineContext;

import java.io.FileReader;
import java.util.Map;

/**
 * Date: 23/03/2014
 */
public class FileImportTask implements PipelineTask<ImportPipelineContext> {
    private FileParser fileParser;

    public FileImportTask(FileParser fileParser) {
        this.fileParser = fileParser;
    }

    @Override
    public boolean execute(ImportPipelineContext context, Map data) throws Exception {
        FileFormatContext fileFormatContext = new FileFormatContext();
        fileFormatContext.setFileRef(context.getFileRef());
        context.setFileFormatContext(fileParser.parse(new FileReader(context.getInputFile()), fileFormatContext));
        if (context.getFileFormatContext().hasErrors()) {
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return "File import";
    }
}
