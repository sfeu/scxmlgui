package com.mxgraph.examples.swing.editor.fileimportexport;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;

public class SCXMLEdge implements Serializable {
	private static final long serialVersionUID = -136975794270718480L;
	
	public static final String INTERNALID="internalID";
	public static final String CONDITION="cond";
	public static final String EVENT="event";
	public static final String EDGEEXE="edgeexe";
	public static final String TARGETS="targets";
	public static final String SOURCE="source";
	public static final String EDGEORDER="edgeOrder"; // the order of this edge with respect to the other edges exiting the same node
	public static final String WITHTARGET="withTarget"; // an edge can be without target (event handler)

	public static final String EVENTUNDO="EVENTundo";
	public static final String EVENTDOC="EVENTdoc";
	public static final String CONDITIONUNDO="CONDundo";
	public static final String CONDITIONDOC="CONDdoc";
	public static final String EXEUNDO="EXEundo";
	public static final String EXEDOC="EXEdoc";

	private HashMap<String,Object> edge;
	public SCXMLEdge() {
		edge=new HashMap<String, Object>();
		edge.put(WITHTARGET, false);
		setSCXMLTargets(new ArrayList<String>());
		setEvent("");
	}
	public SCXMLEdge(String fromSCXMLID,ArrayList<String> toSCXMLIDs,String cond,String event, String content) {
		edge=new HashMap<String, Object>();
		edge.put(CONDITION,cond);
		edge.put(EVENT,event);
		edge.put(EDGEEXE,content);
		edge.put(SOURCE, fromSCXMLID);
		if (toSCXMLIDs==null) {
			ArrayList<String> targets = new ArrayList<String>();
			targets.add(fromSCXMLID);
			edge.put(TARGETS, targets);
			edge.put(WITHTARGET, false);
		} else {
			edge.put(TARGETS, toSCXMLIDs);
			edge.put(WITHTARGET, true);
		}
	}
	
	public boolean isCycle() {
		ArrayList<String> targets = getSCXMLTargets();
		return (targets.size()==1) && (getSCXMLSource().equals(targets.get(0)));
	}
	public boolean isCycleWithTarget() {
		return isCycle() && (Boolean)edge.get(WITHTARGET);
	}
	public void setCycleWithTarget(boolean withTarget) {
		edge.put(WITHTARGET, withTarget);
	}
	public String getSCXMLSource() {
		return (String)edge.get(SOURCE);
	}
	public void setSCXMLSource(String sourceID) {
		edge.put(SOURCE, sourceID);
	}
	public ArrayList<String> getSCXMLTargets() {
		return (ArrayList<String>)edge.get(TARGETS);
	}
	public void setSCXMLTargets(ArrayList<String> targetIDs) {
		edge.put(TARGETS, targetIDs);		
	}
	public String getInternalID() {
		return (String)edge.get(INTERNALID);
	}
	public void setInternalID(String internalID) {
		edge.put(INTERNALID, internalID);
	}
	public String getEvent() {
		String ret=null;
		Document dmd = getEventDoc();
		if (dmd!=null) {
			try {
				ret=dmd.getText(0, dmd.getLength());
			} catch (BadLocationException e) {
				ret=(String)edge.get(EVENT);
			}
		}
		else
			ret=(String)edge.get(EVENT);
		return (ret==null)?"":ret;
	}
	public void setEvent(String e) {
		edge.put(EVENT, e);
	}
	public String getCondition() {
		String ret=null;
		Document dmd = getConditionDoc();
		if (dmd!=null) {
			try {
				ret=dmd.getText(0, dmd.getLength());
			} catch (BadLocationException e) {
				ret=(String)edge.get(CONDITION);
			}
		}
		else
			ret=(String)edge.get(CONDITION);
		return (ret==null)?"":ret;
	}
	public void setCondition(String c) {
		edge.put(CONDITION, c);
	}
	public String getExe() {
		String ret=null;
		Document dmd = getExeDoc();
		if (dmd!=null) {
			try {
				ret=dmd.getText(0, dmd.getLength());
			} catch (BadLocationException e) {
				ret=(String)edge.get(EDGEEXE);
			}
		}
		else
			ret=(String)edge.get(EDGEEXE);
		return (ret==null)?"":ret;
	}
	public void setExe(String e) {
		edge.put(EDGEEXE, e);
	}
	// getter and setter for document and undomanager for editing event 
	public UndoManager getEventUndoManager() {
		return (UndoManager) edge.get(EVENTUNDO);
	}
	public UndoManager setEventUndoManager(UndoManager um) {
		edge.put(EVENTUNDO,um);
		return um;
	}
	public Document getEventDoc() {
		return (Document) edge.get(EVENTDOC);
	}
	public Document setEventDoc(Document doc) {
		edge.put(EVENTDOC,doc);
		return doc;
	}
	// getter and setter for document and undomanager for editing condition 
	public UndoManager getConditionUndoManager() {
		return (UndoManager) edge.get(CONDITIONUNDO);
	}
	public UndoManager setConditionUndoManager(UndoManager um) {
		edge.put(CONDITIONUNDO,um);
		return um;
	}
	public Document getConditionDoc() {
		return (Document) edge.get(CONDITIONDOC);
	}
	public Document setConditionDoc(Document doc) {
		edge.put(CONDITIONDOC,doc);
		return doc;
	}
	// getter and setter for document and undomanager for editing executable content 
	public UndoManager getExeUndoManager() {
		return (UndoManager) edge.get(EXEUNDO);
	}
	public UndoManager setExeUndoManager(UndoManager um) {
		edge.put(EXEUNDO,um);
		return um;
	}
	public Document getExeDoc() {
		return (Document) edge.get(EXEDOC);
	}
	public Document setExeDoc(Document doc) {
		edge.put(EXEDOC,doc);
		return doc;
	}
	public SCXMLEdge cloneEdge() {
		SCXMLEdge e=new SCXMLEdge();
		e.edge=(HashMap<String, Object>) this.edge.clone();
		// as for the node, set all documents to null, but gets the values they contain because it's the most updated.
		e.setConditionDoc(null);
		e.setConditionUndoManager(null);
		e.setCondition(getCondition());
		e.setEventDoc(null);
		e.setEventUndoManager(null);
		e.setEvent(getEvent());
		e.setExeDoc(null);
		e.setExeUndoManager(null);
		e.setExe(getExe());
		return e;
	}
	public void setOrder(int o) {
		assert(o>=0);
		edge.put(EDGEORDER, o);
	}
	public Integer getOrder() {
		if (edge.containsKey(EDGEORDER)) return (Integer)edge.get(EDGEORDER);
		else return null;
	}
	private void writeObject(ObjectOutputStream out) throws IOException
	{
		SCXMLEdge newe = cloneEdge();
		HashMap<String, Object> hash = edge;
		edge=newe.edge;
		out.defaultWriteObject();
		edge=hash;
	}
	public String toString() {
		return getSCXMLSource()+"-["+getCondition()+","+getEvent()+"]->"+getSCXMLTargets();
	}
	
	public String getStyle() {
		String ret="straight;strokeColor=#888888;";
		if (isCycle() && (!isCycleWithTarget())) ret+="strokeWidth=3;dashed=1;";
		return ret;
	}
}

