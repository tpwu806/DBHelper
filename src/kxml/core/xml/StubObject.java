package kxml.core.xml;
/*package com.db.kxml.core.xml;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;
import org.jdom.Attribute;
import org.jdom.Element;

public class StubObject implements Serializable {
	protected transient Map lostStubTable;
	static final long serialVersionUID = 1L;
	protected transient int level;
	protected transient Icon compIcon;
	protected transient Icon icon;
	protected transient Color frontColor;
	protected transient Color backColor;
	protected List childList;
	protected StubObject parentStub;
	protected Hashtable StubTable;
	protected long signature;

	public Object getLostValue(Object key, Object def) {
		if (this.lostStubTable == null)
			return def;
		Object v = this.lostStubTable.get(key);
		if (v == null)
			v = def;
		return v;
	}

	public void setLostValue(Object key, Object value) {
		if (this.lostStubTable == null)
			this.lostStubTable = new HashMap();
		this.lostStubTable.put(key, value);
	}

	public Icon getCompIcon() {
		return this.compIcon;
	}

	public void setCompIcon(Icon compIcon) {
		this.compIcon = compIcon;
	}

	public List getChildList() {
		return this.childList;
	}

	public void setChildList(List list) {
		this.childList = list;
	}

	public List removeChildList() {
		List list = this.childList;
		this.childList = null;
		return list;
	}

	public void addChild(StubObject SO) {
		if (this.childList == null)
			this.childList = new ArrayList();
		this.childList.add(SO);
	}

	public void removeChild(StubObject SO) {
		if (this.childList != null)
			this.childList.remove(SO);
	}

	public void setName(String name) {
		setObject("name", name);
	}

	public String getName() {
		return getString("name", null);
	}

	public boolean hasChild() {
		return ((this.childList != null) && (this.childList.size() != 0));
	}

	public StubObject[] getChilds() {
		if (this.childList == null)
			return null;
		StubObject[] SOS = new StubObject[this.childList.size()];
		return ((StubObject[]) this.childList.toArray(SOS));
	}

	public void setParent(StubObject SO) {
		this.parentStub = SO;
	}

	public StubObject getParent() {
		return this.parentStub;
	}

	public void setID(Object ID) {
		setObject("id", ID);
	}

	public Object getID() {
		return getObject("id", null);
	}

	public void setCaption(String Caption) {
		setObject("caption", Caption);
	}

	public String getCaption() {
		return getString("caption", null);
	}

	public void setStubTable(Hashtable StubTable) {
		this.StubTable = StubTable;
	}

	public Hashtable getStubTable() {
		return this.StubTable;
	}

	public StubObject() {
		this(null);
	}

	public StubObject(String id) {
		this.lostStubTable = null;

		this.level = 1;
		this.compIcon = null;

		this.icon = null;
		this.frontColor = null;
		this.backColor = null;

		this.childList = null;

		this.parentStub = null;
		this.StubTable = null;

		this.signature = 0L;

		setID(id);
	}

	public String toString() {
		return getCaption();
	}

	public void removeAll() {
		if (this.StubTable != null)
			this.StubTable.clear();
	}

	public Object getObject(Object Key, Object Default) {
		Object res = Default;
		if (this.StubTable != null) {
			Default = this.StubTable.get(Key);
			if (Default != null)
				res = Default;
		}
		return res;
	}

	public void setObject(Object Key, Object Value) {
		if (Value != null) {
			if (this.StubTable == null)
				this.StubTable = new Hashtable();
			this.StubTable.put(Key, Value);
		} else if (this.StubTable != null) {
			this.StubTable.remove(Key);
		}
	}

	public String getString(Object Key, String def) {
		String res = (String) getObject(Key, def);
		if (res == null)
			res = def;
		return res;
	}

	public void setString(Object key, String v) {
		setObject(key, v);
	}

	public int getInt(Object Key, int def) {
    int res = def; Object re = null;
    try {
      re = getObject(Key, String.valueOf(def));
      if (re instanceof Double) {
        res = ((Double)re).intValue(); break label72: }
      if (re instanceof Long) {
        res = ((Long)re).intValue(); break label72:
      }
      res = Integer.parseInt(re.toString());
    }
    catch (Exception e) {
      res = def;
    }
    label72: return res; }

	public void setInt(Object key, int v) {
		setObject(key, new Integer(v));
	}

	public boolean getBoolean(Object Key, boolean def) {
		boolean res = def;
		Object b = getObject(Key, new Boolean(def));
		if (b instanceof Boolean)
			res = ((Boolean) b).booleanValue();
		return res;
	}

	public void setBoolean(Object key, boolean v) {
		setObject(key, new Boolean(v));
	}

	public void setObjectFromXMLElemnt(Element e) {
		if (e == null)
			return;
		setStubID(e.getName());
		setNodeText(e.getText());
		List list = e.getAttributes();
		if (list == null)
			return;

		for (int i = 0; i < list.size(); ++i) {
			Attribute attr = (Attribute) list.get(i);
			if (attr != null) {
				String Name = attr.getName();
				String Value = attr.getValue();
				if ((Name != null) && (Value != null))
					setObject(Name, Value);
			}
		}
	}

	public static StubObject convertXML2Stub(JDOMXMLBaseObject XML) {
		return convertXML2Stub(XML, StubObject.class);
	}

	private static StubObject getStubObject(Class StubClass) {
		StubObject SO = null;
		try {
			SO = (StubObject) StubClass.newInstance();
		} catch (Exception localException) {
		}
		return SO;
	}

	public static StubObject convertXML2Stub(JDOMXMLBaseObject XML,
			Class StubClass) {
		StubObject SO = convertXML2Stub(XML, XML.Root, StubClass);
		return SO;
	}

	public static StubObject convertXML2Stub(JDOMXMLBaseObject XML, Element e,
			Class StubClass) {
		StubObject SO = getStubObject(StubClass);
		createXML2Stub(XML, e, SO);
		return SO;
	}

	public static void createXML2Stub(JDOMXMLBaseObject XML, Element e,
			StubObject SO) {
		List nodelist = XML.BeginEnumerate(e);
		int Index = 0;
		StubObject childSO = null;

		SO.setObjectFromXMLElemnt(e);
		while (nodelist != null) {
			Element node = XML.Enumerate(nodelist, Index++);
			if (node == null)
				break;
			childSO = getStubObject(SO.getClass());
			childSO.setParent(SO);
			SO.addChild(childSO);
			createXML2Stub(XML, node, childSO);
		}
		XML.EndEnumerate();
	}

	public boolean isSelected() {
		return getBoolean("selected", false);
	}

	public boolean isSelected(boolean def) {
		return getBoolean("selected", def);
	}

	public String getStubID() {
		return getString("stubID", null);
	}

	public Icon getIcon() {
		return this.icon;
	}

	public int getLevel() {
		return this.level;
	}

	public Color getBackColor() {
		return this.backColor;
	}

	public Color getFrontColor() {
		return this.frontColor;
	}

	public String getNodeText() {
		return getString("_nodeText_", null);
	}

	public String getNodeCData() {
		return getString("_nodeCData_", null);
	}

	public void setSelected(boolean b) {
		setBoolean("selected", b);
	}

	public void setStubID(String stubID) {
		setString("stubID", stubID);
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setBackColor(Color backColor) {
		this.backColor = backColor;
	}

	public void setFrontColor(Color frontColor) {
		this.frontColor = frontColor;
	}

	public void setNodeText(String nodeText) {
		setString("_nodeText_", nodeText);
	}

	public void setNodeCData(String nodeCData) {
		setString("_nodeCData_", nodeCData);
	}

	public long getLong(Object key, long def) {
		long res = def;
		Object re = null;
		try {
			Number num = (Number) getObject(key, new Long(def));
			res = num.longValue();
		} catch (Exception e) {
			res = def;
		}
		return res;
	}

	public void setLong(Object key, long v) {
		setObject(key, new Long(v));
	}

	public float getFloat(Object key, float def) {
		float res = def;
		Object re = null;
		try {
			Number num = (Number) getObject(key, new Float(def));
			res = num.floatValue();
		} catch (Exception e) {
			res = def;
		}
		return res;
	}

	public void setFloat(Object key, float v) {
		setObject(key, new Float(v));
	}

	public double getDouble(Object key, double def) {
		double res = def;
		Object re = null;
		try {
			Number num = (Number) getObject(key, new Double(def));
			res = num.doubleValue();
		} catch (Exception e) {
			res = def;
		}
		return res;
	}

	public void setDouble(Object key, double v) {
		setObject(key, new Double(v));
	}

	public byte getByte(Object key, byte def) {
		byte res = def;
		Object re = null;
		try {
			Number num = (Number) getObject(key, new Byte(def));
			res = num.byteValue();
		} catch (Exception e) {
			res = def;
		}
		return res;
	}

	public void setByte(Object key, byte v) {
		setObject(key, new Byte(v));
	}

	public short getShort(Object key, short def) {
		short res = def;
		Object re = null;
		try {
			Number num = (Number) getObject(key, new Short(def));
			res = num.shortValue();
		} catch (Exception e) {
			res = def;
		}
		return res;
	}

	public void setShort(Object key, short v) {
		setObject(key, new Short(v));
	}

	public Class getPluginClass() {
		return ((Class) getObject("pluginClass", null));
	}

	public void setPluginClass(Class pluginClass) {
		setObject("pluginClass", pluginClass);
	}

	public Object getPluginObject() {
		return getObject("pluginObject", null);
	}

	public void setPluginObject(Object pluginObject) {
		setObject("pluginObject", pluginObject);
	}

	public void assignStubObject(StubObject stub) {
		if (stub.getStubTable() == null)
			return;
		if (this.StubTable == null)
			this.StubTable = new Hashtable();
		this.StubTable.putAll(stub.getStubTable());
	}

	public long getSignature() {
		return this.signature;
	}

	public void setSignature(long s) {
		this.signature = s;
	}
}*/