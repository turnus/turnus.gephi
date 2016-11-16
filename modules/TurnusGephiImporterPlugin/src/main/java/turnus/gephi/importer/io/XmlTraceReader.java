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
package turnus.gephi.importer.io;

import turnus.gephi.importer.TurnusTraceOptions;
import static turnus.gephi.importer.io.XmlTraceMarkup.*;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import org.gephi.io.importer.api.ContainerLoader;
import org.gephi.io.importer.api.EdgeDirection;
import org.gephi.io.importer.api.EdgeDraft;
import org.gephi.io.importer.api.ImportUtils;
import org.gephi.io.importer.api.Issue;
import org.gephi.io.importer.api.NodeDraft;
import org.gephi.io.importer.api.Report;

/**
 * 
 * @author Simone Casale Brunet
 *
 */
public class XmlTraceReader {

    private final XMLStreamReader reader;
    private final ContainerLoader loader;
    private final Report report;

    private final Dependency tempDep;
    private final Step tempStep;
    private final Trace tempTrace;
    private Attributable currentAttributable;

    private static final String STEP_ID_PREFIX = "s";
    private static final String DEPENDENCY_ID_PREFIX = "d";

    private Integer firings;
    private boolean load = true;

    private final TurnusTraceOptions options;

    public XmlTraceReader(Reader reader, ContainerLoader loader, Report report, TurnusTraceOptions options) {
        this.reader = ImportUtils.getXMLReader(reader);
        this.loader = loader;
        this.report = report;
        this.options = options;

        tempDep = new Dependency();
        tempStep = new Step();
        tempTrace = new Trace();

        firings = 0;

    }

    private void addStep(Step step) {

        String label = "step[" + step.id + "]:" + step.actor + ":" + step.action;
        NodeDraft node = loader.factory().newNodeDraft(STEP_ID_PREFIX + step.id);
        node.setLabel(label);
        node.setColor("65", "105", "205");
        node.setValue("actor", step.actor);
        node.setValue("action", step.action);
        node.setValue("actor-class", step.actorClass);
        node.setValue("step-id", step.id);

        loader.addNode(node);

        firings++;
        if (options.isFiringsLimit()) {
            load = options.getMaxFirings() >= firings;
            if (!load) {
                report.log("Reached the max number of loaded firings...");
            }
        }
    }

    private void addDependency(Dependency dep) {

        if (dep.kind.equals(DEPENDENCY_KIND_FSM)) {
            if (!options.isLoadFsm()) {
                return;
            }
        } else if (dep.kind.equals(DEPENDENCY_KIND_STATEVAR)) {
            if (!options.isLoadInternalVar()) {
                return;
            }
        } else if (dep.kind.equals(DEPENDENCY_KIND_PORT)) {
            if (!options.isLoadPort()) {
                return;
            }
        } else if (dep.kind.equals(DEPENDENCY_KIND_TOKENS)) {
            if (!options.isLoadTokens()) {
                return;
            }
        } else if (dep.kind.equals(DEPENDENCY_KIND_GUARD)) {
            if (!options.isLoadGuard()) {
                return;
            }
        } else if (dep.kind.equals(DEPENDENCY_KIND_UNKNOWN)) {
            if (!options.isLoadUnknown()) {
                return;
            }
        }

        String srcId = STEP_ID_PREFIX + dep.sourceId;
        String tgtId = STEP_ID_PREFIX + dep.targetId;
        String edgeId = DEPENDENCY_ID_PREFIX + srcId + ":" + tgtId;

        NodeDraft src = loader.getNode(srcId);
        NodeDraft tgt = loader.getNode(tgtId);

        EdgeDraft edge;
        if (loader.edgeExists(edgeId)) {
            edge = loader.getEdge(edgeId);
        } else {
            edge = loader.factory().newEdgeDraft(edgeId);
            edge.setSource(src);
            edge.setTarget(tgt);
            edge.setColor("85", "107", "47");
            edge.setDirection(EdgeDirection.DIRECTED);
            loader.addEdge(edge);
        }

        edge.setValue(dep.kind, "true");

    }

    public void read() {

        try {
            while (load && reader.hasNext()) {
                reader.next();
                switch (reader.getEventType()) {
                    case XMLEvent.START_ELEMENT:
                        startElement(reader.getName());
                        break;
                    case XMLEvent.END_ELEMENT:
                        endElement(reader.getName());
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            report.logIssue(new Issue(e.toString(), Issue.Level.SEVERE));
        }
    }

    private void endElement(QName qname) {
        String name = qname.toString();
        if (name.equals(TRACE)) {
            currentAttributable = null;

        } else if (name.equals(STEP)) {
            addStep(tempStep);
            currentAttributable = tempTrace;

        } else if (name.equals(DEPENDENCY)) {
            addDependency(tempDep);
            currentAttributable = tempTrace;
        }
    }

    private void startElement(QName qname) {
        String name = qname.toString();

        if (name.equals(TRACE)) {
            currentAttributable = tempTrace;
        } else if (name.equals(STEP)) {
            tempStep.clear();
            tempStep.id = reader.getAttributeValue("", STEP_FIRING);
            tempStep.actor = reader.getAttributeValue("", STEP_ACTOR);
            tempStep.action = reader.getAttributeValue("", STEP_ACTION);
            tempStep.actorClass = reader.getAttributeValue("", STEP_ACTOR_CLASS);
            currentAttributable = tempStep;
        } else if (name.equals(DEPENDENCY)) {
            tempDep.clear();
            tempDep.sourceId = reader.getAttributeValue("", DEPENDENCY_SOURCE);
            tempDep.targetId = reader.getAttributeValue("", DEPENDENCY_TARGET);
            tempDep.kind = reader.getAttributeValue("", DEPENDENCY_KIND);

            if (tempDep.kind.equals(DEPENDENCY_KIND_GUARD)) {
                tempDep.guard = reader.getAttributeValue("", GUARD);
                tempDep.direction = reader.getAttributeValue("",
                        DIRECTION);
            } else if (tempDep.kind.equals(DEPENDENCY_KIND_STATEVAR)) {
                tempDep.variable = reader.getAttributeValue("", VARIABLE);
                tempDep.direction = reader.getAttributeValue("",
                        DIRECTION);
            } else if (tempDep.kind.equals(DEPENDENCY_KIND_PORT)) {
                tempDep.port = reader.getAttributeValue("", PORT);
                tempDep.direction = reader.getAttributeValue("",
                        DIRECTION);
            } else if (tempDep.kind.equals(DEPENDENCY_KIND_TOKENS)) {
                tempDep.sourcePort = reader.getAttributeValue("", SOURCE_PORT);
                tempDep.targetPort = reader.getAttributeValue("", TARGET_PORT);
                tempDep.count = Integer.parseInt(reader.getAttributeValue("",
                        COUNT));
            }

            currentAttributable = tempDep;

        } else if (name.equals(ATTRIBUTE)) {
            String attName = reader.getAttributeValue("", ATTRIBUTE_NAME);
            Object attValue = parseAttribute();
            currentAttributable.setAttribute(attName, attValue);
        }
    }

    private Object parseAttribute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private interface Attributable {

        void setAttribute(String name, Object value);
    }

    private class Trace implements Attributable {

        @Override
        public void setAttribute(String name, Object value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    private class Dependency implements Attributable {

        private Integer count;
        private String direction;
        private String guard;
        private String kind;
        private String port;
        private String sourceId;
        private String sourcePort;
        private String targetId;
        private String targetPort;
        private String variable;
        private Map<String, Object> attributesMap = new HashMap<String, Object>();

        Dependency() {
            clear();
        }

        private void clear() {
            attributesMap.clear();
            direction = null;
            guard = null;
            kind = null;
            port = null;
            sourceId = null;
            targetId = null;
            sourcePort = null;
            targetPort = null;
            variable = null;
            count = null;
        }

        @Override
        public void setAttribute(String name, Object value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    private class Step implements Attributable {

        private String action;
        private String actor;
        private String actorClass;
        private String id;
        private Map<String, Object> attributesMap = new HashMap<String, Object>();

        Step() {
            clear();
        }

        @Override
        public void setAttribute(String name, Object value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        private void clear() {
            attributesMap.clear();
            id = null;
            actor = null;
            actorClass = null;
            action = null;
        }
    }
}
