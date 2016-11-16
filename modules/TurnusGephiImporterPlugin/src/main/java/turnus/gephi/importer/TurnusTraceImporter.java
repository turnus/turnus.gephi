/* 
 * TURNUS - www.turnus.co
 * 
 * Copyright (C) 2010-2016 EPFL SCI STI MM
 *
 * This file is part of TURNUS.
 *
 * TURNUS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TURNUS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TURNUS.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Additional permission under GNU GPL version 3 section 7
 * 
 * If you modify this Program, or any covered work, by linking or combining it
 * with Eclipse (or a modified version of Eclipse or an Eclipse plugin or 
 * an Eclipse library), containing parts covered by the terms of the 
 * Eclipse Public License (EPL), the licensors of this Program grant you 
 * additional permission to convey the resulting work.  Corresponding Source 
 * for a non-source form of such a combination shall include the source code 
 * for the parts of Eclipse libraries used as well as that of the  covered work.
 * 
 */
package turnus.gephi.importer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.zip.GZIPInputStream;
import org.gephi.io.importer.api.ContainerLoader;
import org.gephi.io.importer.api.EdgeDraft;
import org.gephi.io.importer.api.Issue;
import org.gephi.io.importer.api.NodeDraft;
import org.gephi.io.importer.api.Report;
import org.gephi.io.importer.spi.FileImporter;
import turnus.gephi.importer.io.XmlTraceReader;

/**
 * File importer example which can import the Matrix Market file format. This
 * format is a text-based representation of a matrix and can be tested with
 * <a href="http://www2.research.att.com/~yifanhu/GALLERY/GRAPHS/index.html">Yifan
 * Hu's matrix gallery</a>.
 * <p>
 * The example show how graph data should be set in the {@link ContainerLoader}
 * instance. It shows how {@link NodeDraft} and {@link EdgeDraft} are created
 * from the factory. It also append logs in the {@link Report} class, which is
 * the standard way to report messages and issues.
 *
 * @author Mathieu Bastian
 * @author Simone Casale Brunet
 */
public class TurnusTraceImporter implements FileImporter {
    
    private final String filePath;
    private final boolean isCompressed;
    private final Report report;
    private final boolean cancel;

    private TurnusTraceOptions options;

    //Architecture
    private Reader reader;
    private ContainerLoader container;
    
    public TurnusTraceImporter(String filePath, boolean isCompressed) {
        super();

        this.filePath = filePath;
        this.isCompressed = isCompressed;
        this.report = new Report();
        cancel = false;
    }
    
    public void setOptions(TurnusTraceOptions options) {
        this.options = options;
    }
    

    @Override
    public boolean execute(ContainerLoader loader) {
         this.container = loader;

        new XmlTraceReader(reader, loader, report, options).read();

        if (isCompressed) {
            try {
                reader.close();
            } catch (Exception e) {
                report.logIssue(new Issue(e.toString(), Issue.Level.SEVERE));
            }
        }
        
        return !cancel;
    }

   

    @Override
    public void setReader(Reader reader) {
       if (isCompressed) {
            try {
                GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(filePath));
                this.reader = new BufferedReader(new InputStreamReader(gzip));
            } catch (Exception e) {
                report.logIssue(new Issue(e.toString(), Issue.Level.SEVERE));
            }
        } else {
            this.reader = reader;
        }
    }

    @Override
    public ContainerLoader getContainer() {
        return container;
    }

    @Override
    public Report getReport() {
        return report;
    }
}
