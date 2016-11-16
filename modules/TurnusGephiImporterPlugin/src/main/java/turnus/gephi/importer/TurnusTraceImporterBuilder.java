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

import org.gephi.io.importer.api.FileType;
import org.gephi.io.importer.spi.FileImporter;
import org.gephi.io.importer.spi.FileImporterBuilder;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.ServiceProvider;

/**
 * File Importer builder implementation for the Matrix Market file format. The
 * builder is responsible for creating the importer's instances.
 * 
 * @author Mathieu Bastian
 * @author Simone Casale Brunet
 */
@ServiceProvider(service = FileImporterBuilder.class)
public class TurnusTraceImporterBuilder implements FileImporterBuilder {

    public static final String IDENTIFER = "trace";
    public static final String[] EXTENSIONS = {".trace", ".tracez"};
    public static final String DESCRIPTION = "Turnus execution trace file";
    
// these variables are used to naively support tracez gz compressed files...
    private String filePath;
    private boolean isCompressed;

    @Override
    public FileImporter buildImporter() {
        return new TurnusTraceImporter(filePath, isCompressed);
    }

    @Override
    public String getName() {
        return IDENTIFER;
    }

    @Override
    public FileType[] getFileTypes() {
        return new FileType[]{new FileType(EXTENSIONS, DESCRIPTION)};
    }

    @Override
    public boolean isMatchingImporter(FileObject fileObject) {
       if (fileObject != null) {
            if (fileObject.getExt().equalsIgnoreCase("trace")) {
                isCompressed = false;
                filePath = fileObject.getPath();
                return true;
            } else if (fileObject.getExt().equalsIgnoreCase("tracez")) {
                isCompressed = true;
                filePath = fileObject.getPath();
                return true;
            }
        }
        
        filePath = null;
        return false;
    }
}
