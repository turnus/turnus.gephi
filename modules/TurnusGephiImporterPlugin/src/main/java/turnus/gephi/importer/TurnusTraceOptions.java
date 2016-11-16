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

/**
 * 
 * @author Simone Casale Brunet
 *
 */
public class TurnusTraceOptions {

    public static boolean DEFAULT_FSM = true;
    public static boolean DEFAULT_GUARD = true;
    public static boolean DEFAULT_INTERNAL_VARS = true;
    public static boolean DEFAULT_PORT = true;
    public static boolean DEFAULT_TOKENS = true;
    public static boolean DEFAULT_UNKNOWN = false;
    public static boolean DEFAULT_FIRINGS_LIMIT = false;
    public static int DEFAULT_MAX_FIRINGS = 1000;

    private boolean loadFsm = DEFAULT_FSM;
    private boolean loadGuard = DEFAULT_GUARD;
    private boolean loadInternalVar = DEFAULT_INTERNAL_VARS;
    private boolean loadPort = DEFAULT_PORT;
    private boolean loadTokens = DEFAULT_TOKENS;
    private boolean loadUnknown = DEFAULT_UNKNOWN;
    private int maxFirings = DEFAULT_MAX_FIRINGS;
    private boolean firingsLimit = DEFAULT_FIRINGS_LIMIT;

    public boolean isLoadFsm() {
        return loadFsm;
    }

    public void setLoadFsm(boolean loadFsm) {
        this.loadFsm = loadFsm;
    }

    public boolean isLoadGuard() {
        return loadGuard;
    }

    public void setLoadGuard(boolean loadGuard) {
        this.loadGuard = loadGuard;
    }

    public boolean isLoadInternalVar() {
        return loadInternalVar;
    }

    public void setLoadInternalVar(boolean loadInternalVar) {
        this.loadInternalVar = loadInternalVar;
    }

    public boolean isLoadPort() {
        return loadPort;
    }

    public void setLoadPort(boolean loadPort) {
        this.loadPort = loadPort;
    }

    public boolean isLoadTokens() {
        return loadTokens;
    }

    public void setLoadTokens(boolean loadTokens) {
        this.loadTokens = loadTokens;
    }

    public boolean isLoadUnknown() {
        return loadUnknown;
    }

    public void setLoadUnknown(boolean loadUnknown) {
        this.loadUnknown = loadUnknown;
    }

    public int getMaxFirings() {
        return maxFirings;
    }

    public void setMaxFirings(int maxFirings) {
        this.maxFirings = maxFirings;
    }

    public boolean isFiringsLimit() {
        return firingsLimit;
    }

    public void setFiringsLimit(boolean firingsLimit) {
        this.firingsLimit = firingsLimit;
    }

}

