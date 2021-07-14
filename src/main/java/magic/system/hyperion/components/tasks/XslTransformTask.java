/*
 * The MIT License
 *
 * Copyright 2021 Thomas Lehmann.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package magic.system.hyperion.components.tasks;

import magic.system.hyperion.components.TaskParameters;
import magic.system.hyperion.components.TaskResult;
import magic.system.hyperion.tools.FileUtils;
import magic.system.hyperion.tools.TemplateEngine;
import magic.system.hyperion.tools.XmlTools;
import net.sf.saxon.s9api.SaxonApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Transformation of a XML via XSL.
 *
 * @author Thomas Lehmann
 */
public class XslTransformTask extends AbstractTask {
    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(XslTransformTask.class);

    /**
     * The transformation script (embedded or as path and filename).
     */
    private String strXsl;

    /**
     * The XML to transform (embedded or as path and filename).
     */
    private String strXml;

    /**
     * Initialize task with defaults.
     *
     * @param strInitTitle - title of the task.
     * @since 2.0.0
     */
    public XslTransformTask(String strInitTitle) {
        super(strInitTitle);
    }

    /**
     * Get transformation script (embedded or as path and filename).
     *
     * @return transformation script (embedded or as path and filename).
     * @since 2.0.0
     */
    public String getXsl() {
        return this.strXsl;
    }

    /**
     * Change Xsl.
     *
     * @param strInitXsl new XSL.
     * @since 2.0.0
     */
    public void setXsl(final String strInitXsl) {
        this.strXsl  = strInitXsl;
    }

    /**
     * Get the XML to transform (embedded or as path and filename).
     *
     * @return The XML to transform (embedded or as path and filename).
     * @since 2.0.0
     */
    public String getXml() {
        return this.strXml;
    }

    /**
     * Change Xml.
     *
     * @param strInitXml new XML.
     * @since 2.0.0
     */
    public void setXml(final String strInitXml) {
        this.strXml  = strInitXml;
    }

    @Override
    public TaskResult run(TaskParameters parameters) {
        TaskResult taskResult = null;
        final var engine = new TemplateEngine();

        logTitle(parameters);

        String strXslContent = this.strXsl;
        String strXmlContent = this.strXml;

        try {
            // TODO (FileUtils wegen Exception wegen "Paths.get"

            if (FileUtils.isRegularFile(this.strXsl)) {
                LOGGER.info("Processing XSL file {}", this.strXsl);
                strXslContent = Files.readString(Paths.get(this.strXsl));
            }

            if (FileUtils.isRegularFile(this.strXml)) {
                LOGGER.info("Processing XML file {}", this.strXml);
                strXmlContent = Files.readString(Paths.get(this.strXml));
            }

            // render model, matrix, variables, ...
            final var strRenderedXsl = engine.render(
                    strXslContent, parameters.getTemplatingContext());
            final var strRenderedXml = engine.render(
                    strXmlContent, parameters.getTemplatingContext());

            // store result into variable
            getVariable().setValue(XmlTools.transform(strRenderedXsl, strRenderedXml));
            taskResult = new TaskResult(true, getVariable());
        } catch (final IOException | SaxonApiException e) {
            LOGGER.error(e.getMessage(), e);
            taskResult = new TaskResult(false, getVariable());
        }

        return taskResult;
    }

    @Override
    public AbstractTask copy() {
        final var task = new XslTransformTask(this.getTitle());
        task.setXsl(this.getXsl());
        task.setXml(this.strXml);
        return task;
    }
}
